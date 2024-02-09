package sw.world.interfaces;

import arc.struct.*;
import mindustry.gen.*;
import sw.world.graph.*;
import sw.world.meta.*;
import sw.world.modules.*;

public interface HasVibration extends Buildingc {
	VibrationModule vibration();

	VibrationConfig vibrationConfig();
	default VibrationGraph vibrationGraph() {
		return vibration().graph;
	}

	default Seq<HasVibration> nextBuilds() {
		return proximity().select(b -> b instanceof HasVibration).<HasVibration>as().map(b -> b.getVibrationDestination(this)).removeAll(b -> {
			return !connects(b) && proximity().contains((Building) b);
		});
	}

	/**
	 * static connection(useful for pipes and bitmask related things)
	 */
	default boolean connects(HasVibration to) {
		return vibrationConfig().outputsVibration || vibrationConfig().acceptsVibration;
	}

	/**
	 * vibration destination
	 */
	default HasVibration getVibrationDestination(HasVibration from) {
		return this;
	}
}
