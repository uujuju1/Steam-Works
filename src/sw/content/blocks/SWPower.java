package sw.content.blocks;

import mindustry.type.*;
import mindustry.world.*;
import sw.world.blocks.power.*;

import static mindustry.type.ItemStack.*;

public class SWPower {
	public static Block gasPipe;

	public static void load() {
		gasPipe = new GasPipe("gas-pipe") {{
			requirements(Category.power, with());
		}};
	}
}
