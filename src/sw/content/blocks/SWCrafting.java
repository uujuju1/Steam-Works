package sw.content.blocks;

import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import sw.content.*;
import sw.world.blocks.production.*;

import static mindustry.type.ItemStack.*;

public class SWCrafting {
	public static Block thermiteMixer;

	public static void load() {
		thermiteMixer = new SWGenericCrafter("thermite-mixer") {{
			requirements(Category.crafting, with());

			size = 2;
			health = 160;

			consumeItem(SWItems.denseAlloy, 1);
			consumeLiquid(SWLiquids.steam, 0.1f);
			consumeTension(10, 10);
			consumeTension(0, 20).staticTension();

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawRegion("-rotator") {{
					spinSprite = true;
					rotateSpeed = 3f;
				}},
				new DrawDefault()
			);

			outputItems = with(SWItems.thermite, 1);
		}};
	}
}
