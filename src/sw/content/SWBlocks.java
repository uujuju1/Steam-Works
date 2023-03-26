package sw.content;

import arc.graphics.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.entities.pattern.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.blocks.power.*;
import mindustry.world.blocks.production.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.blocks.units.*;
import mindustry.world.consumers.*;
import mindustry.world.draw.*;
import sw.world.blocks.defense.*;
import sw.world.blocks.heat.*;
import sw.world.blocks.production.*;
import sw.world.blocks.units.*;
import sw.world.consumers.*;
import sw.world.recipes.*;

import static mindustry.type.ItemStack.*;
import static mindustry.world.blocks.units.UnitFactory.*;

public class SWBlocks {
	public static Block

		heatPipe,
	  heatBridge,
		heatRadiator,

		burner,
		boiler, thermalBoiler,
		nickelForge,
    batchPress,
		rebuilder,

		stirlingGenerator,

		bolt, light, /*thunder,*/

    compoundWall, compoundWallLarge, denseWall, denseWallLarge,

    subFactory,
		crafterFactory,

		coreScaffold;

	public static void load() {
//		distribution
		heatPipe = new HeatPipe("heat-pipe") {{
			requirements(Category.power, with(Items.silicon, 1, Items.metaglass, 1, SWItems.nickel, 3));
			heatConfig().heatLoss = 0.1f;
		}};
		heatBridge = new HeatBridge("heat-bridge") {{
			requirements(Category.power, with(Items.silicon, 5, SWItems.nickel, 8));
			heatConfig().heatLoss = 0.1f;
		}};
		heatRadiator = new HeatRadiator("heat-radiator") {{
			requirements(Category.power, with(Items.silicon, 3, SWItems.nickel, 2, Items.graphite, 1));
			size = 2;
			heatConfig().maxHeat = 2000;
			heatConfig().heatLoss = 1.06f;
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
			updateEffect = Fx.coalSmeltsmoke;
			craftTime = 120f;
			outputHeat = 3f;
			heatConfig().acceptHeat = false;
		}};

		boiler = new HeatGenericCrafter("boiler") {{
			requirements(Category.production, with(SWItems.nickel, 40, Items.metaglass, 35, Items.titanium, 30));
			heatConfig().maxHeat = 2000f;
			heatConfig().outputHeat = false;
			size = 2;
			health = 160;
			hasLiquids = true;
			consumeLiquid(Liquids.water, 0.1f);
			consume(new ConsumeHeat(0.5f, 100f, false));
			updateEffect = Fx.smoke;
			outputLiquid = new LiquidStack(SWLiquids.steam, 0.15f);
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
			updateEffect = Fx.smoke;
      outputLiquid = new LiquidStack(SWLiquids.steam, 0.2f);
		}};

		nickelForge = new GenericCrafter("nickel-forge") {{
			requirements(Category.crafting, with(Items.copper, 40, Items.graphite, 25));
			size = 2;
			health = 160;
			craftTime = 30f;
			drawer = new DrawMulti(new DrawDefault(), new DrawFlame());
			craftEffect = SWFx.nickelCraft;
      updateEffect = Fx.smeltsmoke;
			consumeItems(with(Items.scrap, 2, Items.lead, 1));
			consumePower(1f);
			outputItems = with(SWItems.nickel, 1);
		}};
		batchPress = new StackCrafter("batch-press") {{
			requirements(Category.crafting, with(
				Items.titanium, 120,
				Items.silicon, 50,
				Items.graphite, 75,
				Items.plastanium, 140,
				SWItems.denseAlloy, 150
			));
			size = 3;
			health = 200;
			craftTime = 30f;
			stacks = 6;
			consumeItem(Items.coal, 1);
			consumeLiquid(SWLiquids.steam, 0.02f);
			craftEffect = SWFx.graphiteCraft;
			stackCraftEffect = SWFx.graphiteStackCraft;
			updateEffect = Fx.smoke;
      drawer = new DrawMulti(new DrawDefault(), new DrawFlame());
			outputItems = with(Items.graphite, 8);
		}};
		rebuilder = new MultiCrafter("rebuilder") {{
			requirements(Category.crafting, with(
				SWItems.nickel, 65,
				Items.titanium, 80,
				Items.copper, 60,
				Items.lead, 75
			));
			size = 3;
			health = 200;
			hasLiquids = true;
			recipes.add(
				new GenericRecipe() {{
					craftTime = 45f;
					consumeItems = with(SWItems.nickel, 1, Items.copper, 2);
					outputItems = with(SWItems.compound, 1);
					drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawBlurSpin("-rotator", 2) {{
						blurThresh = 12331;
					}}, new DrawDefault(), new DrawRegion("-cap"));
					craftEffect = SWFx.compoundCraft;
					updateEffect = 	Fx.smeltsmoke;
				}},
				new GenericRecipe() {{
					craftTime = 60f;
					consumeItems = with(SWItems.nickel, 2, Items.plastanium, 2);
					consumeLiquids = LiquidStack.with(SWLiquids.steam, 0.3f);
					outputItems = with(SWItems.denseAlloy, 1);
					drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidRegion(SWLiquids.steam), new DrawDefault(), new DrawRegion("-top"));
					updateEffect = Fx.smoke;
				}}
 			);
		}};

