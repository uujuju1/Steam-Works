package sw.world.meta;

import mindustry.world.meta.*;

public class VibrationConfig {
	public float range = 160f, resistance = 0.1f;
	public boolean acceptsVibration = true, outputsVibration = true;

	public void addStats(Stats stats) {
		stats.add(SWStat.soundResistance, resistance * 60f, StatUnit.perSecond);
		stats.add(SWStat.wireRange, range/8f, StatUnit.blocks);
		stats.add(SWStat.acceptsVibration, acceptsVibration);
		stats.add(SWStat.outputsVibration, acceptsVibration);
	}
}
