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
		auger,
	
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
			ambientSound = Sounds.loopCombustion;
			ambientSoundVolume = 0.01f;

			useAllowList = true;
			drillMultipliers.put(Items.graphite, 1);

			consumeLiquid(SWLiquids.solvent, 0.025f).boost();
			optionalBoostIntensity = 2;

			drawer = new DrawMulti(new DrawRegion(), new DrawRangedDrill());
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
			ambientSound = Sounds.loopDrill;
			ambientSoundVolume = 0.14f;

			useAllowList = true;
			drillMultipliers.put(Items.sand, 1f);
			drillMultipliers.put(Items.silicon, 0.5f);

			drillEffect = SWFx.blockCrack;

			consume(new ConsumeSpin() {{
				minSpeed = 0.75f;
				maxSpeed = 1.25f;
				efficiencyScale = Interp.one;
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
				resistance = 0.5f/60f;
				allowedEdges = new int[][]{
					new int[]{4, 5},
					new int[]{6, 7},
					new int[]{0, 1},
					new int[]{2, 3}
				};
			}};
		}};
		auger = new SWDrill("auger") {{
			requirements(Category.production, with(
				SWItems.verdigris, 100,
				SWItems.bloom, 120,
				Items.graphite, 80,
				Items.silicon, 100
			));
			size = 3;
			rotate = true;
			tier = 4;
			drillTime = hardnessDrillMultiplier = 67.5f;
			liquidBoostIntensity = 1f;
			
			consume(new ConsumeSpin() {{
				minSpeed = 20f / 10f;
				maxSpeed = 100f / 10f;
				
				efficiencyScale = speed -> Mathf.map(speed, 2f, 10f, 0.5f, 2f);
			}});
			
			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawRegion("-rotator", 3, true),
				new DrawAxles() {{
					rotationOverride = b -> ((HasSpin) b).getRotation();
					
					for(int i : Mathf.signs) {
						axles.add(new Axle("-axle") {{
							pixelWidth = 4;
							pixelHeight = 1;
							
							x = 10f * i;
							y = 0;
							
							width = 4f;
							height = 3.5f;
							
							paletteLight = SWPal.axleLight;
							paletteMedium = SWPal.axleMedium;
							paletteDark = SWPal.axleDark;
						}});
					}
				}},
				new DrawFacingLightRegion(),
				new DrawRegion("-top")
			);
			
			spinConfig = new SpinConfig() {{
				resistance = 20/600f;
				
				allowedEdges = new int[][]{
					new int[]{0, 6},
					new int[]{3, 9},
					new int[]{6, 0},
					new int[]{9, 3}
				};
			}};
		}};
		
		castingOutlet = new SingleItemMultiCrafter("casting-outlet") {{
			requirements(Category.production, with(
				SWItems.iron, 20,
				Items.graphite, 10
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
				SWItems.iron, 2,
				SWItems.aluminium, 1
			);
			
			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawLiquidTile(Liquids.slag, 2f),
				new DrawDefault(),
				new DrawRegion("-top")
			);
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

			consume(new ConsumeSpin() {{
				minSpeed = 0.5f;
				maxSpeed = 35f;
				efficiencyScale = t -> Mathf.pow(Mathf.map(t, 0.5f, 35.5f, 0f, 1f), 20f) + 1f;
			}});

			hasAttribute = true;
			displayEfficiency = true;
			attribute = Attribute.water;
			baseEfficiency = 0f;
			boostScale = 1f / 9f;
			maxBoost = 4f;
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
							y = 7f * i;
							yScl = -i;

							moveRot = -360f * i;
							
							clampProgress = false;

							progress = DrawParts.spin.loop(360f).mul(p -> {
								float r = (p.rotation / 90) - 1f;
								return (r == 1 || r == 2) ? -1f : 1f;
							});
							
							moves.add(new PartMove(p -> {
								float r = (p.rotation / 90) - 1f;
								return (r == 1 || r == 2) ? 1f : 0f;
							}, 0, 0, 0, i * 2f, 0));
						}}
					);
				}},
				new DrawAxles(
					b -> ((HasSpin) b).getRotation(),
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
				new DrawFacingLightRegion()
			);

			spinConfig = new SpinConfig() {{
				resistance = 2.5f/600f;
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
		}};
	}
}
