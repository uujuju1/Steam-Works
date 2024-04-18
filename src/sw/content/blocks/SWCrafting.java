package sw.content.blocks;

import arc.graphics.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;
import mindustry.world.draw.*;
import sw.content.*;
import sw.world.blocks.production.*;

import static mindustry.type.ItemStack.*;

public class SWCrafting {
	public static Block
		siliconBoiler,
		compoundSmelter, thermiteMixer,
		densePress;

	public static void load() {
		siliconBoiler = new GenericCrafter("silicon-boiler") {{
			requirements(Category.crafting, with(
				SWItems.nickel, 150,
				SWItems.iron, 120,
				Items.graphite, 80
			));
			size = 3;
			health = 240;

			craftTime = 180f;

			consumeItems(with(Items.graphite, 3, Items.sand, 3));
			outputItems = with(Items.silicon, 3);

			drawer = new DrawMulti(
				new DrawDefault(),
				new DrawFlame() {{
					flameRadius = 5f;
				}}
			);
		}};

		compoundSmelter = new GenericCrafter("compound-smelter") {{
			requirements(Category.crafting, with(
				SWItems.iron, 80,
				SWItems.nickel, 200,
				Items.silicon, 150,
				Items.graphite, 160
			));
			size = 3;
			health = 200;
			craftTime = 30f;

			drawer = new DrawMulti(
				new DrawDefault(),
				new DrawFlame(Color.valueOf("BEB5B2"))
			);

			consumeItems(with(
				Items.silicon, 1,
				SWItems.nickel, 2
			));
			consumeLiquid(SWLiquids.solvent, 0.1f);

			outputItem = new ItemStack(SWItems.compound, 1);
		}};

		densePress = new GenericCrafter("dense-press") {{
			requirements(Category.crafting, with(
				SWItems.iron, 160,
				SWItems.nickel, 200,
				Items.silicon, 80,
				Items.graphite, 160
			));
			size = 3;
			health = 200;
			craftTime = 60f;
			craftEffect = SWFx.denseAlloyCraft;

			consumeItems(with(
				Items.silicon, 2,
				SWItems.iron, 2
			));
			consumeLiquid(SWLiquids.solvent, 0.1f);

			outputItem = new ItemStack(SWItems.denseAlloy, 2);
		}};
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
