package sw.world.consumers;

import arc.math.*;
import mindustry.gen.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;
import sw.world.interfaces.*;
import sw.world.meta.*;

public class ConsumeSpeed extends Consume {
	public float min, max;

	public ConsumeSpeed(float min, float max) {
		this.min = min;
		this.max = max;
	}

	@Override public float efficiency(Building build) {
		return build instanceof HasForce b && Math.abs(b.graph().getSpeed()) < max ? Mathf.clamp(Math.abs(b.graph().getSpeed()) - min) : 0f;
	}
	@Override public float efficiencyMultiplier(Building build) {
		return build instanceof HasForce b ? 1f + Mathf.map(Math.abs(b.graph().getSpeed()), min, max, 0, 1) : 0f;
	}

	@Override
	public void display(Stats stats) {
		stats.add(SWStat.minSpeedConsume, min, StatUnit.perMinute);
		stats.add(SWStat.maxSpeedConsume, max, StatUnit.perMinute);
	}
}
