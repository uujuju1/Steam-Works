package sw.world.interfaces;

import arc.struct.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.world.*;
import sw.world.consumers.*;
import sw.world.graph.*;
import sw.world.meta.*;
import sw.world.modules.*;

public interface HasSpin {
	default Building asBuilding() {
		return (Building) this;
	}
	/**
	 * Method indicating if a gas building can connect to another building. Mutually inclusive.
	 */
	static boolean connects(@Nullable HasSpin from, @Nullable HasSpin to) {
		if (from == null || to == null) return false;
		return from.connectTo(to) && to.connectTo(from);
	}

	/**
	 * Method indicating if this building connects to another building.
	 * @apiNote Liz, do not make this method call itself on another instance, and do not make this null, ever.
	 */
	default boolean connectTo(HasSpin other) {
		boolean hasSpin = spinConfig().hasSpin;
		boolean sameTeam = other.asBuilding().team == asBuilding().team;
		boolean isEdge = !asBuilding().proximity.contains((Building) other);
		if (spinConfig().allowedEdges != null) {
			for(int i : spinConfig().allowedEdges[asBuilding().rotation]) {
				isEdge |= asBuilding().nearby(
					Edges.getEdges(asBuilding().block.size)[i].x,
					Edges.getEdges(asBuilding().block.size)[i].y
				) == other;
			}
		} else isEdge = true;
		return hasSpin && sameTeam && isEdge;
	}

	default boolean consumesSpin() {
		return asBuilding().block.findConsumer(c -> c instanceof ConsumeRotation) != null;
	}

	/**
	 * Returns the destination to connect with the graph.
	 */
	default HasSpin getSpinGraphDestination(HasSpin from) {
		return this;
	}
	/**
	 * Returns the destination to connect with the section.
	 * @see SpinSection
	 * @apiNote if called, should be done after getSpinGraphDestination
	 */
	default @Nullable HasSpin getSpinSectionDestination(HasSpin from) {
		return this;
	}

	default SpinModule spin() {
		try {
			return (SpinModule) asBuilding().getClass().getField("spin").get(this);
		} catch (Exception ignored) {
			return null;
		}
	}
	default SpinConfig spinConfig() {
		try {
			return (SpinConfig) asBuilding().block.getClass().getField("spinConfig").get(asBuilding().block);
		} catch (Exception ignored) {
			return null;
		}
	}
	default SpinGraph spinGraph() {
		return spin().graph;
	}
	default SpinSection spinSection() {
		return spin().section;
	}

	/**
	 * Returs the force that this build applies on the system.
	 */
	default float getForce() {
		return 0;
	}
	/**
	 * Returns the resistance that this build applies on the system.
	 */
	default float getResistance() {
		return spinConfig().resistance / spinGraph().ratios.get(this, 1);
	}
	/**
	 * @return the current rotation of this system scaled by this build's ratio
	 */
	default float getRotation() {
		return spinGraph().rotation / spinGraph().ratios.get(this, 1);
	}
	/**
	 * Returns the speed that this block should try to reach.
	 */
	default float getTargetSpeed() {
		return 0;
	}

	/**
	 * @return true whenever the connection is invalid, if true, the graph should not move and should break if it tries.
	 */
	default boolean invalidConnection(HasSpin other, float ratio, float lastRatio) {
		return ratio != lastRatio;
	}

	/**
	 * Returns a seq with the buildings that this build can connect to.
	 */
	default Seq<HasSpin> nextBuilds() {
		return asBuilding().proximity
			     .select(b -> b instanceof HasSpin a && connects(this, a.getSpinGraphDestination(this)))
			     .map(a -> ((HasSpin) a)
				   .getSpinGraphDestination(this));
	}

	/**
	 * Called whenever connections changed.
	 */
	default void onGraphUpdate() {
		nextBuilds().map(b -> b.getSpinSectionDestination(this)).removeAll(b -> !connects(this, b)).each(b -> b.spinSection().merge(spinSection()));
	}

	default boolean outputsSpin() {
		return false;
	}

	/**
	 * @return the ratio of other relative to this build and the last build before this on the floodFill for the ratios.
	 */
	default float ratioOf(HasSpin other, HasSpin last, float startRatio, float lastRatio) {
		return startRatio;
	}
}
