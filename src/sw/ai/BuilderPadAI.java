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

		if (!Vars.net.client() && tether != null) {
			tempPlans.set(Vars.player.unit().plans.toArray(BuildPlan.class));

			plan = tempPlans.min(f -> tether.dst(f));

			unit.plans.clear();
			if (plan != null && Vars.control.input.isBuilding) {
				unit.plans.add(plan);
				moveTo(plan, unit.type.buildRange * 0.5f);
			} else moveTo(tether, 1);
		}
	}
}
