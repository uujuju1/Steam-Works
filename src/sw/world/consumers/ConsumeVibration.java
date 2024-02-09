package sw.world.consumers;

import mindustry.gen.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;
import sw.world.interfaces.*;
import sw.world.meta.*;

public class ConsumeVibration extends Consume {
	public float frequency;

	public ConsumeVibration(float frequency) {
		this.frequency = frequency;
	}

	@Override public float efficiency(Building build) {
		return (build instanceof HasVibration hasBuild && hasBuild.vibrationGraph().hasFrequency(frequency)) ? 1f : 0f;
	}
	@Override public float efficiencyMultiplier(Building build) {
		return (build instanceof HasVibration hasBuild && hasBuild.vibrationGraph().hasFrequency(frequency)) ? 1f : 0f;
	}

	@Override public void display(Stats stats) {
		stats.add(SWStat.consumeFrequency, frequency);
	}
}
