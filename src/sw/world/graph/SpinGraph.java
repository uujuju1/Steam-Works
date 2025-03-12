package sw.world.graph;

import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.content.*;
import sw.gen.*;
import sw.world.interfaces.*;

/**
 * Graph containing an isolated group of uildings sharing common stuff.
 */
public class SpinGraph {
	public float rotation;
	public float speed;

	/**
	 * Wether the graph has changed in any way like adding/removing builds.
	 */
	public boolean changed;

	/**
	 * Used when updating inertia.
	 */
	public float lastSpeed;

	/**
	 * Entity of this graph.
	 */
	public final SpinGraphUpdater updater = SpinGraphUpdater.create().setGraph(this);

	/**
	 * List of buildings of this graph.
	 */
	public final Seq<HasSpin> builds = new Seq<>();
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

	/**
	 * Adds a build to this graph, and removes the build from its older graph.
	 */
	public void addBuild(HasSpin build) {
		builds.add(build);
		if (build.outputsSpin()) producers.add(build);
		if (build.consumesSpin()) consumers.add(build);
		checkEntity();
		build.spinGraph().remove(build, false);
		build.spin().graph = this;
		changed = true;
	}

	/**
	 * Disables the updater if there are less than a single building.
	 */
	public void checkEntity() {
		if (builds.size > 0) {
			updater.add();
		} else {
			updater.remove();
		}
	}

	/**
	 * Returns the force that all builds are doing to push the whole system.
	 */
	public float force() {
		return builds.sumf(HasSpin::getForce);
	}

	/**
	 * Merges this graph with another one. if other graph is bigger, merge the other graph with this one.
	 * @param priority If true, the graph will always merge with the other graph.
	 */
	public void merge(SpinGraph other, boolean priority) {
		if (other.builds.size > builds.size && !priority) {
			other.merge(this, false);
		} else {
			other.builds.each(this::addBuild);
		}
		builds.each(HasSpin::onGraphUpdate);
	}
	public void mergeFlood(HasSpin other) {
		tmp.clear().add(other);
		tmp2.clear();

		while(!tmp.isEmpty()) {
			HasSpin next = tmp.pop();
			tmp2.addUnique(next);
			next.nextBuilds().each(b -> {
				if (!tmp2.contains(b)) tmp.add(b);
			});
		}

		tmp2.each(this::addBuild);
	}

	/**
	 * Removes a building from this graph.
	 * @param split if true, makes each connection a separate graph.
	 */
	public void remove(HasSpin build, boolean split) {
		builds.remove(build);
		producers.remove(build);
		consumers.remove(build);
		if (split) build.nextBuilds().each(p -> {
			new SpinGraph().mergeFlood(p);
		});
		checkEntity();
		changed = true;
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

	/**
	 * Called every frame by the updater.
	 */
	public void update() {
		if (changed) {
			updateRatios(builds.first());
			var fastest = ratios.keys().toArray().max(build -> ratios.get(build, 1) * (producers.contains(build) ? 1f : -1f));
			consumers.each(consumer -> {
				updateRatios(consumer);
				relativeRatios.put(consumer, new ObjectFloatMap<>(ratios));
			});
			updateRatios(fastest);
			builds.each(b -> {
				b.spin().section = new SpinSection();
				b.spinSection().addBuild(b);
			});
			builds.each(HasSpin::onGraphUpdate);
			updateInertia();
			changed = false;
		}

		float accel = Math.abs(force() - resistance());
		speed = Mathf.approachDelta(speed, targetSpeed(), accel);

		if (invalid) {
			speed = 0;
			if (Mathf.maxZero(force() - resistance()) > 0) {
				var b = builds.random();

				b.damage(Mathf.maxZero(force() - resistance()));
				Fx.smoke.at(b);
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
}
