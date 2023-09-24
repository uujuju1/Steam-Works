package sw.content;

import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.blocks.production.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.blocks.units.*;
import mindustry.world.draw.*;
import sw.content.blocks.*;
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

		resistantConveyor,

		nickelForge, oilDistiller,
    batchPress,
		rebuilder, impactBuilder,

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

//		distribution
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
