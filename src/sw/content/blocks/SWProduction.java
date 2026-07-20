package sw.content.blocks;

import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import mindustry.content.*;
import mindustry.entities.effect.*;
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
import sw.entities.effect.*;
import sw.graphics.*;
import sw.world.blocks.payloads.*;
import sw.world.blocks.production.*;
import sw.world.consumers.*;
import sw.world.draw.*;
import sw.world.interfaces.*;
import sw.world.meta.*;

import static mindustry.type.ItemStack.*;

public class SWProduction {
	public static Block
		mechanicalBore, hydraulicDrill, mechanicalFracker,
		auger, quarry, rig, atmosphericSiphon,
	
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
			scaledHealth = 80f;
			drillTime = 480f;
			tier = 1;
			range = 5;
			ambientSound = Sounds.loopCombustion;
			ambientSoundVolume = 0.01f;
			drillSoundVolume = 0.5f;

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
			scaledHealth = 80f;
			tier = 2;
			drillTime = hardnessDrillMultiplier = 240;
			drillEffect = new WrapEffect(SWFx.groundCrack, Color.white, 8) {
				@Override
				public void create(float x, float y, float rotation, Color color, Object data){
					effect.create(x, y, this.rotation, color, data);
				}
			};
			drillEffectRnd = 0f;
			ambientSound = Sounds.drillCharge;
			drillSoundVolume = 0.5f;
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
			scaledHealth = 80f;
			drillTime = 60f;
			tier = 1;
			range = 5;
			ambientSound = Sounds.loopDrill;
			ambientSoundVolume = 0.14f;
			drillSoundVolume = 0.5f;

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
				new DrawAxles() {{
					rotationOverride = b -> ((HasSpin) b).getRotation();
					for (int i : Mathf.signs) axles.add(Axles.halfBlock.position(-6f, 4f * i, 0f, 1f));
				}},
				new DrawRotated(),
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
			researchCost = with(
				SWItems.verdigris, 1000,
				SWItems.bloom, 120,
				Items.graphite, 800,
				Items.silicon, 1000
			);
			size = 3;
			rotate = true;
			tier = 4;
			drillTime = hardnessDrillMultiplier = 67.5f;
			liquidBoostIntensity = 1f;
			drillMultipliers.put(Items.thorium, 1f / 1.6f);
			drillMultipliers.put(SWItems.iron, 3f / (8f / 3f));

