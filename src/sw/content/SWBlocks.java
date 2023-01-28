package sw.content;

import arc.graphics.Color;
import mindustry.content.*;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.gen.Sounds;
import mindustry.world.blocks.defense.turrets.PowerTurret;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.production.AttributeCrafter;
import mindustry.world.blocks.production.Drill;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.type.Category;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import mindustry.world.consumers.ConsumeItemFlammable;
import mindustry.world.draw.*;
import sw.world.blocks.heat.HeatBridge;
import sw.world.blocks.heat.HeatGenericCrafter;
import sw.world.blocks.heat.HeatPipe;
import sw.world.blocks.heat.HeatRadiator;
import sw.world.consumers.ConsumeHeat;
import sw.world.recipes.GenericRecipe;
import sw.world.blocks.production.MultiCrafter;

import static mindustry.type.ItemStack.*;

public class SWBlocks {
	public static Block
		oreNickel,

		heatPipe,
	  heatBridge,
		heatRadiator,

		hydraulicDrill,

		burner,
		boiler, thermalBoiler,
		nickelForge,
		rebuilder,

		bolt,

		coreScaffold;

	public static void load() {
		oreNickel = new OreBlock(SWItems.nickel);

//		distribution
		heatPipe = new HeatPipe("heat-pipe") {{
			requirements(Category.power, with(Items.silicon, 1, Items.metaglass, 1, SWItems.nickel, 3));
			maxHeat = 500f;
		}};
		heatBridge = new HeatBridge("heat-bridge") {{
			requirements(Category.power, with(Items.silicon, 5, SWItems.nickel, 8));
			maxHeat = 500f;
		}};
		heatRadiator = new HeatRadiator("heat-radiator") {{
			requirements(Category.power, with(Items.silicon, 3, SWItems.nickel, 2, Items.graphite, 1));
			maxHeat = 500f;
			minHeatLoss = 280f;
		}};

//		production
		hydraulicDrill = new Drill("hydraulic-drill") {{
			requirements(Category.production, with(Items.silicon, 12, SWItems.nickel, 6));
			size = 2;
			health = 160;
			tier = 3;
			drillTime = 300f;
			consumeLiquid(SWLiquids.steam, 0.2f);
			consumeLiquid(Liquids.water, 0.05f).boost();
		}};

//		crafting
		burner = new HeatGenericCrafter("burner") {{
			requirements(Category.power, with(Items.silicon, 20, Items.graphite, 30, Items.lead, 25));
			size = 2;
			health = 160;
			consume(new ConsumeItemFlammable());
			drawer = new DrawMulti(new DrawDefault(), new DrawFlame() {{
				flameRadius = 1.5f;
				flameRadiusIn = 0.75f;
				flameRadiusMag = 1f;
			}});
			craftTime = 120f;
			outputHeat = 250f;
			acceptsHeat = false;
			maxHeat = 500f;
		}};

		boiler = new HeatGenericCrafter("boiler") {{
			requirements(Category.production, with(SWItems.denseAlloy, 40, Items.metaglass, 35, Items.titanium, 30));
			size = 2;
			health = 160;
			hasLiquids = true;
			outputsHeat = false;
			consumeLiquid(Liquids.water, 0.1f);
			consume(new ConsumeHeat(0.5f, 100f, false));
			outputLiquid = new LiquidStack(SWLiquids.steam, 0.15f);
			maxHeat = 300f;
		}};
		thermalBoiler = new AttributeCrafter("thermal-boiler") {{
			requirements(Category.production, with(SWItems.compound, 40, Items.metaglass, 35, Items.titanium, 30));
			size = 2;
			health = 160;
			baseEfficiency = 0f;
			minEfficiency = 0.25f;
			maxBoost = 1.5f;
			boostScale = 0.5f;
			consumeLiquid(Liquids.water, 0.1f);
      outputLiquid = new LiquidStack(SWLiquids.steam, 0.2f);
		}};

		nickelForge = new GenericCrafter("nickel-forge") {{
			requirements(Category.crafting, with(Items.silicon, 40, Items.graphite, 25));
			size = 2;
			health = 160;
			craftTime = 30f;
			drawer = new DrawMulti(new DrawDefault(), new DrawFlame());
			consumeItems(with(Items.scrap, 2, Items.lead, 1));
			consumePower(1f);
			outputItems = with(SWItems.nickel, 1);
		}};
		rebuilder = new MultiCrafter("rebuilder") {{
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
							SWItems.compound, 95
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
