package sw.world.consumers;

import mindustry.gen.*;
import mindustry.world.consumers.*;
import sw.world.interfaces.*;

public class ConsumeVibration extends Consume {
	public float min, max;

	public ConsumeVibration(float min, float max) {
		this.min = min;
		this.max = max;
	}

	@Override public float efficiency(Building build) {
		if (build instanceof HasVibration hasBuild) {
			for (float f : hasBuild.vGraph().frequencies.toArray()) if (f >= min && f <= max) return 1f;
		}
		return 0f;
	}
	@Override public float efficiencyMultiplier(Building build) {
		if (build instanceof HasVibration hasBuild) {
			for (float f : hasBuild.vGraph().frequencies.toArray()) if (f >= min && f <= max) return 1f;
		}
		return 0f;
	}
}
