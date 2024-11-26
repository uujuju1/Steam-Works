package sw.world.consumers;

import arc.*;
import arc.math.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;
import sw.world.interfaces.*;
import sw.world.meta.*;

public class ConsumeRotation extends Consume {
	public float startSpeed, endSpeed;
	public Interp curve = Interp.one;

	public HasSpin cast(Building build) {
		try {
			return (HasSpin) build;
		} catch (ClassCastException e) {
			throw new RuntimeException("This consumer requires a block that supports the rotatiuon system, use a different consumer", e);
		}
	}

	@Override public void display(Stats stats) {
		stats.add(SWStat.spinRequirement, Core.bundle.format("stat.sw-spin-requirement.format", Strings.autoFixed(startSpeed * 10f, 2), Strings.autoFixed(endSpeed * 10f, 2)));
	}

	@Override
	public float efficiency(Building build) {
		if (getSpeed(build) >= startSpeed && getSpeed(build) <= endSpeed) {
			return curve.apply(Mathf.map(getSpeed(build), startSpeed, endSpeed, 0f, 1f));
		}
		return 0f;
	}
	@Override
	public float efficiencyMultiplier(Building build) {
		if (getSpeed(build) >= startSpeed && getSpeed(build) <= endSpeed) {
			return curve.apply(Mathf.map(getSpeed(build), startSpeed, endSpeed, 0f, 1f));
		}
		return 0f;
	}

	public float getSpeed(Building build) {
		return cast(build).spinGraph().speed * cast(build).spinSection().ratio;
	}
}
