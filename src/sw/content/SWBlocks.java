package sw.content;

import arc.graphics.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.power.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.blocks.units.*;
import mindustry.world.draw.*;
import sw.content.blocks.*;
import sw.util.*;
import sw.world.blocks.environment.*;
import sw.world.blocks.production.*;
import sw.world.blocks.sandbox.*;
import sw.world.blocks.units.*;
import sw.world.draw.*;
import sw.world.recipes.*;

import static mindustry.type.ItemStack.*;

public class SWBlocks {
	public static Block

		rebuilder,
		pressModule, smelterModule, arcSmelterModule, impactPressModule, mixerModule, crystalizerModule,

		powerWire,
		burner,

    subFactory,
		constructor,
		upgrader,

		coreScaffold,
		filler,

		allSource;

	public static void load() {
		SWEnvironment.load();
		SWTurrets.load();
		SWProduction.load();
		SWDefense.load();
		SWDistribution.load();
		SWCrafting.load();
		SWPower.load();

		// region crafting


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

		smelterModule = new MultiCrafterRecipe("smelter-module", rebuilder) {{
			requirements(Category.crafting, with(
				SWItems.compound, 50,
				Items.silicon, 50
			));
			recipe = new GenericRecipe() {{
				consumeItems = with(Items.silicon, 2, SWItems.nickel, 3);
				outputItems = with(SWItems.compound, 2);
				craftTime = 30f;
				craftEffect = SWFx.compoundCraft;
				drawer = new DrawMulti(
					new DrawRegion("-bottom"),
					new DrawDefault(),
					new DrawFlame(SWDraw.compoundBase) {{
						flameRadius = 5.5f;
						flameRadiusIn = 3f;
					}},
					new DrawRegion("-compound")
				);
			}};
		}};
		impactPressModule = new MultiCrafterRecipe("impact-press-module", rebuilder) {{
			requirements(Category.crafting, with(
				SWItems.denseAlloy, 50,
				Items.silicon, 50
			));
			recipe = new GenericRecipe() {{
				consumeItems = with(Items.silicon, 3, SWItems.iron, 3);
				outputItems = with(SWItems.denseAlloy, 3);
				craftTime = 45f;
				craftEffect = SWFx.denseAlloyCraft;
				drawer = new DrawMulti(
					new DrawRegion("-bottom"),
					new DrawDefault(),
					new DrawRegion("-cover-impact"),
					new DrawRegion("-dense-alloy")
				);
			}};
		}};
		pressModule = new MultiCrafterRecipe("press-module", rebuilder) {{
			requirements(Category.crafting, with(
				SWItems.compound, 50,
				Items.silicon, 50
			));
			recipe = new GenericRecipe() {{
				consumeItems = with(Items.graphite, 4, Items.titanium, 2);
				outputItems = with(SWItems.compound, 2);
				craftEffect = SWFx.compoundCraft;
				drawer = new DrawMulti(
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
			size = 3;
			health = 2000;
			alwaysUnlocked = true;
			unitType = SWUnitTypes.lambda;
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
			consumeItems(with(Items.silicon, 10, Items.sand, 10));
			consumePower(1f);
		}};
		// endregion

		// region sandbox
		allSource = new ResourceSource("all-source") {{
			health = 2147483647;
		}};
		// endregion
	}
}
