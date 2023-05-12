package sw.world.consumers;

import arc.math.*;
import mindustry.gen.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;
import sw.world.interfaces.*;
import sw.world.meta.*;

public class ConsumeTorque extends Consume {
	public float amount, scl;

	public ConsumeTorque(float amount, float scl) {
		this.amount = amount;
		this.scl = scl;
	}

	@Override public float efficiency(Building build) {
		return build instanceof HasForce b ? Mathf.clamp(Math.abs(b.torque()) - amount) : 0f;
	}
	@Override public float efficiencyMultiplier(Building build) {
		return build instanceof HasForce b ? 1f + Mathf.map(Math.abs(b.torque()), amount, amount + scl, 0, 1) : 0f;
	}

	@Override public void display(Stats stats) {
		stats.add(SWStat.strengthConsume, amount);
	}
}
