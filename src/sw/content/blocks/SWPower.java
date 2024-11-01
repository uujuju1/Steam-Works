package sw.content.blocks;

import arc.struct.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import sw.content.*;
import sw.math.*;
import sw.world.blocks.power.*;
import sw.world.blocks.production.*;
import sw.world.meta.*;

import static mindustry.type.ItemStack.*;

public class SWPower {
	public static Block
		boiler,
		wireShaft, wireShaftRouter, wireShaftJunction, shaftTransmission;

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
		wireShaftJunction = new ShaftJunction("wire-shaft-junction") {{
			requirements(Category.power, with(
				SWItems.nickel, 7,
				SWItems.iron, 7
			));
		}};

		shaftTransmission = new ShaftTransmission("shaft-transmission") {{
			requirements(Category.power, with(
				SWItems.nickel, 50,
				SWItems.iron, 40,
				Items.silicon, 30
			));
			size = 2;

			spinConfig = new SpinConfig() {{
				connections = new Seq[]{
					BlockGeometry.sides21,
					BlockGeometry.sides22,
					BlockGeometry.sides23,
					BlockGeometry.sides24
				};
			}};
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
				connections = new Seq[]{
					BlockGeometry.half2,
					BlockGeometry.half2,
					BlockGeometry.half2,
					BlockGeometry.half2
				};
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

			outputRotation = 5f;
		}};
	}
}
