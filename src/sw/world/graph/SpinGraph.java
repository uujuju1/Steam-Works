package sw.world.graph;

import arc.math.*;
import arc.struct.*;
import arc.util.*;
import sw.*;
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
	public float friction, inertia = 1;

	public float staticFriction, dynamicFriction;
	public Seq<ForceEntry> staticTorque = new Seq<>();
	public Seq<ForceEntry> dynamicTorque = new Seq<>();

	public float torque, targetSpeed;

	public static final Seq<ForceEntry> tmpTorque = new Seq<>();

	/**
	 * List of buildings of this graph.
	 */
	public final Seq<HasSpin> staticBuilds = new Seq<>();
	public final Seq<HasSpin> dynamicBuilds = new Seq<>();
	/**
	 * Buildings that aren't connected but still influence this graph with force.
	 */
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
		if (build.spinConfig().hasStaticTorque || build.spinConfig().hasStaticFriction) staticBuilds.addUnique(build);
		if (!build.spinConfig().hasStaticTorque || !build.spinConfig().hasStaticFriction) dynamicBuilds.addUnique(build);
		build.spin().graph = this;
	}
	
	/**
	 * Returns the force that all builds are doing to push the whole system.
	 */
	public float force() {
		return torque;
	}
	
	@Override
	public void graphChanged() {
		updateRatios(builds.first());
		builds.each(HasSpin::onGraphUpdate);
		updateInertia();
		
//		friction = builds.sumf(HasSpin::getResistance);
		inertia = Math.max(1f, builds.sumf(HasSpin::getInertia));

		updateStaticForces();
	}

	public void mergeFlood(HasSpin other) {
		floodFill(other, HasSpin::nextBuilds).each(build -> {
			if (build.spin() != null && build.spinGraph() != null && build.spinGraph() != this) {
				if (build.spinConfig().disconnected) {
					disconnected.addUnique(build);
				} else {
					build.spinGraph().removeBuild(build);
					addBuild(build);
				}
			}
		});
	}

	@Override
	public void removeBuild(HasSpin build) {
		super.removeBuild(build);
		staticBuilds.remove(build);
		dynamicBuilds.remove(build);
	}
	
	@Override
	public void update() {
		super.update();

		updateDynamicForces();

		tmpTorque.clear();
		staticTorque.each(forceEntry -> {
			if (tmpTorque.contains(forceEntry)) {
				tmpTorque.get(tmpTorque.indexOf(forceEntry)).value += forceEntry.value;
			} else tmpTorque.add(forceEntry);
		});
		dynamicTorque.each(forceEntry -> {
			if (tmpTorque.contains(forceEntry)) {
				tmpTorque.get(tmpTorque.indexOf(forceEntry)).value += forceEntry.value;
			} else tmpTorque.add(forceEntry);
		});

		targetSpeed = 0;
		torque = tmpTorque.sumf(forceEntry -> forceEntry.value);

		friction = staticFriction + dynamicFriction;

		tmpTorque.sort(forceEntry -> -forceEntry.speed);

		for (int i = 0; i < tmpTorque.size; i++) {
			ForceEntry key = tmpTorque.get(i);
			float prev = i < 1 ? 0 : tmpTorque.get(i - 1).value;
			if (torque - prev >= friction) {
				targetSpeed = key.speed;
				torque -= prev;
			}
		};
		
		float accel = Math.abs(torque - friction) / inertia;
		
		speed = Mathf.approachDelta(speed, targetSpeed, accel);

		if (invalid) {
			speed = 0;
			// TODO make it better
		}

		if (lastSpeed != speed) {
			lastSpeed = speed;
			builds.each(b -> b.spin().inertia = speed);
		}

		rotation += speed * Time.delta;
	}

	public void updateDynamicForces() {
		dynamicFriction = 0;
		dynamicTorque.clear();
		dynamicBuilds.each(b -> {
			if (!b.spinConfig().hasStaticFriction) dynamicFriction += b.getResistance();
			if (!b.spinConfig().hasStaticTorque) {
				ForceEntry forceEntry = new ForceEntry(b.spinConfig().checkSpeed ? b.getTargetSpeed() : Float.POSITIVE_INFINITY, b.getForce());
				if (dynamicTorque.contains(forceEntry)) {
					dynamicTorque.get(dynamicTorque.indexOf(forceEntry)).value += forceEntry.value;
				} else dynamicTorque.add(forceEntry);
			}
		});
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

	public void updateStaticForces() {
		staticFriction = 0;
		staticTorque.clear();
		staticBuilds.each(b -> {
			if (b.spinConfig().hasStaticFriction) staticFriction += b.getResistance();
			if (b.spinConfig().hasStaticTorque) {
				ForceEntry forceEntry = new ForceEntry(b.spinConfig().checkSpeed ? b.getTargetSpeed() : Float.POSITIVE_INFINITY, b.getForce());
				if (staticTorque.contains(forceEntry)) {
					staticTorque.get(staticTorque.indexOf(forceEntry)).value += forceEntry.value;
				} else staticTorque.add(forceEntry);
			}
		});	}
	
	@Override
	public boolean validGraph() {
		return !builds.removeAll(b -> !b.asBuilding().isValid() && b.spinGraph() != this).isEmpty();
	}

	// janky
	public static class ForceEntry {
		public final float speed;
		public float value;

		public ForceEntry(float speed, float value) {
			this.speed = speed;
			this.value = value;
		}

		@Override
		public boolean equals(Object other) {
			return other instanceof ForceEntry forceEntry && Mathf.equal(forceEntry.speed, speed, SWVars.speedTolerance);
		}

		@Override
		public String toString() {
			return speed + "=" + value;
		}
	}
}
