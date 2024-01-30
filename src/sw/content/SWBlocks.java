package sw.content;

import arc.graphics.*;
import arc.math.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.entities.part.*;
import mindustry.entities.pattern.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.blocks.power.*;
import mindustry.world.blocks.production.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.blocks.units.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import sw.content.blocks.*;
import sw.util.*;
import sw.world.blocks.defense.*;
import sw.world.blocks.distribution.*;
import sw.world.blocks.environment.*;
import sw.world.blocks.liquids.*;
import sw.world.blocks.production.*;
import sw.world.blocks.sandbox.*;
import sw.world.blocks.units.*;
import sw.world.draw.*;
import sw.world.recipes.*;

import static mindustry.type.ItemStack.*;
import static mindustry.world.blocks.units.UnitFactory.*;

public class SWBlocks {
	public static Block

		mechanicalBore, mechanicalCrusher, hydraulicDrill,

		resistantConveyor, suspensionConveyor,
		mechanicalDistributor, mechanicalBridge, mechanicalTunnel,
		mechanicalGate,
		mechanicalOverflowGate, mechanicalUnderflowGate, mechanicalUnloader,

		mechanicalPayloadConveyor, mechanicalPayloadRouter,

		mechanicalConduit,

		siliconBoiler,
		oilDistiller,
		compoundSmelter, densePress,

		rebuilder,
		pressModule, smelterModule, arcSmelterModule, impactPressModule, mixerModule, crystalizerModule,

		powerWire,
		burner,

		flow, trail, vniz, rozpad,
		artyleriya, curve, thermikos,

		ironWall, ironWallLarge, nickelWall, nickelWallLarge,
    compoundWall, compoundWallLarge, denseWall, denseWallLarge,

    subFactory,
		crafterFactory, structuraFactory,
		constructor,
		upgrader,

		coreScaffold,
		filler,

		allSource;