//		power
		stirlingGenerator = new ConsumeGenerator("stirling-generator") {{
      requirements(Category.power, with(
				SWItems.denseAlloy, 120,
	      Items.silicon, 140,
	      Items.titanium, 100
      ));
			size = 3;
			health = 160;
			powerProduction = 7f;
			hasLiquids = true;
			generateEffect = Fx.smoke;
			generateEffectRange = 12f;
			effectChance = 0.1f;
			consumeLiquid(SWLiquids.steam, 0.03f);
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawPistons() {{
				sides = 8;
				sideOffset = 10f;
				sinScl = -2.5f;
				sinMag = 2f;
			}}, new DrawDefault());
    }};

//		turrets
		bolt = new HeatTurret("bolt") {{
			requirements(Category.turret, with(
				Items.silicon, 120,
				Items.copper, 70,
				SWItems.compound, 95
			));
			consumer = new ConsumeHeatTrigger(100, 250, false);
			maxAmmo = 2;
			size = 2;
			health = 180 * 4;
			reload = 30f;
			range = 23f * 8f;
			shootSound = Sounds.blaster;
			shootType = new LaserBulletType(60) {{
				colors = new Color[]{Color.white, Color.lightGray};
			}};
		}};
		light = new HeatTurret("light") {{
			requirements(Category.turret, with(
							Items.titanium, 200,
							Items.silicon, 200,
							Items.graphite, 220,
							SWItems.compound, 210
			));
			consumer = new ConsumeHeatTrigger(200, 1000, true);
			size = 3;
			health = 200 * 9;
			reload = 60f;
			range = 180f;
			shootSound = Sounds.shotgun;
			shoot = new ShootSpread(3, 15);
			shootType = new ShrapnelBulletType() {{
				damage = 130;
				length = 180f;
				serrations = 9;
				fromColor = Color.white;
				toColor = Color.lightGray;
			}};
		}};

//    walls
    compoundWall = new Wall("compound-wall") {{
      requirements(Category.defense, with(SWItems.compound, 6));
      health = 120 * 4;
    }};
		compoundWallLarge = new Wall("compound-wall-large") {{
			requirements(Category.defense, mult(compoundWall.requirements, 4));
			size = 2;
			health = 120 * 4 * 4;
		}};
		denseWall = new HeatableWall("dense-wall") {{
			requirements(Category.defense, with(SWItems.denseAlloy, 6));
			health = 200 * 4;
			heatConfig().heatLoss = 0f;
			absorbLasers = true;
		}};
		denseWallLarge = new HeatableWall("dense-wall-large") {{
			requirements(Category.defense, mult(denseWall.requirements, 4));
			size = 2;
			health = 200 * 4 * 4;
			heatConfig().heatLoss = 0f;
			absorbLasers = true;
		}};

//		units
		subFactory = new UnitFactory("submarine-factory") {{
			requirements(Category.units, with(
							SWItems.compound, 120,
							Items.lead, 140,
							Items.graphite, 100
			));
			size = 3;
			health = 160;
			consumePower(1.5f);
			plans.add(new UnitPlan(SWUnitTypes.recluse, 60f * 50f, with(Items.silicon, 15, Items.metaglass, 25, SWItems.compound, 20)));
		}};
		crafterFactory = new SingleUnitFactory("crafter-factory") {{
			requirements(Category.units, with(
							SWItems.compound, 130,
							Items.titanium, 120,
							Items.silicon, 150
			));
			size = 2;
			health = 160;
			consumePower(2f);
			itemCapacity = 40;
			unitPlan = new UnitPlan(SWUnitTypes.bakler, 60f * 30f, with(Items.silicon, 20, Items.titanium, 15, SWItems.compound, 10));
			consumeItems(unitPlan.requirements);
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
