package sw.content;

import arc.graphics.Color;
import mindustry.content.*;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.gen.Sounds;
import mindustry.world.blocks.defense.turrets.PowerTurret;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.production.AttributeCrafter;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.type.Category;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import mindustry.world.draw.*;
import sw.world.recipes.GenericRecipe;
import sw.world.blocks.production.MultiCrafter;

import static mindustry.type.ItemStack.*;

public class SWBlocks {
	public static Block
		oreNickel,

		boiler, thermalBoiler,
		hydraulicCrafter,

		bolt,

		coreScaffold;

	public static void load() {
		oreNickel = new OreBlock(SWItems.nickel);

//		crafting
		boiler = new GenericCrafter("boiler") {{
			requirements(Category.crafting, with(
				SWItems.nickel, 45,
							Items.graphite, 50,
							Items.metaglass, 30
			));
			size = 2;
			health = 160;
			consumeLiquid(Liquids.water, 0.1f);
			outputLiquid = new LiquidStack(SWLiquids.steam, 0.15f);
		}};
		thermalBoiler = new AttributeCrafter("thermal-boiler") {{
			requirements(Category.crafting, with(
							SWItems.compound, 40,
							Items.metaglass, 35,
							Items.titanium, 30
			));
			size = 2;
			health = 160;
			consumeLiquid(Liquids.water, 0.1f);
      outputLiquid = new LiquidStack(SWLiquids.steam, 0.2f);
		}};

		hydraulicCrafter = new MultiCrafter("hydraulic-crafter") {{
			requirements(Category.crafting, with(
				SWItems.nickel, 65,
				Items.graphite, 80,
				Items.copper, 60,
				Items.lead, 75
			));
			size = 3;
			health = 200;
			recipes.add(
				new GenericRecipe() {{
					craftTime = 45f;
					consumeItems = with(SWItems.nickel, 1, Items.copper, 2);
					outputItems = with(SWItems.compound, 1);
					drawer = new DrawMulti(
						new DrawDefault(),
						new DrawFlame()
					);
					craftEffect = SWFx.compoundCraft;
					updateEffect = 	Fx.smeltsmoke;
				}},
				new GenericRecipe() {{
					craftTime = 60f;
					consumeItems = with(SWItems.nickel, 2, Items.graphite, 2);
					consumeLiquids = LiquidStack.with(SWLiquids.steam, 0.3f);
					outputItems = with(SWItems.denseAlloy, 1);
					drawer = new DrawMulti(
						new DrawDefault(),
						new DrawRegion("-cap")
					);
					craftEffect = SWFx.denseCraft;
					updateEffect = Fx.smoke;
				}}
 			);
		}};
//		turrets
		bolt = new PowerTurret("bolt") {{
			requirements(Category.turret, with(
							Items.silicon, 120,
							Items.copper, 70,
							SWItems.nickel, 95
			));
			size = 2;
			health = 200;
			reload = 30f;
			range = 120f;
			consumePower(4f);
			shootSound = Sounds.blaster;
			shootType = new BasicBulletType(2.5f, 20, "circle-bullet") {{
				lifetime = 48;
				frontColor = Color.white;
				backColor = Color.lightGray;
			}};
		}};
//		storage
		coreScaffold = new CoreBlock("core-scaffold") {{
			requirements(Category.effect, with(
							Items.graphite, 1000,
							Items.lead, 1500,
							Items.silicon, 1200
			));
			size = 3;
			health = 2000;
			alwaysUnlocked = true;
			unitType = UnitTypes.nova;
			itemCapacity = 5000;
			unitCapModifier = 12;
		}};
	}
}
