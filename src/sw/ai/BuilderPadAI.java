package sw.ai;

import arc.struct.*;
import mindustry.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import sw.world.blocks.units.*;

public class BuilderPadAI extends AIController {
	public BuildPlan plan;
	public BuilderPad.BuilderPadBuild tether;

	public static Seq<BuildPlan> tempPlans = new Seq<>();

	@Override
	public void updateMovement() {
		if (unit instanceof BuildingTetherc tetherUnit) tether = (BuilderPad.BuilderPadBuild) tetherUnit.building();

		Player closest = null;
		float minDst = Float.MAX_VALUE;
		for (Player player : Groups.player) {
			if(!player.dead() && player.isBuilder() && player.team() == unit.team && player.unit().updateBuilding){
				float dst = player.dst2(unit);
				if(dst < minDst){
					closest = player;
					minDst = dst;
				}
			}
		}

		unit.plans.clear();
		if (!Vars.net.client() && tether != null && closest != null && closest.unit() != null) {
			tempPlans.set(closest.unit().plans.toArray(BuildPlan.class));

			Player finalClosest = closest;
			plan = tempPlans.min(f -> tether.dst(f) + (finalClosest.unit().shouldSkip(f, finalClosest.core()) ? 1000000 * 8 : 0));

			if (plan != null && finalClosest.unit().updateBuilding && tether.efficiency > 0f) {
				unit.plans.add(plan);
				moveTo(plan, unit.type.buildRange * 0.9f, 50f);
				unit.lookAt(plan);
			} else {
				moveTo(tether, 1, 50f);
				if (unit.dst(tether) < 2f) unit.lookAt(90f);
			}
		} else if (tether != null) {
			moveTo(tether, 1, 50f);
			if (unit.dst(tether) < 2f) unit.lookAt(90f);
		}
	}
}
