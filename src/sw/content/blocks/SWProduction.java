package sw.content.blocks;

import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;
import mindustry.world.draw.*;
import sw.content.*;
import sw.graphics.*;
import sw.world.blocks.production.*;
import sw.world.consumers.*;
import sw.world.draw.*;
import sw.world.draw.DrawAxles.*;
import sw.world.interfaces.*;
import sw.world.meta.*;

import static mindustry.type.ItemStack.*;

public class SWProduction {
	public static Block
		mechanicalBore, hydraulicDrill, mechanicalFracker,

		liquidCollector, fogCollector;

	public static void load() {
		mechanicalBore = new RangedDrill("mechanical-bore") {{
			requirements(Category.production, with(
				SWItems.verdigris, 10
			));
			researchCost = with(
				SWItems.verdigris, 20
			);
			size = 2;
			health = 160;
			drillTime = 480f;
			tier = 1;
			range = 5;
			ambientSound = Sounds.combustion;
			ambientSoundVolume = 0.01f;

			useAllowList = true;
			drillMultipliers.put(Items.graphite, 1);

			consumeLiquid(SWLiquids.solvent, 0.025f).boost();
			optionalBoostIntensity = 2;

			drawer = new DrawMulti(new DrawRegion(), new DrawRangedDrill());

			spinConfig.hasSpin = false;
		}};
		hydraulicDrill = new AreaDrill("hydraulic-drill") {{
			requirements(Category.production, with(
				SWItems.verdigris, 20,
				Items.graphite, 10
			));
			researchCost = with(
				SWItems.verdigris, 40,
				Items.graphite, 20
			);
			size = 2;
			health = 160;
			tier = 2;
			drillTime = hardnessDrillMultiplier = 240;
			drillEffect = SWFx.groundCrack;
			drillEffectRnd = 0f;
			ambientSound = Sounds.drillCharge;
			mineRect = new Rect(0, 0, 4, 4);

			consumeLiquid(SWLiquids.solvent, 0.025f).boost();
			liquidBoostIntensity = 1.5f;
		}};

		mechanicalFracker = new RangedDrill("mechanical-fracker") {{
			requirements(Category.production, with(
				SWItems.iron, 20,
				SWItems.verdigris, 40,
				Items.graphite, 35
			));

			size = 2;
			health = 160;
			drillTime = 60f;
			tier = 1;
			range = 5;
			ambientSound = Sounds.drill;
			ambientSoundVolume = 0.14f;

			useAllowList = true;
			drillMultipliers.put(Items.sand, 1);

			drillEffect = SWFx.blockCrack;

			consume(new ConsumeRotation() {{
				startSpeed = 0.5f;
				endSpeed = 1f;
				curve = Interp.one;
			}});

			drawer = new DrawMulti(
				new DrawAxles(
					b -> ((HasSpin) b).spinGraph().rotation * ((HasSpin) b).spinSection().ratio,
					new Axle("-shaft") {{
						pixelWidth = 64;
						pixelHeight = 7;

						y = 4;

						width = 16f;
						height = 3.5f;

						paletteLight = SWPal.axleLight;
						paletteMedium = SWPal.axleMedium;
						paletteDark = SWPal.axleDark;
					}},
					new Axle("-shaft") {{
						pixelWidth = 64;
						pixelHeight = 7;

						y = -4;

						width = 16f;
						height = 3.5f;

						paletteLight = SWPal.axleLight;
						paletteMedium = SWPal.axleMedium;
						paletteDark = SWPal.axleDark;
					}}
				),
				new DrawBitmask("-tiles", b -> 0) {{
					tileWidth = tileHeight = 64;
				}},
				new DrawSpinningDrill() {{
					startAxle = new Axle("-bore-start") {{
						pixelWidth = 96;
						pixelHeight = 7;

						width = 24f;
						height = 3.5f;
					}};
					middleAxle = new Axle("-bore-middle") {{
						pixelWidth = 32;
						pixelHeight = 7;

						width = 8f;
						height = 3.5f;
					}};
					endAxle = new Axle("-bore-end") {{
						pixelWidth = 8;
						pixelHeight = 7;

						width = 2f;
						height = 3.5f;
					}};

					layer = Layer.power - 1;
				}},
				new DrawRegion("-rotator", 2f, true) {{
					layer = Layer.power - 1;
				}}
			);

			spinConfig = new SpinConfig() {{
				topSpeed = 1f;
				resistance = 0.5f/60f;
				allowedEdges = new int[][]{
					new int[]{4, 5},
					new int[]{6, 7},
					new int[]{0, 1},
					new int[]{2, 3}
				};
			}};
		}};

		liquidCollector = new Pump("liquid-collector") {{
			requirements(Category.liquid, with(
				SWItems.iron, 20
			));
		}};

		fogCollector = new SpaciousGenericCrafter("fog-collector") {{
			requirements(Category.production, with(
				SWItems.iron, 20,
				Items.graphite, 35
			));
			size = 3;

			ambientSound = Sounds.wind2;

			outputLiquids = LiquidStack.with(
				Liquids.water, 3f/60f
			);

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawLiquidTile(Liquids.water, 2f),
				new DrawDefault(),
				new DrawNet() {{
					radius = 8f;
					height = 2f;

					netColor = Pal.shadow;
					coverColor = Color.valueOf("9799A3");
				}},
				new DrawIcon()
			);
		}};
	}
}
