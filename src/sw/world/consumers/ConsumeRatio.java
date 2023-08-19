package sw.world.consumers;

import arc.*;
import mindustry.gen.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;
import sw.world.graph.*;
import sw.world.interfaces.*;
import sw.world.meta.*;

public class ConsumeRatio extends Consume {
	public ForceGraph.ForceRatio ratio;

	public ConsumeRatio(ForceGraph.ForceRatio ratio) {
		this.ratio = ratio;
	}

	@Override public float efficiency(Building build) {
		return build instanceof HasForce b && b.getRatio() == ratio ? 1: 0f;
	}
	@Override public float efficiencyMultiplier(Building build) {
		return build instanceof HasForce b && b.getRatio() == ratio ? 1: 0f;
	}

	@Override public void display(Stats stats) {
		stats.add(SWStat.strengthConsume, Core.bundle.get(ratio.name()));
	}
}
