package sw.world.interfaces;

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
	 * Method indicating if this building connects to another gas building.
	 * @apiNote Liz, do not make this method call itself on another instance.
	 */
	default boolean connectTo(HasGas other) {
		return true;
	}

	GasModule gas();
	GasConfig gasConfig();
	default GasGraph gasGraph() {
		return gas().graph;
	}

	default float getGas() {
		return gas().amount;
	}
	default float getGasPressure() {
		return gas().amount * (gas().amount/gasConfig().gasCapacity);
	}

	/**
	 * Returns a seq with the gas buildings that this build can connect to.
	 */
	default Seq<HasGas> nextBuilds() {
		return proximity().select(b -> b instanceof HasGas a && connects(this, a)).as();
	}

	/**
	 * Should be called whenever connections changed.
	 */
	default void onGasGraphUpdate() {

	}
}
