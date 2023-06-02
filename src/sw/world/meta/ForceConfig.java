package sw.world.meta;

import mindustry.world.meta.*;

public class ForceConfig {
	public float maxForce = 5f;
	public float beltSizeIn = 2f;
	public float beltSizeOut = 2f;
	public float friction = 0.5f;
	public float range = 80f;
	public boolean acceptsForce = true, outputsForce = true;

	public void addStats(Stats stats) {
		stats.add(SWStat.maxForce, maxForce * 5 * 60, StatUnit.perMinute);
		stats.add(SWStat.beltSizeIn, beltSizeIn / 2f, StatUnit.blocksSquared);
		stats.add(SWStat.beltSizeOut, beltSizeOut / 2f, StatUnit.blocksSquared);
		stats.add(SWStat.baseResistance, friction);
		stats.add(Stat.range, range/8f, StatUnit.blocks);
	}
}
