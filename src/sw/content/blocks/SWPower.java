package sw.content.blocks;

import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import sw.content.*;
import sw.util.*;
import sw.world.blocks.power.*;
import sw.world.blocks.production.*;
import sw.world.meta.*;

import static mindustry.type.ItemStack.*;

public class SWPower {
	public static Block
		boiler,
		wireShaft, wireShaftRouter;

	public static void load() {
		wireShaft = new WireShaft("wire-shaft") {{
			requirements(Category.power, with(
				SWItems.nickel, 5,
				SWItems.iron, 5
			));
		}};
		wireShaftRouter = new WireShaft("wire-shaft-router") {{
			requirements(Category.power, with(
				SWItems.nickel, 10,
				SWItems.iron, 5
			));
			rotate = false;
		}};

		boiler = new SWGenericCrafter("boiler") {{
			requirements(Category.power, with(
				SWItems.iron, 20,
				SWItems.compound, 40,
				Items.silicon, 35
			));
			size = 2;
			health = 160;

			spinConfig = new SpinConfig() {{
				connections = BlockGeometry.half2;
			}};

			drawer = new DrawMulti(
				new DrawDefault(),
				new DrawRegion("-rotator") {{
					spinSprite = true;
					rotateSpeed = 2f;
				}}
			);

			updateEffectStatic = SWFx.burnElevation;

			consumeItem(Items.graphite, 1);
			consumeLiquid(SWLiquids.solvent, 0.1f);
			craftTime = 30;

			outputRotation = 0.2f;
		}};
	}
}
