package sw.content.blocks;

import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.*;
import sw.content.*;

import static mindustry.type.ItemStack.*;

public class SWDefense {
	public static Block
		grindLamp, lavaLamp,
		ironWall, ironWallLarge;

	public static void load() {
		// region lamps
		// endregion
		// region walls
		ironWall = new Wall("iron-wall") {{
			requirements(Category.defense, with(SWItems.iron, 6));
			health = 100 * 4;
			absorbLasers = true;
		}};
		ironWallLarge = new Wall("iron-wall-large") {{
			requirements(Category.defense, mult(ironWall.requirements, 4));
			size = 2;
			health = 100 * 4 * 4;
			absorbLasers = true;
		}};
		// endregion
	}
}
