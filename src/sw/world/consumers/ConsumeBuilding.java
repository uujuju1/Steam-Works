package sw.world.consumers;

import arc.func.*;
import mindustry.gen.*;
import mindustry.world.consumers.*;

public class ConsumeBuilding extends Consume {
	public Boolf<Building> buildReq;
	
	public ConsumeBuilding(Boolf<Building> buildReq) {
		this.buildReq = buildReq;
	}
	
	@Override public float efficiency(Building build) {
		return buildReq.get(build) ? 1 : 0;
	}
	@Override public float efficiencyMultiplier(Building build) {
		return buildReq.get(build) ? 1 : 0;
	}
}
