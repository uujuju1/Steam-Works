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
		return build instanceof HasForce b && Math.abs(b.speed()) > min && Math.abs(b.speed()) < max ? 1f : 0f;
	}
	@Override public float efficiencyMultiplier(Building build) {
		return build instanceof HasForce b ? 1f + Mathf.map(Math.abs(b.speed()), min, max, 0, 1) : 0f;
	}

	@Override
	public void display(Stats stats) {
		stats.add(SWStat.minSpeedConsume, min, StatUnit.perMinute);
		stats.add(SWStat.maxSpeedConsume, max, StatUnit.perMinute);
	}
}
