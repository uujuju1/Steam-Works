package sw.world.graph;

import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.content.*;
import sw.world.interfaces.*;

/**
 * Graph containing an isolated group of uildings sharing common stuff.
 */
public class SpinGraph extends Graph<HasSpin> {
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

	public final ObjectFloatMap<HasSpin> ratios = new ObjectFloatMap<>();
	public boolean invalid;

	@Override
	public void addBuild(HasSpin build) {
		super.addBuild(build);
		if (build.outputsSpin()) producers.add(build);
		if (build.consumesSpin()) consumers.add(build);
		build.spin().graph = this;
	}
	
	/**
	 * Returns the force that all builds are doing to push the whole system.
	 */
	public float force() {
		return builds.sumf(HasSpin::getForce);
	}
	
	@Override
	public void graphChanged() {
		updateRatios(builds.first());
		builds.each(HasSpin::onGraphUpdate);
		updateInertia();
	}
	
	public float inertia() {
		return builds.sumf(HasSpin::getInertia) + 1f;
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
	@Deprecated public float targetSpeed() {
		if (force() < resistance()) return 0;
		if (force() == resistance()) return speed;
		return builds.max(HasSpin::getTargetSpeed).getTargetSpeed();
	}
	
	@Override
	public void update() {
		super.update();
		
		float netTorque = force();
		float netFriction = resistance();
		float netInertia = inertia();
		
		boolean stops = Math.abs(netTorque) < netFriction;
		
		float accel = (netTorque + netFriction * -Mathf.sign(speed))/netInertia;
		
		if (stops && Math.abs(speed) < netFriction) {
			speed = 0;
		} else speed += accel;

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
		tmp2.clear();
		ratios.clear();
		ratios.put(start, 1);

		while (!tmp.isEmpty()) {
      HasSpin current = tmp.pop();
			tmp2.add(current);

		  current.nextBuilds().each(next -> {
			  if (!tmp2.contains(next)) {
				  tmp.addUnique(next);
				  ratios.put(next, current.ratioTo(next) * next.ratioScl(current));
			  } else {
				  if (current.ratioInvalid(next) || next.ratioInvalid(current)) invalid = true;
			  }
		  });
		}
	}
	
	@Override
	public boolean validGraph() {
		return !builds.removeAll(b -> !b.asBuilding().isValid() && b.spinGraph() != this).isEmpty();
	}
}
