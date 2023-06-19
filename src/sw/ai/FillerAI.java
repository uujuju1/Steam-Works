package sw.ai;

import arc.util.*;
import mindustry.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import sw.world.blocks.environment.Filler.*;

public class FillerAI extends AIController {
	public static float fillRange = 6f, moveRange = 5f, moveSmoothing = 20f;

	@Override
	public void updateMovement() {
		if (!(unit instanceof BuildingTetherc u) || !(u.building() instanceof FillerBuild build)) return;

		Tmp.v1.set(build.targetX, build.targetY);
		if (build.targets().isEmpty() || !build.canConsume()) {
			moveTo(build, moveRange, moveSmoothing);
		} else {
			moveTo(Tmp.v1, moveRange, moveSmoothing);
		}
		if (u.dst(Tmp.v1) <= fillRange) build.swap(Vars.world.tileWorld(build.targetX, build.targetY));
	}
}
