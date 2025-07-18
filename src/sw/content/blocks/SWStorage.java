package sw.content.blocks;

import arc.math.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.liquid.*;
import mindustry.world.blocks.storage.*;
import sw.content.*;
import sw.world.blocks.storage.*;

import static mindustry.type.ItemStack.*;

public class SWStorage {
	public static Block
		compactContainer,
		liquidBasin,
		coreScaffold, coreMole;

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
				SWItems.verdigris, 500,
				Items.graphite, 400
			));
			size = 3;
			health = 2000;
			alwaysUnlocked = true;
			unitType = SWUnitTypes.lambda;
			itemCapacity = 2000;
			unitCapModifier = 12;
		}};
		coreMole = new CogCore("core-mole") {{
			requirements(Category.effect, with(
				SWItems.verdigris, 500,
				SWItems.aluminium, 300,
				Items.graphite, 200
			));
			researchCost = with(
				SWItems.verdigris, 2000,
				SWItems.aluminium, 1000,
				Items.graphite, 1500
			);
			size = 3;
			health = 2500;
			unitType = SWUnitTypes.rho;
			itemCapacity = 4500;
			unitCapModifier = 16;
			
			landDuration = 600f;
			
			cogShowInterp = a -> Interp.swing.apply(Mathf.clamp((1f - a) * 2f));
			cogMaxOffset = 14f;
			
			cogRotateInterp = a -> Interp.swingOut.apply(Mathf.clamp(2f * (1f - a) - 1f));
			minCogRotations = 1f;
			maxCogRotations = 5f;
			
			fadeOutInterp = a -> Interp.swingOut.apply(Mathf.clamp(2f * (1f - a) - 1f));
		}};
	}
}
