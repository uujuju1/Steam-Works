package sw.world.consumers;

import mindustry.gen.*;
import mindustry.world.consumers.*;
import sw.world.interfaces.*;

public class ConsumeVibration extends Consume {
	public float frequency;

	public ConsumeVibration(float frequency) {
		this.frequency = frequency;
	}

	@Override
	public float efficiency(Building build) {
		if (build instanceof HasVibration hasBuild) {
			return hasBuild.vGraph().hasFrequency(frequency) ? 1f : 0f;
		}
		return 0f;
	}
	@Override
	public float efficiencyMultiplier(Building build) {
		if (build instanceof HasVibration hasBuild) {
			return hasBuild.vGraph().hasFrequency(frequency) ? 1f : 0f;
		}
		return 0f;
	}
}
