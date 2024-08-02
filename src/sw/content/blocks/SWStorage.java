package sw.content.blocks;

import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.storage.*;
import sw.content.*;

import static mindustry.type.ItemStack.*;

public class SWStorage {
	public static Block
		compactContainer,
		coreScaffold;

	public static void load() {
		compactContainer = new StorageBlock("compact-container") {{
			requirements(Category.effect, with(
				SWItems.nickel, 40,
				SWItems.iron, 50,
				Items.silicon, 30
			));
			size = 2;
			health = 500;
			itemCapacity = 500;
			coreMerge = false;
		}};

		coreScaffold = new CoreBlock("core-scaffold") {{
			requirements(Category.effect, with(
				SWItems.nickel, 1000,
				Items.graphite, 800
			));
			size = 3;
			health = 2000;
			alwaysUnlocked = true;
			unitType = SWUnitTypes.lambda;
			itemCapacity = 1000;
			unitCapModifier = 12;
		}};
	}
}
