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
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.power.*;
import mindustry.world.blocks.production.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.blocks.units.*;
import mindustry.world.draw.*;
import sw.content.blocks.*;
import sw.world.blocks.defense.*;
import sw.world.blocks.distribution.*;
import sw.world.blocks.environment.*;
import sw.world.blocks.production.*;
import sw.world.blocks.sandbox.*;
import sw.world.blocks.units.*;
import sw.world.recipes.*;

import static arc.struct.ObjectMap.*;
import static mindustry.type.ItemStack.*;
import static mindustry.world.blocks.units.UnitFactory.*;

public class SWBlocks {
	public static Block

		mechanicalBore, mechanicalCrusher, hydraulicDrill,

		resistantConveyor,
		mechanicalDistributor, mechanicalBridge,
		mechanicalOverflowGate, mechanicalUnderflowGate, mechanicalUnloader,

		siliconBoiler,
		nickelForge, oilDistiller,
    batchPress,
		rebuilder, impactBuilder,

		powerWire,
		burner,

		artyleriya, thermikos,

    compoundWall, compoundWallLarge, denseWall, denseWallLarge,

    subFactory,
		crafterFactory, structuraFactory,

		coreScaffold,
		filler,

		allSource;

	public static void load() {
		SWEnvironment.load();
		SWForce.load();
		SWHeat.load();
		SWVibration.load();

		mechanicalBore = new BeamDrill("mechanical-bore") {{
			requirements(Category.production, with(
				SWItems.nickel, 50
			));
			size = 2;
			health = 160;
			glowIntensity = pulseIntensity = 0f;
			drillTime = 160f;
			tier = 3;
			range = 5;
			boostHeatColor = Color.black;
			optionalBoostIntensity = 1f;
		}};
		mechanicalCrusher = new WallCrafter("mechanical-crusher") {{
			requirements(Category.production, with(
				SWItems.nickel, 50,
				Items.graphite, 50
			));
			size = 2;
			health = 160;
			drillTime = 110f;
			output = Items.sand;
			ambientSound = Sounds.drill;
			ambientSoundVolume = 0.04f;
		}};
		hydraulicDrill = new Drill("hydraulic-drill") {{
			requirements(Category.production, with(
				SWItems.nickel, 80,
				Items.graphite, 100
			));
			size = 2;
			health = 160;
			tier = 3;
			drillTime = 400;
		}};

//		distribution
		resistantConveyor = new MechanicalConveyor("resistant-conveyor") {{
			requirements(Category.distribution, with(
				SWItems.nickel, 2
			));
			health = 125;
			speed = 0.08f;
			displayedSpeed = 11f;
		}};
		mechanicalDistributor = new DuctRouter("mechanical-distributor") {{
			requirements(Category.distribution, with(
				SWItems.nickel, 5,
				Items.graphite, 3
			));
			health = 40;
			speed = 2f;
			solid = false;
		}};
		mechanicalBridge = new DuctBridge("mechanical-bridge") {{
			requirements(Category.distribution, with(
				SWItems.nickel, 7,
				Items.graphite, 5
			));
			health = 50;
			speed = 2f;
		}};
		mechanicalOverflowGate = new OverflowDuct("mechanical-overflow-gate") {{
			requirements(Category.distribution, with(
				SWItems.nickel, 7,
				Items.graphite, 5
			));
			health = 40;
			solid = false;
		}};
		mechanicalUnderflowGate = new OverflowDuct("mechanical-underflow-gate") {{
			requirements(Category.distribution, with(
				SWItems.nickel, 7,
				Items.graphite, 5
			));
			health = 40;
			solid = false;
			invert = true;
		}};
		mechanicalUnloader = new DirectionalUnloader("mechanical-unloader") {{
			requirements(Category.distribution, with(
				SWItems.nickel, 7,
				Items.graphite, 5
			));
			health = 50;
			speed = 2f;
			solid = false;
			underBullets = true;
			regionRotated1 = 1;
		}};

//		crafting
		siliconBoiler = new GenericCrafter("silicon-boiler") {{
			requirements(Category.crafting, with(
				SWItems.nickel, 150,
				Items.titanium, 120,
				Items.graphite, 80
			));
			size = 3;
			health = 240;

			craftTime = 90f;

			consumeItems(with(Items.graphite, 2, Items.sand, 2));
			outputItems = with(Items.silicon, 1);

			drawer = new DrawMulti(
				new DrawDefault(),
				new DrawFlame() {{
					flameRadius = 5f;
				}}
			);
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
			drawer = new DrawMulti(
				new DrawRegion("-base"),
				new DrawPistons() {{
					sides = 4;
					sideOffset = 10f;
					sinScl = 2.5f;
					sinMag = 2f;
				}},
				new DrawDefault()
			);
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
				Items.titanium, 60,
				Items.graphite, 75
			));
			size = 3;
			health = 200;
			hasLiquids = true;
			recipes.add(
				new GenericRecipe() {{
					craftTime = 45f;

					consumeItems = with(SWItems.nickel, 1, Items.titanium, 2);
					consumePower = 10f/60f;

					outputItems = with(SWItems.compound, 1);

					drawer = new DrawMulti(
						new DrawRegion("-bottom"),
						new DrawBlurSpin("-rotator", 2) {{
						blurThresh = 12331;
						}},
						new DrawDefault(),
						new DrawRegion("-cap")
					);

					craftEffect = SWFx.compoundCraft;
					updateEffect = 	Fx.smeltsmoke;
				}},
				new GenericRecipe() {{
					craftTime = 60f;

					consumeItems = with(Items.titanium, 2, Items.silicon, 2);
					consumePower = 20f/60f;

					outputItems = with(SWItems.denseAlloy, 1);

					drawer = new DrawMulti(
						new DrawRegion("-bottom"),
						new DrawLiquidRegion(SWLiquids.steam),
						new DrawDefault(), new DrawRegion("-top")
					);

					updateEffect = Fx.smoke;
				}}
 			);
		}};
		impactBuilder = new MultiCrafter("impact-builder") {{
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
					consumeItems = with(SWItems.compound, 1, Items.titanium, 2);
					outputItems = with(SWItems.neodymium, 1);
					drawer = new DrawMulti(
						new DrawRegion("-bottom"),
						new DrawWeave(),
						new DrawDefault(),
						new DrawRegion("-cap")
					);
					craftEffect = SWFx.neodymiumCraft;
				}},
				new GenericRecipe() {{
					craftTime = 60f;
					consumeItems = with(SWItems.denseAlloy, 2);
					consumeLiquids = LiquidStack.with(Liquids.cryofluid, 0.3f);
					outputItems = with(SWItems.frozenMatter, 1);
					drawer = new DrawMulti(
						new DrawRegion("-bottom"),
						new DrawDefault(),
						new DrawRegion("-cover"),
						new DrawGlowRegion() {{
							color = Pal.lancerLaser;
						}}
					);
					craftEffect = SWFx.frozenMatterCraft;
					updateEffect = 	Fx.smeltsmoke;
				}}
			);
		}};

		artyleriya = new ItemTurret("artyleriya") {{
			requirements(Category.turret, with(
				SWItems.nickel, 100,
				Items.graphite, 120
			));
			size = 2;
			health = 480;
			reload = 60f;
			range = 96f;
			shoot = new ShootAlternate();
			ammo(
				SWItems.nickel, new BasicBulletType(2f, 30) {{
					lifetime = 48f;
					width = height = 12f;
					splashDamage = 15;
					splashDamageRadius = 16f;
				}},
				Items.titanium, new ArtilleryBulletType(2f, 50) {{
					lifetime = 60f;
					rangeChange = 24f;
					width = height = 12f;
					splashDamage = 50;
					splashDamageRadius = 24f;
				}}
			);
		}};
		thermikos = new SWConsumeTurret("thermikos") {{
			requirements(Category.turret, with(
				Items.silicon, 200,
				Items.titanium, 150,
				SWItems.denseAlloy, 220
			));
			size = 3;
			scaledHealth = 220f;
			range = 240f;
			reload = 120f;

			shootY = 8f;
			shoot = new ShootPattern() {{
				firstShotDelay = 30f;
			}};

			shootSound = Sounds.cannon;
			chargeSound = Sounds.lasercharge2;

			consumeItems(with(Items.graphite, 2, SWItems.neodymium, 3));

			drawer = new DrawTurret() {{
				parts.add(
					new RegionPart("-cannon") {{
						under = true;
						moveY = -4f;
						progress = PartProgress.heat.curve(Interp.bounceIn);
					}}
				);
			}};

			shootType = new ArtilleryBulletType(4f, 200f) {{
				splashDamage = 200f;
				splashDamageRadius = 16f;
				lifetime = 40f;
				width = height = 20f;

				collides = collidesAir = collidesGround = true;

				shootEffect = SWFx.shootFirery;
				chargeEffect = SWFx.chargeFiery;
			}};
		}};

//		power
		powerWire = new PowerNode("power-wire") {{
			requirements(Category.power, with(
				Items.titanium, 10,
				SWItems.nickel, 5
			));
			size = 2;
			health = 80;
			maxNodes = 3;
			maxRange = 80f;
		}};
		burner = new ConsumeGenerator("burner") {{
			requirements(Category.power, with(
				Items.titanium, 50,
				SWItems.nickel, 80
			));
			size = 2;
			health = 160;

			consumeItem(Items.graphite, 1);

			itemDuration = 30f;
			powerProduction = 0.5f;
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
			unitType = SWUnitTypes.delta;
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
