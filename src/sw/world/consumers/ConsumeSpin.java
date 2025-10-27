package sw.world.consumers;

import arc.*;
import arc.math.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;
import sw.world.interfaces.*;
import sw.world.meta.*;

public class ConsumeSpin extends Consume {
	public float minSpeed = Float.NEGATIVE_INFINITY;
	public float maxSpeed = Float.POSITIVE_INFINITY;
	
	/**
	 * Interpolation curve between the speed and the efficiency multiplier.
	 */
	public Interp efficiencyScale = Interp.linear;
	
	public HasSpin cast(Building build) {
		try {
			return (HasSpin) build;
		} catch (ClassCastException e) {
			throw new RuntimeException("This consumer requires a block that supports the rotation system, use a different consumer", e);
		}
	}
	
	@Override public void display(Stats stats) {
		stats.add(SWStat.spinRequirement, Core.bundle.format("stat.sw-spin-requirement.format", Strings.autoFixed(minSpeed * 10f, 2), Strings.autoFixed(maxSpeed * 10f, 2)));
	}
	
	@Override
	public float efficiency(Building build) {
		return getSpeed(build) >= minSpeed && getSpeed(build) <= maxSpeed ? 1f : 0f;
	}
	@Override
	public float efficiencyMultiplier(Building build) {
		if (getSpeed(build) >= minSpeed && getSpeed(build) <= maxSpeed) {
			return efficiencyScale.apply(getSpeed(build));
		}
		return 0f;
	}
	
	public float getSpeed(Building build) {
		return cast(build).getSpeed();
	}
}
