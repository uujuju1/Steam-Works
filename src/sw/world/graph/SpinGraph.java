package sw.world.graph;

import arc.math.*;
import arc.struct.*;
import arc.util.*;
import sw.world.interfaces.*;

/**
 * Graph containing an isolated group of buildings sharing common stuff.
 */
public class SpinGraph extends Graph<HasSpin> {
	public float rotation;
	public float speed;

	/**
	 * Used when updating inertia.
	 */
	public float lastSpeed;
	
	/**
	 * constant values based on the builds.
	 */
	public float friction, inertia;
	
	public float torque, targetSpeed;

	/**
	 * List of buildings of this graph.
	 */
	public final Seq<HasSpin> producers = new Seq<>();
	public final Seq<HasSpin> consumers = new Seq<>();
	/**
	 * Buildings that aren't connected but still influence this graph with force.
	 */
	// TODO generalize to include disconnected builds in floodFill
	public final Seq<HasSpin> disconnected = new Seq<>();
	
	/**
	 * Ratios used to scale quantities.
	 */
	public final ObjectFloatMap<HasSpin> ratios = new ObjectFloatMap<>();
	public boolean invalid;

	/**
	 * Temporary fields for use in calculations.
	 */
	public static final Seq<HasSpin> tmp = new Seq<>(), tmp2 = new Seq<>();
	public static @Nullable SpinGraph graphContext;

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
//		graphContext = this;
//		return producers.sumf(HasSpin::getForce) + disconnected.removeAll(build -> !build.asBuilding().isValid()).sumf(HasSpin::getForce);
		return torque;
	}
	
	@Override
	public void graphChanged() {
		disconnected.clear();
		updateRatios(builds.first());
		builds.each(HasSpin::onGraphUpdate);
		updateInertia();
		
		friction = builds.sumf(HasSpin::getResistance);
		inertia = Math.max(1f, builds.sumf(HasSpin::getInertia));
	}

	public void mergeFlood(HasSpin other) {
		floodFill(other, HasSpin::nextBuilds).each(build -> {
			if (build.spin() != null && build.spinGraph() != null && build.spinGraph() != this) {
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
	
	@Override
	public void update() {
		super.update();
		
		targetSpeed = 0;

		tmp.set(producers);
		tmp.add(disconnected);
		
		graphContext = this;
		for(HasSpin build : tmp) {
			if ((build.getTargetSpeed() > targetSpeed) && tmp.sumf(b -> b.getTargetSpeed() >= build.getTargetSpeed() || !b.spinConfig().checkSpeed ? b.getForce() : 0f) >= friction) targetSpeed = build.getTargetSpeed();
		}
		
		torque = tmp.sumf(b -> b.getTargetSpeed() >= speed || !b.spinConfig().checkSpeed ? b.getForce() : 0f);
		
		float accel = Math.abs(torque + friction * -Mathf.sign(speed))/inertia;
		
		speed = Mathf.approachDelta(speed, targetSpeed, accel);

		if (invalid) {
			speed = 0;
			// TODO make it better
//			if (Math.abs(accel) > 0) {
//				var b = builds.random();
//
//				b.asBuilding().damage(Math.abs(accel));
//				if (Mathf.chance(0.5)) Fx.smoke.at(b.asBuilding());
//			}
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
