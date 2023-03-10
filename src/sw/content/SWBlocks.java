package sw.content;

import arc.graphics.Color;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.content.UnitTypes;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.ShrapnelBulletType;
import mindustry.gen.Sounds;
import mindustry.type.Category;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import mindustry.world.blocks.defense.turrets.PowerTurret;
import mindustry.world.blocks.power.ConsumeGenerator;
import mindustry.world.blocks.production.AttributeCrafter;
import mindustry.world.blocks.production.Drill;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.blocks.units.UnitFactory;
import mindustry.world.consumers.ConsumeItemFlammable;
import mindustry.world.draw.*;
import sw.world.blocks.heat.HeatBridge;
import sw.world.blocks.heat.HeatGenericCrafter;
import sw.world.blocks.heat.HeatPipe;
import sw.world.blocks.heat.HeatRadiator;
import sw.world.blocks.production.MultiCrafter;
import sw.world.blocks.production.StackCrafter;
import sw.world.blocks.units.SingleUnitFactory;
import sw.world.consumers.ConsumeHeat;
import sw.world.recipes.GenericRecipe;

import static mindustry.type.ItemStack.with;
import static mindustry.world.blocks.units.UnitFactory.*;

public class SWBlocks {
	public static Block

		heatPipe,
	  heatBridge,
		heatRadiator,

		hydraulicDrill,

		burner,
		boiler, thermalBoiler,
		nickelForge,
    batchPress,
		rebuilder,

		stirlingGenerator,

		bolt, light, /*thunder,*/

    subFactory,
		crafterFactory,

		coreScaffold;

	public static void load() {
//		distribution
		heatPipe = new HeatPipe("heat-pipe") {{
			requirements(Category.power, with(Items.silicon, 1, Items.metaglass, 1, SWItems.nickel, 3));
		}};
		heatBridge = new HeatBridge("heat-bridge") {{
			requirements(Category.power, with(Items.silicon, 5, SWItems.nickel, 8));
		}};
		heatRadiator = new HeatRadiator("heat-radiator") {{
			requirements(Category.power, with(Items.silicon, 3, SWItems.nickel, 2, Items.graphite, 1));
			minHeatLoss = 280f;
		}};

//		production
		hydraulicDrill = new Drill("hydraulic-drill") {{
			requirements(Category.production, with(Items.silicon, 12, SWItems.nickel, 6));
			size = 2;
			health = 160;
			tier = 3;
			drillTime = 300f;
			consumeLiquid(SWLiquids.steam, 0.02f);
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
			updateEffect = Fx.coalSmeltsmoke;
			craftTime = 120f;
			outputHeat = 400f;
			heatConfig().acceptHeat = false;
		}};

		boiler = new HeatGenericCrafter("boiler") {{
			requirements(Category.production, with(SWItems.nickel, 40, Items.metaglass, 35, Items.titanium, 30));
			size = 2;
			health = 160;
			hasLiquids = true;
			heatConfig().maxHeat = 300f;
			heatConfig().outputHeat = false;
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
			requirements(Category.crafting, with(Items.silicon, 40, Items.graphite, 25));
			size = 2;
			health = 160;
			craftTime = 30f;
			drawer = new DrawMulti(new DrawDefault(), new DrawFlame());
      updateEffect = Fx.smeltsmoke;
			consumeItems(with(Items.scrap, 2, Items.lead, 1));
			consumePower(1f);
			outputItems = with(SWItems.nickel, 1);
		}};
		batchPress = new StackCrafter("batch-press") {{
			requirements(Category.crafting, with(Items.titanium, 120, Items.silicon, 50, Items.graphite, 75, SWItems.nickel, 150));
			size = 3;
			health = 200;
			craftTime = 30f;
			stacks = 6;
			consumeItem(Items.coal, 1);
			consumeLiquid(SWLiquids.steam, 0.02f);
			craftEffect = SWFx.graphiteCraft;
			updateEffect = Fx.smoke;
      drawer = new DrawMulti(new DrawDefault(), new DrawFlame());
			outputItems = with(Items.graphite, 8);
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
					consumeItems = with(SWItems.nickel, 2, Items.graphite, 2);
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
		bolt = new PowerTurret("bolt") {{
			requirements(Category.turret, with(
							Items.silicon, 120,
							Items.copper, 70,
							SWItems.compound, 95
			));
			size = 2;
			health = 180 * 4;
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
		light = new PowerTurret("light") {{
			requirements(Category.turret, with(
							Items.titanium, 200,
							Items.silicon, 200,
							Items.graphite, 220,
							SWItems.compound, 210
			));
			size = 3;
			health = 200 * 9;
			reload = 60f;
			range = 180f;
			consumePower(6f);
			shootSound = Sounds.shotgun;
			shootType = new ShrapnelBulletType() {{
				damage = 130;
				length = 180f;
				serrations = 9;
				fromColor = Color.white;
				toColor = Color.lightGray;
			}};
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
