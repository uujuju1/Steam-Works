package sw.content.blocks;

import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import sw.content.*;
import sw.world.blocks.power.*;
import sw.world.blocks.production.*;
import sw.world.meta.*;

import static mindustry.type.ItemStack.*;

public class SWPower {
	public static Block
		combustionTensionGenerator,

		lowWire, coatedWire,
		wireAdapter,

		lowRouter, coatedRouter,

		lowBridge, coatedBridge,

		wireJunction;

	public static void load() {
		combustionTensionGenerator = new SWGenericCrafter("combustion-tension-generator") {{
			requirements(Category.power, with(
				SWItems.compound, 5,
				SWItems.iron, 50,
				SWItems.nickel, 60,
				Items.silicon, 30
			));

			size = 2;
			health = 160;
			craftTime = 60;

			drawer = new DrawMulti(
				new DrawDefault(),
				new DrawRegion("-top") {{
					spinSprite = true;
					rotateSpeed = 3f;
				}}
			);

			consumeItem(Items.graphite, 1);

			outputTension = 10;
		}};

		//region tension
		lowWire = new TensionWire("low-tier-wire") {{
			requirements(Category.power, with(
				SWItems.compound, 3
			));
			tensionConfig = new TensionConfig() {{
				staticTension = 2.5f;
			}};
		}};
		coatedWire = new TensionWire("coated-tier-wire") {{
			requirements(Category.power, with(
				SWItems.chalk, 3
			));
			tensionConfig = new TensionConfig() {{
				staticTension = 2.5f;
				tier = 1;
			}};
		}};
		wireAdapter = new TensionWire("wire-adapter") {{
			requirements(Category.power, with(
				SWItems.compound, 5,
				Items.silicon, 5
			));
			tensionConfig = new TensionConfig() {{
				tier = -1;
				staticTension = 5;
			}};
		}};

		lowRouter = new TensionRouter("low-tier-wire-router") {{
			requirements(Category.power, with(
				SWItems.compound, 5
			));
			tensionConfig = new TensionConfig() {{
				staticTension = 5;
			}};
		}};
		coatedRouter = new TensionRouter("coated-tier-wire-router") {{
			requirements(Category.power, with(
				SWItems.chalk, 5
			));
			tensionConfig = new TensionConfig() {{
				staticTension = 5;
				tier = 1;
			}};
		}};

		lowBridge = new TensionBridge("low-tier-wire-bridge") {{
			requirements(Category.power, with(
				SWItems.compound, 6,
				Items.graphite, 3
			));
			tensionConfig = new TensionConfig() {{
				staticTension = 5;
			}};
		}};
		coatedBridge = new TensionBridge("coated-tier-wire-bridge") {{
			requirements(Category.power, with(
				SWItems.chalk, 6,
				Items.graphite, 3
			));
			tensionConfig = new TensionConfig() {{
				staticTension = 5;
				tier = 1;
			}};
		}};

		wireJunction = new TensionJunction("wire-junction") {{
			requirements(Category.power, with(
				SWItems.compound, 3,
				Items.silicon, 3
			));
		}};
		//endregion
	}
}
