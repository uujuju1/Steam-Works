package sw.content.blocks;

import arc.math.*;
import arc.math.geom.*;
import mindustry.content.*;
import mindustry.entities.part.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import sw.content.*;
import sw.entities.*;
import sw.graphics.*;
import sw.world.blocks.production.*;
import sw.world.consumers.*;
import sw.world.draw.*;
import sw.world.interfaces.*;
import sw.world.meta.*;

import static mindustry.type.ItemStack.*;

public class SWProduction {
	public static Block
		mechanicalBore, hydraulicDrill, mechanicalFracker,
	
		castingOutlet,

		liquidCollector, artesianWell, pumpjack;

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
			researchCost = with(
				SWItems.iron, 40,
				SWItems.verdigris, 80,
				Items.graphite, 70
			);

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
				new DrawRegion("-bottom"),
				new DrawAxles(
					b -> ((HasSpin) b).spinGraph().rotation / ((HasSpin) b).spinGraph().ratios.get((HasSpin) b, 1),
					new Axle("-axle") {{
						pixelWidth = 16;
						pixelHeight = 1;

						x = -6f;
						y = 4;

						width = 4f;
						height = 3.5f;

						paletteLight = SWPal.axleLight;
						paletteMedium = SWPal.axleMedium;
						paletteDark = SWPal.axleDark;
					}},
					new Axle("-axle") {{
						hasIcon = false;

						pixelWidth = 16;
						pixelHeight = 1;

						x = -6f;
						y = -4f;

						width = 4f;
						height = 3.5f;

						paletteLight = SWPal.axleLight;
						paletteMedium = SWPal.axleMedium;
						paletteDark = SWPal.axleDark;
					}}
				),
				new DrawBitmask("-tiles", b -> 0, 64),
				new DrawSpinningDrill() {{
					startAxle = new Axle("-bore-start") {{
						circular = true;

						pixelWidth = 96;
						pixelHeight = 7;

						width = 24f;
						height = 3.5f;
					}};
					middleAxle = new Axle("-bore-middle") {{
						circular = true;

						pixelWidth = 32;
						pixelHeight = 7;

						width = 8f;
						height = 3.5f;
					}};
					endAxle = new Axle("-bore-end") {{
						circular = true;

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
		
		castingOutlet = new SingleItemMultiCrafter("casting-outlet") {{
			requirements(Category.production, with(
			
			));
			rotate = true;
			drawArrow = true;
			
			consume(new ConsumeBuilding(b ->
				b.back() != null &&
				b.back().block == SWStorage.liquidBasin &&
				b.back().liquids != null &&
				b.back().liquids.get(Liquids.slag) > 0
			));
			consumeLiquid(Liquids.slag, 10f / 60f);
			
			outputItems = with(
				SWItems.verdigris, 1,
				SWItems.iron, 2
			);
			
			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawLiquidTile(Liquids.slag, 2f),
				new DrawDefault(),
				new DrawRegion("-top")
			);
			
			spinConfig.hasSpin = false;
		}};

		liquidCollector = new Pump("liquid-collector") {{
			requirements(Category.liquid, with(
				SWItems.iron, 20
			));
			researchCost = with(
				SWItems.iron, 40
			);
		}};
		artesianWell = new SWGenericCrafter("artesian-well") {{
			requirements(Category.production, with(
				SWItems.iron, 20,
				Items.graphite, 35
			));
			researchCost = with(
				SWItems.iron, 40,
				Items.graphite, 70
			);
			size = 3;
			rotate = true;

			consumeSpin(new ConsumeRotation() {{
				startSpeed = 0.5f;
				endSpeed = 35f;
				curve = t -> Mathf.pow(t, 20f) + 1f;
			}});

			hasAttribute = true;
			displayEfficiency = true;
			attribute = Attribute.water;
			baseEfficiency = 0f;
			boostScale = 1f / 9f;
			maxBoost = 2f;
			minEfficiency = 1f;

			outputLiquids = LiquidStack.with(
				Liquids.water, 3f/60f
			);

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawLiquidTile(Liquids.water, 3f) {{
					alpha = 0.75f;
				}},
				new DrawParts() {{
					for (int i : Mathf.signs) parts.add(
						new RegionPart("-rotator") {{
							outline = false;

							x = 0f;
							y = 2f * i;

							moveRot = 360f * i;

							progress = p -> p.recoil / 360f % 1f;

							moves.add(new PartMove(PartProgress.charge.clamp().curve(Interp.circleOut), 0f, 3f * i, 0f));
						}}
					);
				}},
				new DrawAxles(
					b -> ((HasSpin) b).spinGraph().rotation / ((HasSpin) b).spinGraph().ratios.get((HasSpin) b, 1),
					new Axle("-axle-in") {{
						iconOverride = "sw-artesian-well-axle-icon";
						circular = true;

						pixelWidth = 32;
						pixelHeight = 7;

						x = y = 0;
						width = 8f;
						height = 3.5f;
					}},
					new Axle("-axle-out") {{
						hasIcon = false;

						pixelWidth = 32;
						pixelHeight = 1;

						x = 8f;
						y = 0f;
						width = 8f;
						height = 3.5f;

						paletteLight = SWPal.axleLight;
						paletteMedium = SWPal.axleMedium;
						paletteDark = SWPal.axleDark;
					}},
					new Axle("-axle-out") {{
						hasIcon = false;

						pixelWidth = 32;
						pixelHeight = 1;

						x = -8f;
						y = 0f;
						width = 8f;
						height = 3.5f;

						paletteLight = SWPal.axleLight;
						paletteMedium = SWPal.axleMedium;
						paletteDark = SWPal.axleDark;
					}}
				),
				new DrawBitmask("-tiles", b -> 0, 96)
			);

			spinConfig = new SpinConfig() {{
				topSpeed = 1f;
				resistance = 0.25f/60f;
				allowedEdges = new int[][]{
					new int[]{0, 6},
					new int[]{3, 9},
					new int[]{6, 0},
					new int[]{9, 3}
				};
			}};
		}};
		pumpjack = new SWGenericCrafter("pumpjack") {{
			requirements(Category.production, with(
				SWItems.verdigris, 25,
				SWItems.iron, 30
			));
			
			size = 3;
			hasAttribute = true;
			displayEfficiency = true;
			attribute = Attribute.heat;
			baseEfficiency = 0f;
			boostScale = 1f / 9f;
			maxBoost = 1f;
			minEfficiency = 9f;
			
			liquidCapacity = 90;
			outputLiquids = LiquidStack.with(
				Liquids.slag, 18f/60f
			);
			
			drawer = new DrawMulti(
				new DrawDefault(),
				new DrawGlowRegion() {{
					layer = -1f;
					color = Pal.accent;
					glowIntensity = 0.3f;
					glowScale = 20f;
				}},
				new DrawParts() {{
					parts.add(
						new RegionPart("-gear") {{
							outline = false;
							progress = PartProgress.smoothReload.loop(960f);
							moveRot = 360f;
						}},
						new RegionPart("-top") {{
							outline = false;
							clampProgress = false;
							growProgress = PartProgress.smoothReload.loop(180f).curve(Interp.slope).curve(Interp.swing);
							growX = growY = 0.125f;
						}},
						new RegionPart("-top-top") {{
							outline = false;
							clampProgress = false;
							growProgress = PartProgress.smoothReload.add(45f).loop(180f).curve(Interp.slope).curve(Interp.swing);
							growX = growY = 0.125f;
						}}
					);
				}}
			);
			
			spinConfig = new SpinConfig() {{
				hasSpin = false;
			}};
		}};
	}
}
