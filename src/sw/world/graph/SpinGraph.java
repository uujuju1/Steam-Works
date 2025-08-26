package sw.world.graph;

import arc.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.gen.*;
import sw.entities.units.*;
import sw.world.interfaces.*;

/**
 * Graph containing an isolated group of uildings sharing common stuff.
 */
public class SpinGraph extends Graph<HasSpin> {
	static {
		Events.on(EventType.BlockBuildEndEvent.class, e -> {
			if (e.tile.build instanceof HasSpin spin && !e.breaking) {
				if (spin.spinConfig() != null && spin.spinConfig().hasSpin) {
					new SpinGraph().mergeFlood(spin);
				}
			}
		});
		Events.on(EventType.BlockBuildBeginEvent.class, e -> {
			GraphUpdater updater = ((GraphUpdater) Groups.all.find(
				entity -> entity instanceof GraphUpdater &&
					          ((GraphUpdater) entity).graph instanceof SpinGraph spinGraph &&
					          spinGraph.builds.contains(build -> build.asBuilding().tile == e.tile)
			));
			if (e.breaking && updater != null) {
				SpinGraph graph = (SpinGraph) updater.graph;
				HasSpin build = graph.builds.find(b -> b.asBuilding().tile == e.tile);
				graph.removeBuild(build);
				build.nextBuilds().each(next -> {
					new SpinGraph().mergeFlood(next);
				});
			}
		});
	}
	
	public float rotation;
	public float speed;

	/**
	 * Used when updating inertia.
	 */
	public float lastSpeed;

	/**
	 * List of buildings of this graph.
	 */
	public final Seq<HasSpin> producers = new Seq<>();
	public final Seq<HasSpin> consumers = new Seq<>();

	/**
	 * Temporary seqs for use in flood.
	 */
	public static final Seq<HasSpin> tmp = new Seq<>(), tmp2 = new Seq<>();
	public static final ObjectMap<HasSpin, HasSpin> mainConnection = new ObjectMap<>();

	public final ObjectFloatMap<HasSpin> ratios = new ObjectFloatMap<>();
	public final ObjectMap<HasSpin, ObjectFloatMap<HasSpin>> relativeRatios = new ObjectMap<>();
	public boolean invalid;

	@Override
	public void addBuild(HasSpin build) {
		super.addBuild(build);
		if (build.outputsSpin()) producers.add(build);
		if (build.consumesSpin()) consumers.add(build);
		build.spin().graph = this;
	}
	
	@Override
	public void graphChanged() {
		updateRatios(builds.first());
		var fastest = ratios.keys().toArray().max(build -> ratios.get(build, 1) * (producers.contains(build) ? 1f : -1f));
		consumers.each(consumer -> {
			updateRatios(consumer);
			relativeRatios.put(consumer, new ObjectFloatMap<>(ratios));
		});
		updateRatios(fastest);
		builds.each(HasSpin::onGraphUpdate);
		updateInertia();
	}
	
	/**
	 * Returns the force that all builds are doing to push the whole system.
	 */
	public float force() {
		return builds.sumf(HasSpin::getForce);
	}

	public void mergeFlood(HasSpin other) {
		floodFill(other, HasSpin::nextBuilds).each(build -> {
			if (build.spinGraph() != this) {
				build.spinGraph().removeBuild(build);
				addBuild(build);
			}
		});
	}

	@Override
	public void removeBuild(HasSpin build) {
		super.removeBuild(build);
		producers.remove(build);
		consumers.remove(build);
	}

	/**
	 * Returns the resistance of the whole system from moving.
	 */
	public float resistance() {
		return builds.sumf(HasSpin::getResistance);
	}

	/**
	 * Returns the speed that the system is trying to reach.
	 */
	public float targetSpeed() {
		if (force() < resistance()) return 0;
		if (force() == resistance()) return speed;
		return builds.max(HasSpin::getTargetSpeed).getTargetSpeed();
	}
	
	@Override
	public void update() {
		super.update();
		
		float accel = Math.abs(force() - resistance());
		speed = Mathf.approachDelta(speed, targetSpeed(), accel);

		if (invalid) {
			speed = 0;
			if (Mathf.maxZero(force() - resistance()) > 0) {
				var b = builds.random();

				b.asBuilding().damage(Mathf.maxZero(force() - resistance()));
				Fx.smoke.at(b.asBuilding());
			}
		}

		if (lastSpeed != speed) {
			lastSpeed = speed;
			builds.each(b -> b.spin().inertia = speed);
		}

		rotation += speed * Time.delta;
	}

	public void updateInertia() {
		speed = builds.sumf(b -> b.spin().inertia)/builds.size;
	}

	public void updateRatios(HasSpin start) {
		invalid = false;
		tmp.clear().add(start);
		ratios.clear();
		ratios.put(start, 1);
		mainConnection.clear();
		mainConnection.put(start, start);

		final HasSpin[] last = new HasSpin[1];

		while (!tmp.isEmpty()) {
      HasSpin build = tmp.pop();

		  build.nextBuilds().each(other -> {
		    if (!mainConnection.containsKey(other)) {
		      tmp.add(other);
		      mainConnection.put(other, build);
		      last[0] = mainConnection.get(build);

			    invalid |= other != last[0] && (build.invalidConnection(other, ratios.get(other, 1), ratios.get(other, 1)));

			    ratios.put(other, build.ratioOf(other, last[0], ratios.get(build, 1), ratios.get(last[0], 1)));
		    } else {
		      last[0] = mainConnection.get(build);
		      float h = build.ratioOf(other, last[0], ratios.get(build, 1), ratios.get(last[0], 1));

		      invalid |= other != last[0] && (build.invalidConnection(other, h, ratios.get(other, 1)));

		    }
		  });

		  last[0] = build;
		}
	}
	
	@Override
	public boolean validGraph() {
		return !builds.removeAll(b -> !b.asBuilding().isValid() && b.spinGraph() != this).isEmpty();
	}
}
