package sw.world.interfaces;

import arc.math.geom.*;
import arc.struct.*;
import mindustry.gen.*;
import sw.world.graph.*;
import sw.world.meta.*;
import sw.world.modules.*;

public interface HasSpin extends Buildingc {
	/**
	 * Method indicating if a gas building can connect to another gas building. Mutually inclusive.
	 */
	static boolean connects(HasSpin from, HasSpin to) {
		return from.connectTo(to) && to.connectTo(from);
	}

	/**
	 * Returns true if this accepts gas from another gas building, does not mean that the other gas building will output.
	 */
	@Deprecated
	default boolean acceptsGas(HasSpin from, float amount) {
		return getGas() + amount <= from.getGas() - amount;
	}

	/**
	 * Method indicating if this building connects to another gas building.
	 * @apiNote Liz, do not make this method call itself on another instance, and do not make this null, ever.
	 */
	default boolean connectTo(HasSpin other) {
		return spinConfig().hasSpin && other.team() == team() && (spinConfig().connections.isEmpty() || spinConfig().connections.contains(new Point2(other.tileX(), other.tileY()).sub(tileX(), tileY())));
	}

	SpinModule spin();
	SpinConfig spinConfig();
	default SpinGraph spinGraph() {
		return spin().graph;
	}

	default HasSpin getGasDestination(HasSpin from) {
		return this;
	}

	@Deprecated
	default float getGas() {
		return spin().amount;
	}
	/**
	 * Returns the pressure inside a block, lower than gas amount if less than gas capacity, more than gas amount if greater than gasCapacity.
	 */
	@Deprecated
	default float getGasPressure() {
		return spin().amount * (spin().amount/ spinConfig().gasCapacity);
	}

	/**
	 * Transfer gas from a building to another, provided that this building accepts, and the source outputs.
	 * @param force forces the transfer to happen regardless of previous conditions.
	 */
	@Deprecated
	default void handleGas(HasSpin source, float amount, boolean force) {
		if ((source.outputsGas(this, amount) && acceptsGas(source, amount)) || force) {
			source.spin().subAmount(amount);
			spin().addAmount(amount);
		}
	}

	/**
	 * Returns a seq with the gas buildings that this build can connect to.
	 */
	default Seq<HasSpin> nextBuilds() {
		return proximity().select(b -> b instanceof HasSpin a && connects(this, a.getGasDestination(this))).map(a -> ((HasSpin) a).getGasDestination(this));
	}

	/**
	 * Should be called whenever connections changed.
	 */
	default void onGraphUpdate() {

	}

	/**
	 * Returns true if this can output gas to another gas building. Does not mean that the other gas building will accept.
	 */
	@Deprecated
	default boolean outputsGas(HasSpin to, float amount) {
		return to.getGas() + amount <= getGas() - amount;
	}

	default void updateGas() {

	}
}
