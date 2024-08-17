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
		gasPipe, gasPump, gasJunction,
	boiler;

	public static void load() {
		gasPipe = new GasPipe("gas-pipe") {{
			requirements(Category.power, with(
				SWItems.compound, 2
			));
			health = 40;
		}};
		gasJunction = new GasJunction("gas-junction") {{
			requirements(Category.power, with(
				SWItems.compound, 5
			));
			health = 40;
		}};
		((GasPipe) gasPipe).junctionReplacement = gasJunction;
		gasPump = new GasPump("gas-pump") {{
			requirements(Category.power, with(
				SWItems.compound, 2,
				Items.silicon, 3
			));
			health = 80;
		}};

		boiler = new SWGenericCrafter("boiler") {{
			requirements(Category.power, with(
				SWItems.iron, 20,
				SWItems.compound, 40,
				Items.silicon, 35
			));
			size = 2;
			health = 160;

			gasConfig = new GasConfig() {{
				overpressureDamage = 0.2f;
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

			outputGas = 4/60f;
			outputGasContinuous = true;
		}};
	}
}
