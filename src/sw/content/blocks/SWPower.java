package sw.content.blocks;

import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import sw.content.*;
import sw.world.blocks.production.*;

import static mindustry.type.ItemStack.*;

public class SWPower {
	public static Block combustionTensionGenerator;

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
	}
}
