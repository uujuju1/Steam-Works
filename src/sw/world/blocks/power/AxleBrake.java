package sw.world.blocks.power;

import arc.*;
import arc.math.*;
import arc.util.*;
import sw.world.meta.*;

public class AxleBrake extends AxleBlock {
	public float beginBrakeSpeed = 1f, endBrakeSpeed = 2f;
	
	public float brakeStrength = 1f / 600f;
	
	public AxleBrake(String name) {
		super(name);
	}
	
	@Override
	public void init() {
		super.init();
		
		if (spinConfig != null) {
			spinConfig.checkSpeed = false;
		}
	}
	
	@Override
	public void setStats() {
		super.setStats();
		stats.add(SWStat.spinRequirement, Core.bundle.format(
			"stat.sw-spin-requirement.format",
			Strings.autoFixed(beginBrakeSpeed * 600f, 2),
			Strings.autoFixed(endBrakeSpeed * 600f, 2)
		));
		stats.add(SWStat.spinOutputForce, brakeStrength * 600f, SWStat.force);
	}
	
	public class AxleBrakeBuild extends AxleBlockBuild {
		@Override public float getForce() {
			return Mathf.clamp(Mathf.map(getRotation(), beginBrakeSpeed, endBrakeSpeed, 0f, 1f)) * -brakeStrength;
		}
	}
}
