package sw.world.meta;

import mindustry.world.meta.*;

public class ForceConfig {
	public float
		maxForce = 5f,
		beltSizeIn = 2f,
		beltSizeOut = 2f,
		baseResistance = 0.5f,
		resistanceScl = 1f,
		range = 80f;
	public boolean acceptsForce = true, outputsForce = true;

	public void addStats(Stats stats) {
		stats.add(SWStat.maxForce, maxForce, StatUnit.perMinute);
		stats.add(SWStat.beltSizeIn, beltSizeIn * 8f, StatUnit.blocksSquared);
		stats.add(SWStat.beltSizeOut, beltSizeOut * 8f, StatUnit.blocksSquared);
		stats.add(SWStat.baseResistance, baseResistance);
		stats.add(SWStat.resistanceScl, resistanceScl);
		stats.add(Stat.range, range/8f, StatUnit.blocks);
	}
}
