package sw.content;

import arc.graphics.*;
import arc.math.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.entities.part.*;
import mindustry.entities.pattern.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.blocks.production.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.blocks.units.*;
import mindustry.world.consumers.*;
import mindustry.world.draw.*;
import sw.world.blocks.distribution.*;
import sw.world.blocks.environment.*;
import sw.world.blocks.force.*;
import sw.world.blocks.heat.*;
import sw.world.blocks.production.*;
import sw.world.blocks.sandbox.*;
import sw.world.blocks.units.*;
import sw.world.consumers.*;
import sw.world.draw.*;
import sw.world.meta.*;
import sw.world.recipes.*;

import static arc.struct.ObjectMap.*;
import static mindustry.type.ItemStack.*;
import static mindustry.world.blocks.units.UnitFactory.*;

public class SWBlocks {
	public static Block

		excavator,

		beltNode, beltNodeLarge,
		torquePump,
		electricSpinner, turbineSwing, waterWheel,
		compoundMixer, frictionHeater,

		heatPipe, heatBridge, heatRadiator,
		burner, resistance,
		boiler, thermalBoiler,

		resistantConveyor,

		nickelForge, oilDistiller,
    batchPress,
		rebuilder,

		stirlingGenerator,

		bolt, light,
		mortar, incend,

    compoundWall, compoundWallLarge, denseWall, denseWallLarge,

    subFactory,
		crafterFactory, structuraFactory,

		coreScaffold,
		filler,

		allSource;

	public static void load() {
		excavator = new ForceDrill("excavator") {{
			requirements(Category.production, with(
				SWItems.compound, 45,
				Items.graphite, 35,
				Items.silicon, 25,
				Items.titanium, 30)
			);
			size = 3;
			health = 200;
			drillTime = 320f;
			rotatorOffset = 4f;
			tier = 4;
			updateEffect = Fx.pulverizeMedium;
			drillEffect = Fx.mineBig;
			consume(new ConsumeSpeed(1f, 8f));
		}};

//		distribution
		heatPipe = new HeatPipe("heat-pipe") {{
			requirements(Category.power, with(Items.silicon, 1, Items.metaglass, 1, SWItems.denseAlloy, 3));
			heatConfig.heatLoss = 0.01f;
		}};
		heatBridge = new HeatBridge("heat-bridge") {{
			requirements(Category.power, with(Items.silicon, 5, SWItems.denseAlloy, 8));
			heatConfig.heatLoss = 0.01f;
		}};
		heatRadiator = new HeatRadiator("heat-radiator") {{
			requirements(Category.power, with(Items.silicon, 3, SWItems.denseAlloy, 2, Items.graphite, 1));
			size = 2;
			heatConfig = new HeatConfig() {{
				maxHeat = 400f;
				heatLoss = 0.01f;
			}};
		}};

		beltNode = new ForceNode("belt-node") {{
			requirements(Category.power, with(
				Items.silicon, 20,
				SWItems.compound, 20
			));
			health = 120;
			forceConfig = new ForceConfig() {{
				friction = 0.03f;
				maxForce = 12f;
				range = 60f;
			}};
		}};
		beltNodeLarge = new ForceNode("belt-node-large") {{
			requirements(Category.power, with(Items.silicon, 60, Items.titanium, 40, SWItems.compound, 80));
			size = 2;
			health = 120;
			forceConfig = new ForceConfig() {{
				friction = 0.06f;
				maxForce = 12f;
				range = 90f;
				beltSizeIn = beltSizeOut = 4f;
			}};
		}};

		resistantConveyor = new MechanicalConveyor("resistant-conveyor") {{
			requirements(Category.distribution, with(
				SWItems.nickel, 2,
				Items.graphite, 2
			));
			health = 125;
			speed = 0.08f;
			displayedSpeed = 11f;
		}};

//		crafting
		torquePump = new ForcePump("torque-pump") {{
			requirements(Category.liquid, with(
				Items.metaglass, 40,
				Items.silicon, 50,
				Items.lead, 30,
				SWItems.compound, 40
			));
			size = 2;
			health = 160;
			pumpAmount = 0.375f;
			consume(new ConsumeSpeed(3, 9));
			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawPumpLiquid(),
				new DrawDefault()
			);
			forceConfig = new ForceConfig() {{
				maxForce = 18f;
				beltSizeIn = beltSizeOut = 4f;
				outputsForce = false;
			}};
		}};

