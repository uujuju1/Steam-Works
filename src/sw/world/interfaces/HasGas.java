package sw.world.interfaces;

import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import mindustry.gen.*;
import sw.world.graph.*;
import sw.world.meta.*;
import sw.world.modules.*;

public interface HasGas extends Buildingc {
	/**
	 * Method indicating if a gas building can connect to another gas building. Mutually inclusive.
	 */
	static boolean connects(HasGas from, HasGas to) {
		return from.connectTo(to) && to.connectTo(from);
	}

	/**
	 * Returns true if this accepts gas from another gas building, does not mean that the other gas building will output.
	 */
	default boolean acceptsGas(HasGas from, float amount) {
		return getGas() + amount <= from.getGas() - amount;
	}

	/**
	 * Method indicating if this building connects to another gas building.
	 * @apiNote Liz, do not make this method call itself on another instance, and do not make this null, ever.
	 */
	default boolean connectTo(HasGas other) {
		return gasConfig().hasGas && other.team() == team() && (gasConfig().connections.isEmpty() || gasConfig().connections.contains(new Point2(other.tileX(), other.tileY()).sub(tileX(), tileY())));
	}

	GasModule gas();
	GasConfig gasConfig();
	default GasGraph gasGraph() {
		return gas().graph;
	}

	default HasGas getGasDestination(HasGas from) {
		return this;
	}

	default float getGas() {
		return gas().amount;
	}
	/**
	 * Returns the pressure inside a block, lower than gas amount if less than gas capacity, more than gas amount if greater than gasCapacity.
	 */
	default float getGasPressure() {
		return gas().amount * (gas().amount/gasConfig().gasCapacity);
	}

	/**
	 * Returns the amount of gas left to fill the block with a safe amount of gas.
	 */
	default float getMaximumAcceptedGas(float amount) {
		return Mathf.clamp(getGas() + amount, 0, gasConfig().gasCapacity - getGas());
	}

	/**
	 * Transfer gas from a building to another, provided that this building accepts, and the source outputs.
	 * @param force forces the transfer to happen regardless of previous conditions.
	 */
	default void handleGas(HasGas source, float amount, boolean force) {
		if ((source.outputsGas(this, amount) && acceptsGas(source, amount)) || force) {
			source.gas().subAmount(amount);
			gas().addAmount(amount);
		}
	}

	/**
	 * Returns a seq with the gas buildings that this build can connect to.
	 */
	default Seq<HasGas> nextBuilds() {
		return proximity().select(b -> b instanceof HasGas a && connects(this, a.getGasDestination(this))).map(a -> ((HasGas) a).getGasDestination(this));
	}

	/**
	 * Should be called whenever connections changed.
	 */
	default void onGasGraphUpdate() {

	}

	/**
	 * Returns true if this can output gas to another gas building. Does not mean that the other gas building will accept.
	 */
	default boolean outputsGas(HasGas to, float amount) {
		return to.getGas() + amount <= getGas() - amount;
	}

	default void updateGas() {
		if (getGasPressure() > gasConfig().maxPressure) damage(gasConfig().overpressureDamage);
	}
}
