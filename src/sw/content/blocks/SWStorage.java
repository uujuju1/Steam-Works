package sw.content.blocks;

import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.liquid.*;
import mindustry.world.blocks.storage.*;
import sw.content.*;

import static mindustry.type.ItemStack.*;

public class SWStorage {
	public static Block
		compactContainer,
		liquidBasin,
		coreScaffold;

	public static void load() {
		compactContainer = new StorageBlock("compact-container") {{
			requirements(Category.effect, with(
				SWItems.verdigris, 40,
				SWItems.iron, 50,
				Items.silicon, 30
			));
			size = 2;
			health = 500;
			itemCapacity = 500;
			coreMerge = false;
		}};
		
		liquidBasin = new LiquidRouter("liquid-basin") {{
			requirements(Category.liquid, with(
				SWItems.iron, 50,
				SWItems.verdigris, 30
			));
			size = 3;
			health = 500;
			solid = true;
			liquidCapacity = 1000;
		}};

		coreScaffold = new CoreBlock("core-scaffold") {{
			requirements(Category.effect, with(
				SWItems.verdigris, 5000,
				Items.graphite, 400
			));
			size = 3;
			health = 2000;
			alwaysUnlocked = true;
			unitType = SWUnitTypes.lambda;
			itemCapacity = 2000;
			unitCapModifier = 12;
		}};
	}
}