		electricSpinner = new SWGenericCrafter("electric-spinner") {{
			requirements(Category.power, with(
				Items.silicon, 50,
				Items.graphite, 80,
				SWItems.compound, 60
			));
			size = 2;
			health = 160;
			consumePower(1f);
			hasHeat = false;
			craftTime = 1f;
			forceConfig = new ForceConfig() {{
				maxForce = 5f;
				friction = 0.06f;
				beltSizeOut = beltSizeIn = 4f;
				acceptsForce = false;
			}};
			outputSpeed = 5f;
		}};
		waterWheel = new SWGenericCrafter("water-wheel") {{
			requirements(Category.power, with(
				Items.silicon, 80,
				Items.lead, 120,
				Items.metaglass, 70,
				SWItems.compound, 100
			));
			size = 3;
			health = 200;
			consumeLiquid(Liquids.water, 0.2f);
			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawLiquidTile(Liquids.water, 1f),
				new DrawRegion("-wheel") {{
					spinSprite = true;
					rotateSpeed = -1f;
				}},
				new DrawRegion("-top")
			);
			hasHeat = false;
			hasLiquids = true;
			craftTime = 1f;
			forceConfig = new ForceConfig() {{
				maxForce = 3f;
				friction = 0.09f;
				beltSizeOut = beltSizeIn = 6f;
				acceptsForce = false;
			}};
			outputSpeed = 3f;
		}};
		turbineSwing = new ForceSwayCrafter("turbine-swing") {{
			requirements(Category.power, with(
				Items.silicon, 50,
				Items.graphite, 80,
				SWItems.compound, 60
			));
			size = 2;
			health = 160;
			swayScl = 0.7f;
			craftTime = 1f;
			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawRegion("-rotator") {{
					spinSprite = true;
					rotateSpeed = 16f;
				}},
				new DrawDefault()
			);
			consumeLiquid(SWLiquids.steam, 0.2f);
			forceConfig = new ForceConfig() {{
				maxForce = 5f;
				friction = 0.06f;
				beltSizeOut = beltSizeIn = 4f;
				acceptsForce = false;
			}};
			outputSpeed = 5f;
		}};

		compoundMixer = new SWGenericCrafter("compound-mixer") {{
			requirements(Category.crafting, with(
				Items.silicon, 150,
				Items.graphite, 160,
				Items.titanium, 80,
				SWItems.compound, 200
			));
			hasHeat = false;
			forceConfig = new ForceConfig() {{
				friction = 0.09f;
				maxForce = 14f;
				beltSizeIn = beltSizeOut = 6f;
			}};
			size = 3;
			health = 200;
			craftTime = 45f;
			drawer = new DrawMulti(new DrawDefault(), new DrawRegion("-rotator"){{
				spinSprite = true;
				rotateSpeed = 2f;
			}});
			forceConfig.outputsForce = false;
			consume(new ConsumeSpeed(1.5f, 15f));
			consumeItems(with(
				Items.copper, 2,
				SWItems.nickel, 1
			));
			outputItem = new ItemStack(SWItems.compound, 2);
		}};

		burner = new SWGenericCrafter("burner") {{
			requirements(Category.power, with(
				Items.silicon, 20,
				Items.graphite, 30,
				SWItems.denseAlloy, 50,
				Items.lead, 25)
			);
			size = 2;
			health = 160;
			consume(new ConsumeItemFlammable());
			drawer = new DrawMulti(new DrawDefault(), new DrawFlame() {{
				flameRadius = 1.5f;
				flameRadiusIn = 0.75f;
				flameRadiusMag = 1f;
			}});
			hasForce = false;
			updateEffect = Fx.coalSmeltsmoke;
			craftTime = 120f;
			outputHeat = 500f;
			outputHeatSpeed = 3f;
			heatConfig.acceptHeat = false;
		}};
		resistance = new SWGenericCrafter("resistance") {{
			requirements(Category.power, with(
				Items.silicon, 100,
				SWItems.denseAlloy, 90,
				Items.titanium, 70,
				Items.graphite, 70
			));
			hasForce = false;
			size = 2;
			health = 160;
			outputHeatSpeed = 400f;
			outputHeat = 800f;
//			consume(new ConsumePowerMin(9f, 0f, 540f, false));
			consumePower(9f);
			heatConfig = new HeatConfig() {{
				maxHeat = 1000f;
				acceptHeat = false;
			}};
			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawTemperature(),
				new DrawDefault()
			);
		}};
		frictionHeater = new SWGenericCrafter("friction-heater") {{
			requirements(Category.power, with(
				Items.silicon, 150,
				SWItems.denseAlloy, 160,
				SWItems.nickel, 100,
				Items.titanium, 80,
				SWItems.compound, 200
			));
			clampRotation = true;
			size = 3;
			health = 200;
			craftTime = 5f;
			craftEffect = SWFx.sparks;
			forceConfig = new ForceConfig() {{
				friction = 0.18f;
				maxForce = 18f;
				beltSizeIn = beltSizeOut = 6f;
				outputsForce = false;
			}};
			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawRegion("-bar"), new DrawSinSpin() {{
				sinScl = 1f;
				sinMag = 6.25f;
			}}, new DrawDefault());
			consume(new ConsumeSpeed(0.6f, 6f));
			consume(new ConsumeTorque(5.4f, 18f));
			heatConfig.acceptHeat = false;
			outputHeat = 550f;
		}};

		boiler = new HeatGenericCrafter("boiler") {{
			requirements(Category.production, with(SWItems.nickel, 40, Items.metaglass, 35, Items.titanium, 30));
			heatConfig.outputHeat = false;
			size = 2;
			health = 160;
			hasLiquids = true;
			consumeLiquid(Liquids.water, 0.1f);
			consume(new ConsumeHeat(0.5f, 100f, false));
			updateEffect = Fx.smoke;
			outputLiquid = new LiquidStack(SWLiquids.steam, 0.15f);
		}};
		thermalBoiler = new AttributeCrafter("thermal-boiler") {{
			requirements(Category.production, with(SWItems.nickel, 40, Items.titanium, 50, Items.metaglass, 35, Items.titanium, 30));
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
			consumeItems(with(Items.copper, 2, Items.lead, 1));
			consumePower(1f);
			outputItems = with(SWItems.nickel, 1);
		}};
		oilDistiller = new GenericCrafter("oil-distiller") {{
			requirements(Category.crafting, with(
				Items.graphite, 70,
				Items.titanium, 50,
				Items.copper, 120,
				SWItems.nickel, 60
			));
			size = 2;
			health = 160;
			drawer = new DrawMulti(new DrawDefault(), new DrawFlame());
			updateEffect = Fx.smeltsmoke;
			consumeLiquid(Liquids.oil, 0.2f);
			consumePower(2f);
			outputLiquid = new LiquidStack(SWLiquids.butane, 0.1f);
		}};
		batchPress = new StackCrafter("batch-press") {{
			requirements(Category.crafting, with(
				Items.titanium, 120,
				Items.silicon, 50,
				Items.graphite, 75,
				Items.plastanium, 140,
				Items.thorium, 80
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
			outputItems = with(Items.graphite, 8);
		}};
		rebuilder = new MultiCrafter("rebuilder") {{
			requirements(Category.crafting, with(
				SWItems.nickel, 65,
				Items.silicon, 80,
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
					consumeItems = with(SWItems.nickel, 2, Items.silicon, 2);
					consumeLiquids = LiquidStack.with(SWLiquids.steam, 0.3f);
					outputItems = with(SWItems.denseAlloy, 1);
					drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidRegion(SWLiquids.steam), new DrawDefault(), new DrawRegion("-top"));
					updateEffect = Fx.smoke;
				}}
 			);
		}};

