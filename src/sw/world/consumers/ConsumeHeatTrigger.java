package sw.world.consumers;

import arc.math.*;
import mindustry.gen.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;
import sw.world.interfaces.*;
import sw.world.meta.*;

public class ConsumeHeatTrigger extends Consume {
	public float amount;
	public float min;
	public boolean scales;

	public ConsumeHeatTrigger(float amount, float min, boolean scales) {
		this.amount = amount;
		this.min = min;
		this.scales = scales;
	}

	@Override public void trigger(Building build) {
		if (build instanceof HasHeat next) next.module().subHeat(amount);
	}

	@Override public float efficiency(Building build) {
		return build instanceof HasHeat next && next.module().heat >= min ? 1f : 0f;
	}
	@Override public float efficiencyMultiplier(Building build) {
		if (!scales) return 1f;
		return build instanceof HasHeat next ? Mathf.map(next.module().heat, 0, min, 0, 1) : 0f;
	}

	@Override public void display(Stats stats) {
		stats.add(SWStat.heatUseOnce, amount, StatUnit.degrees);
	}
}