			consume(new ConsumeSpin() {{
				minSpeed = 20f / 10f;
				maxSpeed = 100f / 10f;

				minEfficiency = 0.5f;
				maxEfficiency = 2f;
				showGraph = true;
				
				efficiencyScale = speed -> Mathf.map(speed, 2f, 10f, 0.5f, 2f);
			}});
			
			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawRegion("-rotator", 3, true),
				new DrawAxles() {{
					rotationOverride = b -> ((HasSpin) b).getRotation();
					
					for(int i : Mathf.signs) axles.add(Axles.halfBlock.position(10f * i, 0f, 0f, 1f));
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
		quarry = new MultiAttributeConstructor("quarry") {{
			requirements(Category.production, with(
				SWItems.bloom, 200,
				SWItems.iron, 230,
				Items.graphite, 180,
				Items.silicon, 100,
				Items.thorium, 80
			));
			size = 4;
			configurable = false;
			rotate = false;
			drawArrow = false;
			outputsPayload = false;

			cutShadow = true;
			attributeArea = new Rect().setCentered(1, 1, 2);

			baseAttribute = 0f;
			minAttribute = 4f;
			attributeScl = 0.25f;

			buildSound = Sounds.drillImpact;
			ambientSound = Sounds.drillCharge;

			addRecipes(
				SWAttribute.thoriumRich, SWDefense.thoriumClump
			);
			consumeBuilder.clear();
			consume(new ConsumeSpin() {{
				minSpeed = 90f / 10f;
				maxSpeed = 110f / 10f;

				efficiencyScale = Interp.one;
			}});

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawAxles() {{
					rotationOverride = b -> ((HasSpin) b).getRotation();

					for (Point2 offset : Geometry.d8edge) {
						axles.add(Axles.halfBlock.position(14f * offset.x, 4 * offset.y, 0, 1f));
					}
					for (Point2 offset : Geometry.d8edge) {
						axles.add(Axles.halfBlock.position(4f * offset.x, 14f * offset.y, -90, 1f));
					}
				}},
				new DrawRegion(),
				new DrawParts() {{
					for (int i = 0; i < 8; i++) {
						int finalI = i;
						parts.add(new RegionPart("-nail" + (finalI < 4 ? "-light" : "-dark")) {{
							outline = false;

							x = Angles.trnsx(finalI * 45f, 10.5f * (finalI % 2 != 0 ? Mathf.sqrt2 : 1f));
							y = Angles.trnsy(finalI * 45f, 10.5f * (finalI % 2 != 0 ? Mathf.sqrt2 : 1f));
							rotation = finalI * 45f;

							moveX = Angles.trnsx(finalI * 45f, -4f * (finalI % 2 != 0 ? Mathf.sqrt2 : 1f));
							moveY = Angles.trnsy(finalI * 45f, -4f * (finalI % 2 != 0 ? Mathf.sqrt2 : 1f));

							progress = params -> {
								if (!(params instanceof BlockParams blockParams)) return 0f;
								float a = blockParams.progress;
								if (a <= 0.2f) return 25 * a * a - 10 * a + 1;
								if (a >= 0.9f) return 75 * a * a - 135 * a + 61;
								float s = 0.2f + 7f / 80f * finalI;
								if (a < s) return 0f;
								if (a > s + 7f / 80) return 0.25f;
								return Interp.pow2.apply(80 / 7f * (a - s))/4f;
							};
						}});
					}
				}}
			);

			spinConfig = new SpinConfig() {{
				resistance = 200 / 600f;
				allowedEdges = new int[][]{
					new int[]{0, 1, 4, 5, 8, 9, 12, 13},
				};
			}};
		}};
		rig = new AreaAttributeCrafter("rig") {{
			requirements(Category.production, with(
				SWItems.bloom, 100,
				SWItems.aluminium, 150,
				SWItems.verdigris, 200,
				SWItems.iron, 175,
				Items.silicon, 180
			));
			size = 3;
			rotate = true;

			liquidCapacity = 100f;

			hasAttribute = true;
			requireVisible = true;
			displayEfficiency = true;
			areaRect = new Rect(0, 0, 5, 5);
			attribute = Attribute.oil;
			minEfficiency = 21f;
			baseEfficiency = 0f;
			boostScale = 1f / 21f;

			ambientSound = Sounds.loopGrind;
			ambientSoundVolume = 0.5f;
			ambientSoundPitch = 0.5f;

			consume(new ConsumeSpin() {{
				minSpeed = 2.5f / 10f;
				maxSpeed = 7.5f / 10f;

				efficiencyScale = Interp.one;
			}});
			outputLiquids = LiquidStack.with(Liquids.oil, 50f / 60f, SWLiquids.gas, 100f / 60f);
			liquidOutputDirections = new int[]{1, 3};
			dumpExtraLiquid = false;

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawRegion("-gear", 2),
				new DrawAxles() {{
					rotationOverride = b -> ((HasSpin) b).getRotation();

					axles.add(Axles.tripleBlock.position(8f, 0f, -90f, 1f));
					axles.add(new Axle("-wheel") {{
						pixelWidth = 2;
						pixelHeight = 1;

						circular = true;

						x = 8f;
						y = 0;
						width = 2f;
						height = 8f;
						rotation = -90f;
						spinScl = -1f;

						paletteLight = SWPal.axleLight;
						paletteMedium = SWPal.axleMedium;
						paletteDark = SWPal.axleDark;
					}});
				}},
				new DrawRotated() {{
					layer = Layer.block + 0.01f;
				}},
				new DrawParts() {{
					parts.add(new RegionPart("-driver") {{
						outline = false;

						x = 11f / 4f;
						moveX = -19f / 4f;

						layerOffset = 0.01f;

						progress = DrawParts.spin.add(180f).loop(360f).slope().curve(Interp.sine);
					}});

					parts.add(new RegionPart("-outputs1") {{
						outline = false;
						clampProgress = false;

						growX = -1f;

						layerOffset = 0.01f;

						growProgress = param -> param.rotation / 90 - 1 > 0 && param.rotation / 90 - 1 < 3 ? 1 : 0;
					}});
					parts.add(new RegionPart("-outputs2") {{
						outline = false;
						clampProgress = false;

						growX = -1f;

						layerOffset = 0.01f;

						growProgress = param -> param.rotation / 90 - 1 > 0 && param.rotation / 90 - 1 < 3 ? 0 : 1;
					}});
				}},
				new DrawRotated("-top") {{
					layer = Layer.block + 0.01f;
				}}
			);

			spinConfig = new SpinConfig() {{
				resistance = 400f / 600f;

				allowedEdges = new int[][]{
					new int[]{2, 10},
					new int[]{5, 1},
					new int[]{8, 4},
					new int[]{11, 7}
				};
			}};
		}};
		atmosphericSiphon = new SWGenericCrafter("atmospheric-siphon") {{
			requirements(Category.production, with());
			size = 3;
			envRequired = SWEnv.gasPocket;
			liquidCapacity = 100f;

			updateEffect = new ParticlePillarEffect() {{
				color1 = Color.white;
				color2 = Color.valueOf("E3D8B6");

				lifetime = 60f;

				particles = 2;
				sizeMin = 2f;
				sizeMax = 4f;

				heightInterp = a -> Interp.pow5In.apply(1f - a);
				sizeInterp = a -> Interp.pow2Out.apply(1f - a);
			}};
			updateEffectChance = 0.25f;
			consume(new ConsumeSpin() {{
				minSpeed = 1f;
				maxSpeed = 50f / 10f;

				minEfficiency = 1f;
				maxEfficiency = 2f;

				showGraph = true;
				efficiencyScale = speed -> 1f + Interp.pow2.apply(Mathf.map(speed, 1f, 5f, 0f, 1f));
			}});
			outputLiquids = LiquidStack.with(SWLiquids.gas, 25f / 60f);

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawLiquidTile(SWLiquids.gas, 2f),
				new DrawAxles() {{
					rotationOverride = b -> ((HasSpin) b).getRotation();
					for (Point2 offset : Geometry.d4) axles.add(Axles.quarterBlock.position(11f * offset.x, 11f * offset.y, offset.y == 0 ? 0f : -90f, 1f));
				}},
				new DrawRegion()
			);

			spinConfig = new SpinConfig() {{
				resistance = 40f / 600f;
				allowedEdges = new int[][]{
					new int[]{0, 3, 6, 9}
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
				maxSpeed = 20f;

				minEfficiency = 1f;
				maxEfficiency = 2f;
				showGraph = true;

				efficiencyScale = t -> Mathf.pow(Mathf.map(t, 0.5f, 20f, 0f, 1f), 5f) + 1f;
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
				new DrawAxles() {{
					rotationOverride = b -> ((HasSpin) b).getRotation();

					for(int i : Mathf.signs) axles.add(Axles.halfBlock.position(10f * i, 0f, 0f, 1f));
					axles.add(new Axle("-axle-in") {{
						circular = true;

						pixelWidth = 32;
						pixelHeight = 7;

						x = y = 0;
						width = 8f;
						height = 3.5f;
					}});
				}},
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