//		power
		stirlingGenerator = new HeatConsumeGenerator("stirling-generator") {{
      requirements(Category.power, with(
				SWItems.denseAlloy, 120,
				SWItems.nickel, 110,
	      Items.silicon, 140,
	      Items.titanium, 100
      ));
			size = 3;
			health = 160;
			powerProduction = 7f;
			generateEffect = Fx.smoke;
			generateEffectRange = 12f;
			effectChance = 0.1f;
			consume(new ConsumeHeat(1f, 200, false));
			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawPistons() {{
					sides = 8;
					sideOffset = 10f;
					sinScl = -2.5f;
					sinMag = 2f;
				}},
				new DrawDefault()
			);
    }};

//		turrets
		Color col = Pal.accent.cpy().a(0.3f);
		bolt = new HeatTurret("bolt") {{
			requirements(Category.turret, with(
				Items.silicon, 120,
				Items.copper, 70,
				SWItems.denseAlloy, 95
			));
			consumer = consume(new ConsumeHeat(1, 250, false));
			drawer = new DrawTurret() {{
				parts.add(
					new RegionPart("-barrel") {{
						moveY = -1f;
						under = true;
					}}
				);
			}};
			shootSound = Sounds.none;
			loopSound = Sounds.torch;
			loopSoundVolume = 1f;
			size = 2;
			health = 180 * 4;
			range = 92f;
			shootSound = Sounds.blaster;
			shootType = new ContinuousLaserBulletType(5) {{
				width = 4f;
				length = 92f;
				colors = new Color[]{col, col, col};
			}};
		}};
		light = new HeatTurret("light") {{
			requirements(Category.turret, with(
				Items.titanium, 200,
				Items.silicon, 200,
				Items.graphite, 220,
				SWItems.denseAlloy, 210
			));
			consumer = consume(new ConsumeHeat(2, 400, true));
			size = 3;
			health = 200 * 9;
			range = 180f;
			shootSound = Sounds.none;
			loopSound = Sounds.torch;
			loopSoundVolume = 1f;
			shoot = new ShootSpread(3, 15);
			shootType = new ContinuousLaserBulletType(15) {{
				length = 180f;
				colors = new Color[]{col, col, col};
			}};
		}};

		mortar = new ForceTurret("mortar") {{
			requirements(Category.turret, with(
				Items.silicon, 120,
				Items.graphite, 100,
				SWItems.compound, 150
			));
			targetAir = false;
			consume(new ConsumeSpeed(1f, 6f));
			consumeItem(Items.graphite, 2);
			size = 3;
			health = 200 * 9;
			reload = 120f;
			range = 180f;
			recoil = -1f;
			shootY = 2f;
			shootSound = Sounds.mediumCannon;
			shoot = new ShootPattern() {{
				firstShotDelay = 30f;
			}};
			forceConfig = new ForceConfig() {{
				friction = 0.09f;
				maxForce = 10f;
				beltSizeIn = beltSizeOut = 6f;
				outputsForce = false;
			}};
			drawer = new DrawTurret() {{
				parts.addAll(
					new RegionPart("-launcher") {{
						x = y = 0f;
						moveY = -10;
						progress = PartProgress.charge.mul(1.5f).curve(Interp.pow2Out);
					}}
				);
			}};
			shootType = new ArtilleryBulletType(4f, 120) {{
				width = height = 20f;
				splashDamage = 120f;
				splashDamageRadius = 40f;
				lifetime = 50f;
			}};
		}};
		incend = new ForceTurret("incend") {{
			requirements(Category.turret, with(
				Items.silicon, 50,
				SWItems.compound, 40,
				Items.graphite, 50,
				Items.titanium, 80
			));
			size = 2;
			health = 890;
			reload = 6f;
			recoil = 0.5f;
			range = 92f;
			shootY = 6f;
			targetAir = false;
			shootSound = Sounds.flame;
			consume(new ConsumeSpeed(1f, 6f));
			consumeLiquid(SWLiquids.butane, 0.2f);

			forceConfig = new ForceConfig() {{
				friction = 0.18f;
				maxForce = 10f;
				beltSizeIn = beltSizeOut = 4f;
				outputsForce = false;
			}};

			drawer = new DrawTurret() {{
				parts.add(
					new RegionPart("-barrel") {{
						moveY = -1f;
						under = true;
					}}
				);
			}};

			shootType = new BulletType(4f, 25) {{
				hitSize = 8f;
				lifetime = 23f;
				status = StatusEffects.burning;
				despawnEffect = Fx.none;
				shootEffect = SWFx.longShootFlame;
				hitEffect = Fx.hitFlameSmall;
				statusDuration = 60 * 4f;
				pierce = true;
				collidesAir = false;
				keepVelocity = false;
				hittable = false;
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
		denseWall = new Wall("dense-wall") {{
			requirements(Category.defense, with(SWItems.denseAlloy, 6));
			health = 200 * 4;
			absorbLasers = true;
		}};
		denseWallLarge = new Wall("dense-wall-large") {{
			requirements(Category.defense, mult(denseWall.requirements, 4));
			size = 2;
			health = 200 * 4 * 4;
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
		structuraFactory = new SingleUnitFactory("structura-factory") {{
			requirements(Category.units, with(
				SWItems.compound, 150,
				Items.graphite, 100,
				Items.silicon, 120,
				Items.copper, 100
			));
			size = 3;
			health = 160;
			consumePower(2f);
			itemCapacity = 40;
			unitPlan = new UnitPlan(SWUnitTypes.structura, 60f * 30f, with(Items.silicon, 20, Items.titanium, 15, SWItems.compound, 10));
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
		filler = new Filler("filler") {{
			requirements(Category.effect, with(
				Items.titanium, 340,
				Items.silicon, 270,
				SWItems.compound, 200
			));
			size = 2;
			itemCapacity = 200;
			passes = new Entry[]{
				new Entry<>(){{key=Blocks.deepwater; value = Blocks.water;}},
				new Entry<>(){{key=Blocks.water; value = Blocks.metalFloor;}}
			};
			consumeItem(SWItems.compound, 20);
			consumePower(4f);
		}};

//		sandbox
		allSource = new ResourceSource("all-source") {{
			health = 2147483647;
		}};
	}
}
