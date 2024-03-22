package sw.content.blocks;

import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import sw.world.blocks.production.*;

import static mindustry.type.ItemStack.*;

public class SWPower {
	public static Block combustionTensionGenerator;

	public static void load() {
		combustionTensionGenerator = new SWGenericCrafter("combustion-tension-generator") {{
			requirements(Category.power, with());

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
