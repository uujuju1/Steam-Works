package sw.ai;

import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import sw.world.blocks.units.*;

public class CrafterAI extends AIController {
	public static float fillRange = 6f, moveRange = 5f, moveSmoothing = 20f;

	@Override
	public void updateMovement() {
		if (!(unit instanceof BuildingTetherc u) || !(u.building() instanceof CrafterUnitBlock.CrafterUnitBlockBuild build)) return;


		if (build.counter == 0) {
			Tmp.v1.set(build.start.x, build.start.y).scl(8f);
			moveTo(Tmp.v1, moveRange, moveSmoothing);
			build.counter = 1;
		} else {
			if (build.counter == 1) {
				moveTo(Tmp.v1, moveRange, moveSmoothing);
			} else {
				if (build.counter == 2) {
					Tmp.v1.set(build.end.x, build.end.y).scl(8f);
					moveTo(Tmp.v1, moveRange, moveSmoothing);
				} else {
					if (build.counter == 3) {
						moveTo(Tmp.v1, moveRange, moveSmoothing);
					}
				}
			}
		}
	}
}