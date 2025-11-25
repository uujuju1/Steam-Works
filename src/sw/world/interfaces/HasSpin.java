package sw.world.interfaces;

import arc.math.*;
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
	 * Method indicating if a building can connect to another building. Mutually inclusive.
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
		if (spinConfig() == null) return false;
		boolean hasSpin = spinConfig().hasSpin;
		boolean sameTeam = other.asBuilding().team == asBuilding().team;
		boolean isAllowed = !spinConfig().connectors.contains(other.asBuilding().block) ^ spinConfig().connectorAllowList;
		boolean isEdge = !asBuilding().proximity.contains((Building) other);
		if (spinConfig().allowedEdges != null) {
			for(int i : spinConfig().allowedEdges[asBuilding().rotation]) {
				isEdge |= asBuilding().nearby(
					Edges.getEdges(asBuilding().block.size)[i].x,
					Edges.getEdges(asBuilding().block.size)[i].y
				) == other;
			}
		} else isEdge = true;
		return hasSpin && sameTeam && isEdge && isAllowed;
	}

	default boolean consumesSpin() {
		return asBuilding().block.findConsumer(c -> c instanceof ConsumeSpin) != null;
	}

	/**
	 * Returns the destination to connect with the graph.
	 */
	default HasSpin getSpinGraphDestination(HasSpin from) {
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

	/**
	 * @return The force that this build applies on the system.
	 */
	default float getForce() {
		return 0;
	}
	/**
	 * @return The force that this build applies to the specific graph.
	 */
	default float getForceDisconnected(SpinGraph to) {
		return 0;
	}
	/**
	 * Returns the resistance that this build applies on the system.
	 */
	default float getResistance() {
		return spinConfig().resistance * getRatio();
	}
	/**
	 * @return The current ratio of this block relative to the whole graph.
	 */
	default float getRatio() {
		return spinGraph().ratios.get(this, 1);
	}
	/**
	 * @return The current rotation of this system scaled by this build's ratio
	 */
	default float getRotation() {
		return spinGraph().rotation / getRatio();
	}
	/**
	 * @return The current speed of this system scaled by the ratio.
	 */
	default float getSpeed() {
		return spinGraph().speed / getRatio();
	}
	/**
	 * @return The current inertia of this block.
	 */
	default float getInertia() {
		return spinConfig().inertia / getRatio();
	}
	/**
	 * Returns the speed that this block should try to reach.
	 */
	default float getTargetSpeed() {
		return 0;
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
	}

	default boolean outputsSpin() {
		return false;
	}
	
	/**
	 * Called whenever a "new" building is found whose radius was already set.
	 * @return True if the ratio that this building would set to the other is different than what the other currently has.
	 */
	default boolean ratioInvalid(HasSpin with) {
		return !Mathf.zero(ratioTo(with) * with.ratioScl(this) - with.spinGraph().ratios.get(with, 1), 0.00001f);
	}
	
	/**
	 * @return The ratio between this building and the other.
	 */
	default float ratioScl(HasSpin from) {
		return 1f;
	}
	
	/**
	 * @return The ratio this building will appear to have to the other building.
	 * Multiplied with the other's {@link #ratioScl} with this as a parameter will result in the other's ratio relative to the entire graph.
	 */
	default float ratioTo(HasSpin to) {
		return spinGraph().ratios.get(this, 1);
	}
}