	public static void load() {
		SWEnvironment.load();
		SWForce.load();
		SWVibration.load();

		// region production
		mechanicalBore = new BeamDrill("mechanical-bore") {{
			requirements(Category.production, with(
				SWItems.nickel, 25
			));
			researchCost = with(
				SWItems.nickel, 100
			);
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
				SWItems.nickel, 25,
				Items.graphite, 25
			));
			researchCost = with(
				SWItems.nickel, 100,
				Items.graphite, 100
			);
			size = 2;
			health = 160;
			drillTime = 110f;
			output = Items.sand;
			ambientSound = Sounds.drill;
			ambientSoundVolume = 0.04f;
		}};
		hydraulicDrill = new Drill("hydraulic-drill") {{
			requirements(Category.production, with(
				SWItems.nickel, 40,
				Items.graphite, 50
			));
			researchCost = with(
				SWItems.nickel, 160,
				Items.graphite, 200
			);
			size = 2;
			health = 160;
			tier = 3;
			drillTime = 400;
		}};
		// endregion

		// region distribution
		resistantConveyor = new MechanicalConveyor("resistant-conveyor") {{
			requirements(Category.distribution, with(SWItems.nickel, 1));
			health = 100;
			speed = 0.04f;
			displayedSpeed = 5f;
			researchCost = with(
				SWItems.nickel, 30
			);
		}};
		suspensionConveyor = new MechanicalConveyor("suspension-conveyor") {{
			requirements(Category.distribution, with(SWItems.iron, 1));
			health = 109;
			speed = 0.04f;
			displayedSpeed = 5f;
			armored = true;
		}};
		mechanicalDistributor = new MechanicalDistributorSorter("mechanical-distributor") {{
			requirements(Category.distribution, with(SWItems.nickel, 5));
			health = 100;
			solid = false;
		}};
		mechanicalTunnel = new MechanicalTunnel("mechanical-tunnel") {{
			requirements(Category.distribution, with(
				SWItems.nickel, 5,
				SWItems.iron, 5
			));
			health = 100;
			floors.put(SWEnvironment.charoite, -1);
			floors.put(SWEnvironment.charoiteCraters, 2);
			floors.put(SWEnvironment.grime, -1);
			floors.put(SWEnvironment.scorched, -1);
			floors.put(SWEnvironment.scorchedCrater, 2);
		}};
		mechanicalGate = new MechanicalGate("mechanical-gate") {{
			requirements(Category.distribution, with(
				SWItems.nickel, 5,
				SWItems.iron, 3
			));
			health = 100;
		}};
		mechanicalUnloader = new DirectionalUnloader("mechanical-unloader") {{
			requirements(Category.distribution, with(
				SWItems.nickel, 6,
				SWItems.iron, 4
			));
			health = 100;
			speed = 2f;
			solid = false;
			underBullets = true;
			regionRotated1 = 1;
		}};

		mechanicalPayloadConveyor = new PayloadConveyor("mechanical-payload-conveyor") {{
			requirements(Category.units, with(
				Items.silicon, 10,
				Items.titanium, 10,
				SWItems.nickel, 10
			));
			canOverdrive = false;
		}};
		mechanicalPayloadRouter = new PayloadRouter("mechanical-payload-router") {{
			requirements(Category.units, with(
				Items.silicon, 10,
				Items.titanium, 10,
				SWItems.nickel, 10
			));
			canOverdrive = false;
		}};
		//endregion

		//region liquids
		mechanicalConduit = new MechanicalConduit("mechanical-conduit") {{
			requirements(Category.liquid, with());
			health = 100;
		}};
		//endregion

		// region crafting
		siliconBoiler = new GenericCrafter("silicon-boiler") {{
			requirements(Category.crafting, with(
				SWItems.nickel, 150,
				SWItems.iron, 120,
				Items.graphite, 80
			));
			researchCost = with(
				SWItems.nickel, 1500,
				Items.titanium, 1200,
				Items.graphite, 800
			);
			size = 3;
			health = 240;

			craftTime = 90f;

			consumeItems(with(Items.graphite, 2, Items.sand, 2));
			consumePower(0.5f);
			outputItems = with(Items.silicon, 3);

			drawer = new DrawMulti(
				new DrawDefault(),
				new DrawFlame() {{
					flameRadius = 5f;
				}}
			);
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
			outputLiquid = new LiquidStack(SWLiquids.fungi, 0.1f);
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
				Items.graphite, 1,
				SWItems.nickel, 2
			));
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
			craftTime = 120f;
			craftEffect = SWFx.denseAlloyCraft;

			consumeItems(with(
				SWItems.iron, 3,
				SWItems.nickel, 2
			));
			outputItem = new ItemStack(SWItems.denseAlloy, 2);
		}};

		rebuilder = new MultiCrafter("rebuilder") {{
			requirements(Category.crafting, with(
				SWItems.nickel, 65,
				SWItems.iron, 60,
				Items.silicon, 80,
				Items.graphite, 75
			));
			size = 3;
			health = 200;
			hasLiquids = true;
			baseDrawer = new DrawMulti(new DrawRegion("-bottom"), new DrawDefault());
		}};

		pressModule = new MultiCrafterRecipe("press-module", rebuilder) {{
			requirements(Category.crafting, with(
				SWItems.graphene, 50,
				Items.silicon, 50
			));
			recipe = new GenericRecipe() {{
				consumeItems = with(Items.graphite, 4, Items.silicon, 2);
				outputItems = with(SWItems.graphene, 3);
				craftEffect = SWFx.grapheneCraft;
				drawer = new DrawMulti(
					new DrawDefault(),
					new DrawRegion("-cap"),
					new DrawRegion("-overlay-graphene")
				);
			}};
		}};
		impactPressModule = new MultiCrafterRecipe("impact-press-module", rebuilder) {{
			requirements(Category.crafting, with(
				SWItems.denseAlloy, 50,
				Items.silicon, 50
			));
			recipe = new GenericRecipe() {{
				consumeItems = with(SWItems.nickel, 2, SWItems.iron, 3);
				outputItems = with(SWItems.denseAlloy, 3);
				craftTime = 60f;
				craftEffect = SWFx.denseAlloyCraft;
				drawer = new DrawMulti(
					new DrawRegion("-bottom"),
					new DrawDefault(),
					new DrawRegion("-cap-batch"),
					new DrawRegion("-overlay-dense-alloy")
				);
			}};
		}};
		smelterModule = new MultiCrafterRecipe("smelter-module", rebuilder) {{
			requirements(Category.crafting, with(
				SWItems.compound, 50,
				Items.silicon, 50
			));
			recipe = new GenericRecipe() {{
				consumeItems = with(Items.graphite, 4, Items.titanium, 2);
				outputItems = with(SWItems.compound, 2);
				craftEffect = SWFx.compoundCraft;
				drawer = new DrawMulti(
					new DrawRegion("-bottom"),
					new DrawDefault(),
					new DrawFlame(SWDraw.compoundBase) {{
						flameRadius = 5.5f;
						flameRadiusIn = 3f;
					}},
					new DrawRegion("-overlay-compound")
				);
			}};
		}};
		arcSmelterModule = new MultiCrafterRecipe("arc-smelter-module", rebuilder) {{
			requirements(Category.crafting, with(
				SWItems.scorch, 50,
				Items.silicon, 50
			));
			recipe = new GenericRecipe() {{
				consumeItems = with(SWItems.thermite, 2, Items.graphite, 2);
				outputItems = with(SWItems.scorch, 2);
				craftEffect = SWFx.scorchCraft;
				drawer = new DrawMulti(
					new DrawRegion("-bottom"),
					new DrawFire(),
					new DrawDefault(),
					new DrawRegion("-cap-kiln"),
					new DrawRegion("-overlay-scorch")
				);
			}};

		}};
		mixerModule = new MultiCrafterRecipe("mixer-module", rebuilder) {{
			requirements(Category.crafting, with(
				SWItems.thermite, 50,
				Items.silicon, 50
			));
			recipe = new GenericRecipe() {{
				consumeItems = with(Items.graphite, 2, SWItems.compound, 2);
				outputItems = with(SWItems.thermite, 4);
				drawer = new DrawMulti(
					new DrawRegion("-bottom"),
					new DrawMixing() {{
						circleRad = 10f;
						particleRad = 10f;
						alpha = 0.5f;
						particleColor = circleColor = Color.valueOf("6D6F7F");
					}},
					new DrawMixing() {{
						circleRad = 1f;
						particleRad = 10f;
						idOffset = 1;
						alpha = 0.5f;
						particleColor = circleColor = Color.valueOf("6C5252");
					}},
					new DrawBlurSpin("-rotator", 5) {{
						blurThresh = 12331;
					}},
					new DrawDefault(),
					new DrawRegion("-motor"),
					new DrawRegion("-overlay-thermite")
				);
			}};
		}};
		crystalizerModule = new MultiCrafterRecipe("crystalizer-module", rebuilder) {{
			requirements(Category.crafting, with(
				SWItems.bismuth, 50,
				Items.silicon, 50
			));
			recipe = new GenericRecipe() {{
				consumeItems = with(Items.sand, 2, Items.graphite, 2);
				consumeLiquids = LiquidStack.with(Liquids.water, 0.1f);
				outputItems = with(SWItems.bismuth, 4);
				drawer = new DrawMulti(
					new DrawRegion("-bottom"),
					new DrawLiquidTile(Liquids.water, 4),
					new DrawCultivator() {{
						plantColorLight = Color.valueOf("C7D4CF");
						plantColor = Color.valueOf("97ABA4");
						bottomColor = Color.valueOf("97ABA4");
					}},
					new DrawDefault(),
					new DrawRegion("-cap-crystalizer"),
					new DrawRegion("-overlay-bismuth")
				);
			}};
		}};
		// endregion

		// region turrets
		flow = new ItemTurret("flow") {{
			requirements(Category.turret, with());
			size = 1;
			scaledHealth = 200;
			reload = 60f;
			range = 140f;
			targetAir = false;

			shootSound = Sounds.shootAlt;

			shoot = new ShootPattern() {{
				shotDelay = 5f;
				shots = 2;
			}};

			ammo(
				SWItems.nickel, new BasicBulletType(2, 10) {{
					frontColor = trailColor = Color.valueOf("A1A7AB");
					backColor = Color.valueOf("595E61");
					hitEffect = despawnEffect = Fx.hitBulletColor;
					width = 8f;
					height = 10f;
					lifetime = 70f;
					trailWidth = 1.5f;
					trailLength = 5;
					homingRange = 35f;
					homingPower = 0.02f;
					collidesAir = false;
				}},
				SWItems.iron, new BasicBulletType(2, 15) {{
					frontColor = trailColor = Color.valueOf("A1A1B8");
					backColor = Color.valueOf("6F6F85");
					hitEffect = despawnEffect = Fx.hitBulletColor;
					width = 8f;
					height = 10f;
					lifetime = 70f;
					trailWidth = 1.5f;
					trailLength = 5;
					homingRange = 35f;
					homingPower = 0.03f;
					collidesAir = false;
				}}
			);
		}};
		trail = new ItemTurret("trail") {{
			requirements(Category.turret, with());
			size = 2;
			scaledHealth = 200;
			reload = 30f;
			range = 180f;
			targetAir = false;

			shoot = new ShootSummon(0f, 0f, 0f, 5f) {{
				shots = 3;
				shotDelay = 1f;
			}};

			ammo(
				SWItems.nickel, new BasicBulletType(4, 15) {{
					frontColor = trailColor = Color.valueOf("A1A7AB");
					backColor = Color.valueOf("595E61");
					hitEffect = despawnEffect = Fx.hitBulletColor;
					width = 8f;
					height = 10f;
					lifetime = 45f;
					trailWidth = 1.5f;
					trailLength = 5;
					homingRange = 35f;
					homingPower = 0.04f;
					collidesAir = false;
				}},
				SWItems.iron, new BasicBulletType(4, 20) {{
					frontColor = trailColor = Color.valueOf("A1A1B8");
					backColor = Color.valueOf("6F6F85");
					hitEffect = despawnEffect = Fx.hitBulletColor;
					width = 8f;
					height = 10f;
					lifetime = 45f;
					trailWidth = 1.5f;
					trailLength = 5;
					homingRange = 35f;
					homingPower = 0.04f;
					collidesAir = false;
				}}
			);
		}};
		vniz = new ItemTurret("vniz") {{
			requirements(Category.turret, with());
			size = 1;
			scaledHealth = 200;
			reload = 90f;
			range = 180f;
			targetGround = false;

			BulletType frag = new BasicBulletType(2f, 5) {{
				frontColor = trailColor = Color.valueOf("A1A7AB");
				backColor = Color.valueOf("595E61");
				lifetime = 30f;
				trailWidth = 1f;
				trailLength = 5;
				homingRange = 60f;
				homingPower = 0.5f;
				collidesGround = false;
			}};
			ammo(
				SWItems.nickel, new ArtilleryBulletType(4, 10) {{
					frontColor = trailColor = Color.valueOf("A1A7AB");
					backColor = Color.valueOf("595E61");
					hitEffect = despawnEffect = Fx.hitBulletColor;
					width = 8f;
					height = 10f;
					lifetime = 45f;
					trailWidth = 1.5f;
					trailLength = 5;
					collides = collidesAir = true;

					fragBullets = 3;
					fragBullet = frag;
				}},
				SWItems.iron, new ArtilleryBulletType(4, 15) {{
					frontColor = trailColor = Color.valueOf("A1A1B8");
					backColor = Color.valueOf("6F6F85");
					hitEffect = despawnEffect = Fx.hitBulletColor;
					width = 8f;
					height = 10f;
					lifetime = 45f;
					trailWidth = 1.5f;
					trailLength = 5;
					collides = collidesAir = true;

					fragBullets = 5;
					fragBullet = frag;
				}}
			);
		}};
		rozpad = new ItemTurret("rozpad") {{
			requirements(Category.turret, with());
			size = 2;
			scaledHealth = 200;
			reload = 60f;
			range = 200f;
			targetGround = false;

			shoot = new ShootAlternate();

			BulletType frag = new BasicBulletType(2f, 10) {{
				frontColor = trailColor = Color.valueOf("A1A7AB");
				backColor = Color.valueOf("595E61");
				lifetime = 30f;
				trailWidth = 1f;
				trailLength = 5;
				homingRange = 60f;
				homingPower = 0.5f;
				collidesGround = false;
			}};
			ammo(
				SWItems.nickel, new ArtilleryBulletType(4, 20) {{
					frontColor = trailColor = Color.valueOf("A1A7AB");
					backColor = Color.valueOf("595E61");
					hitEffect = despawnEffect = Fx.hitBulletColor;
					width = 8f;
					height = 10f;
					lifetime = 50f;
					trailWidth = 1.5f;
					trailLength = 5;
					homingRange = 60f;
					homingPower = 0.2f;
					collides = collidesAir = true;

					fragBullets = 3;
					fragBullet = frag;
				}},
				SWItems.iron, new ArtilleryBulletType(4, 25) {{
					frontColor = trailColor = Color.valueOf("A1A1B8");
					backColor = Color.valueOf("6F6F85");
					hitEffect = despawnEffect = Fx.hitBulletColor;
					width = 8f;
					height = 10f;
					lifetime = 50f;
					trailWidth = 1.5f;
					trailLength = 5;
					homingRange = 60f;
					homingPower = 0.2f;
					collides = collidesAir = true;

					fragBullets = 5;
					fragBullet = frag;
				}}
			);
		}};
		artyleriya = new ItemTurret("artyleriya") {{
			researchCost = with(
				SWItems.nickel, 200,
				Items.graphite, 240
			);
		}};
		curve = new ItemTurret("curve") {{
			requirements(Category.turret, with(
				SWItems.nickel, 150,
				SWItems.iron, 120,
				Items.silicon, 80
			));
			researchCost = with(
				SWItems.nickel, 1500,
				Items.titanium, 1200,
				Items.silicon, 800
			);
			size = 3;
			scaledHealth = 220f;
			reload = 45f;
			range = 150f;
			shootY = 0f;
			shootSound = Sounds.missileSmall;
			shoot = new ShootBarrel() {{
				shots = 2;
				barrels = new float[]{
					6f, 8f, 15f,
					-6f, 8f, -15f
				};
			}};
			ammo(
				Items.silicon, new BasicBulletType(3f, 40) {{
					homingPower = 0.08f;
					shrinkY = 0f;
					width = 14f;
					height = 14f;
					hitSound = Sounds.explosion;
					lifetime = 50f;
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

			consumeItems(with(Items.graphite, 2, SWItems.thermite, 3));

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
		// endregion

		// region power
		powerWire = new PowerNode("power-wire") {{
			requirements(Category.power, with(
				SWItems.iron, 10,
				SWItems.nickel, 5
			));
			size = 2;
			health = 80;
			maxNodes = 3;
			maxRange = 80f;
		}};
		burner = new ConsumeGenerator("burner") {{
			requirements(Category.power, with(
				SWItems.iron, 50,
				SWItems.nickel, 80
			));
			researchCost = with(
				Items.titanium, 500,
				SWItems.nickel, 800
			);
			size = 2;
			health = 160;

			consumeItem(Items.graphite, 1);

			itemDuration = 60f;
			powerProduction = 0.5f;
		}};
		// endregion

		// region walls
		compoundWall = new Wall("compound-wall") {{
			requirements(Category.defense, with(SWItems.compound, 6));
			health = 110 * 4;
		}};
		compoundWallLarge = new Wall("compound-wall-large") {{
			requirements(Category.defense, mult(compoundWall.requirements, 4));
			size = 2;
			health = 110 * 4 * 4;
		}};
		denseWall = new Wall("dense-wall") {{
			requirements(Category.defense, with(SWItems.denseAlloy, 6));
			health = 110 * 4;
			absorbLasers = true;
		}};
		denseWallLarge = new Wall("dense-wall-large") {{
			requirements(Category.defense, mult(denseWall.requirements, 4));
			size = 2;
			health = 110 * 4 * 4;
			absorbLasers = true;
		}};
		nickelWall = new Wall("nickel-wall") {{
			requirements(Category.defense, with(SWItems.nickel, 6));
			health = 90 * 4;
		}};
		nickelWallLarge = new Wall("nickel-wall-large") {{
			requirements(Category.defense, mult(nickelWall.requirements, 4));
			size = 2;
			health = 90 * 4 * 4;
		}};
		ironWall = new Wall("iron-wall") {{
			requirements(Category.defense, with(SWItems.iron, 6));
			health = 100 * 4;
			absorbLasers = true;
		}};
		ironWallLarge = new Wall("iron-wall-large") {{
			requirements(Category.defense, mult(ironWall.requirements, 4));
			size = 2;
			health = 100 * 4 * 4;
			absorbLasers = true;
		}};
		// endregion

		// region units
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
		crafterFactory = new CrafterUnitBlock("crafter-factory") {{
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

		constructor = new UnitFactory("constructor") {{
			requirements(Category.units, with(
				SWItems.nickel, 150,
				SWItems.iron, 120,
				Items.silicon, 100
			));
			size = 3;
			health = 200;
			plans.add(
				new UnitPlan(SWUnitTypes.focus, 60 * Time.toSeconds, with(Items.silicon, 15, SWItems.nickel, 10)),
				new UnitPlan(SWUnitTypes.fly, 40 * Time.toSeconds, with(Items.silicon, 15, SWItems.iron, 10)),
				new UnitPlan(SWUnitTypes.sentry, 80 * Time.toSeconds, with(Items.silicon, 25, SWItems.denseAlloy, 10)),
				new UnitPlan(SWUnitTypes.recluse, 80 * Time.toSeconds, with(Items.silicon, 15, SWItems.compound, 20))
			);
			consumePower(1f);
			consumeLiquid(SWLiquids.steam, 0.3f);
		}};
		upgrader = new MultiReconstructor("upgrader") {{
			requirements(Category.units, with(
				SWItems.nickel, 200,
				SWItems.iron, 240,
				SWItems.graphene, 300,
				SWItems.scorch, 300,
				Items.silicon, 220
			));
			size = 5;
			health = 320;
			constructTime = 10 * 60f;
			upgrades.addAll(
				new UnitType[]{SWUnitTypes.focus, SWUnitTypes.precision},
				new UnitType[]{SWUnitTypes.precision, SWUnitTypes.target},
				new UnitType[]{SWUnitTypes.fly, SWUnitTypes.spin},
				new UnitType[]{SWUnitTypes.spin, SWUnitTypes.gyro},
				new UnitType[]{SWUnitTypes.sentry, SWUnitTypes.tower},
				new UnitType[]{SWUnitTypes.tower, SWUnitTypes.castle}
			);
			consumeItems(with(Items.silicon, 400));
		}};
		// endregion

		// region storage
		coreScaffold = new CoreBlock("core-scaffold") {{
			requirements(Category.effect, with(
				SWItems.nickel, 2000,
				Items.graphite, 1000
			));
			size = 4;
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
			size = 3;
			itemCapacity = 200;
			entries.addAll(
				new Block[]{SWEnvironment.deepChloro, Blocks.darkPanel3},
				new Block[]{SWEnvironment.chloro, Blocks.darkPanel3},
				new Block[]{SWEnvironment.shallowChloro, Blocks.darkPanel3}
			);
			consumeItems(with(Items.silicon, 10, Items.sand, 10));
			consumePower(1f);
		}};
		// endregion

		// region sandbox
		allSource = new ResourceSource("all-source") {{
			health = 2147483647;
		}};
		// endregion

		//region unused
		mechanicalOverflowGate = new OverflowDuct("mechanical-overflow-gate") {{
			requirements(Category.distribution, BuildVisibility.hidden, with(
				SWItems.nickel, 7,
				Items.graphite, 5
			));
			health = 40;
			solid = false;
		}};
		mechanicalUnderflowGate = new OverflowDuct("mechanical-underflow-gate") {{
			requirements(Category.distribution, BuildVisibility.hidden, with(
				SWItems.nickel, 7,
				Items.graphite, 5
			));
			health = 40;
			solid = false;
			invert = true;
		}};
		mechanicalBridge = new DuctBridge("mechanical-bridge") {{
			requirements(Category.distribution, BuildVisibility.hidden, with(
				SWItems.nickel, 7,
				Items.graphite, 5
			));
			health = 50;
			speed = 2f;
		}};
		//endregion
	}
}
