package sw.content.blocks;

import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import sw.content.*;
import sw.world.blocks.power.*;

import static mindustry.type.ItemStack.*;

public class SWPower {
	public static Block gasPipe, gasPump;

	public static void load() {
		gasPipe = new GasPipe("gas-pipe") {{
			requirements(Category.power, with(
				SWItems.compound, 2
			));
			health = 40;
		}};
		gasPump = new GasPump("gas-pump") {{
			requirements(Category.power, with(
				SWItems.compound, 2,
				Items.silicon, 3
			));
			health = 80;
		}};
	}
}
