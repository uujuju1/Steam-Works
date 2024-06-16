package sw.dream;

import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.blocks.storage.*;

public class DreamContent {
	public static UnitType brutalistUnit;
	public static Block concrete, brutalistConcrete, brutalistCore, lore;

	public static void load() {
		brutalistUnit = new UnitType("brutalist-unit") {{
			constructor = UnitEntity::create;
			drawCell = false;
			speed = 0.55f;
			lightRadius = 20f;
		}};

		concrete = new Floor("concrete", 0);
		brutalistConcrete = new StaticWall("brutalist-concrete");

		brutalistCore = new CoreBlock("brutalist-core") {{
			unitType = brutalistUnit;
			size = 2;
			drawTeamOverlay = false;
		}};
	}
}
