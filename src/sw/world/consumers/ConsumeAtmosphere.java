package sw.world.consumers;

import mindustry.content.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.consumers.*;
import sw.*;
import sw.world.*;

public class ConsumeAtmosphere extends Consume {
	public Liquid fluid = Liquids.water;
	public float efficiencyScl = 1f;

	public ConsumeAtmosphere(Liquid fluid, float efficiencyScl) {
		this.fluid = fluid;
		this.efficiencyScl = efficiencyScl;
	}
	public ConsumeAtmosphere(Liquid fluid) {
		this(fluid, 1);
	}
	public ConsumeAtmosphere() {

	}

	public FluidArea get(Building build) {
		FluidArea fluidArea = SWVars.fluidAreas.copy().filter(area -> area.fluid == fluid).max(area -> area.get(build.x, build.y));
		if (fluidArea == null) fluidArea = new FluidArea(-1200, -1200, 0, Liquids.water);
		return fluidArea;
	}

	@Override
	public float efficiency(Building build) {
		return (get(build).dst(build.x, build.y) < get(build).radius * 8f ? get(build).get(build.x, build.y) : 0) * efficiencyScl;
	}

	@Override
	public float efficiencyMultiplier(Building build) {
		return (get(build).dst(build.x, build.y) < get(build).radius * 8f ? get(build).get(build.x, build.y) : 0) * efficiencyScl;
	}
}
