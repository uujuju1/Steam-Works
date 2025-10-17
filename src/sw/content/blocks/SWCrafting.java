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

public class SWCrafting {
	public static Block
		cokeOven, engineSmelter, waterBallMill,
		
		blastFurnace, wedger, pyrolysisSynthetizer, oxidationPlant, pressureKiln,
		
		crystalFurnace, rte, kitchenGarden;
	
	public static Block compoundSmelter;
	public static Block densePress;
	
	public static void load() {
//		compoundSmelter = new SWGenericCrafter("compound-smelter") {{
//			requirements(Category.crafting, BuildVisibility.hidden, with(
//				SWItems.iron, 80,
//				SWItems.verdigris, 200,
//				Items.silicon, 150,
//				Items.graphite, 160
//			));
//			size = 3;
//			health = 200;
//
//			ambientSound = Sounds.torch;
//			craftTime = 30f;
//			craftEffect = SWFx.compoundCraft;
//
//			drawer = new DrawMulti(
//				new DrawDefault(),
//				new DrawFlame(Color.valueOf("BEB5B2")) {{
//					flameRadiusInMag = flameRadiusMag = 5f;
//					flameRadius = 5;
//					flameRadiusIn = 2.5f;
//				}}
//			);
//
//			consumeItems(with(
//				Items.silicon, 1,
//				SWItems.verdigris, 2
//			));
//			consumeLiquid(SWLiquids.solvent, 0.1f);
//
//			outputItem = new ItemStack(SWItems.compound, 1);
//		}};
//
//		densePress = new SWGenericCrafter("dense-press") {{
//			requirements(Category.crafting, BuildVisibility.hidden, with(
//				SWItems.iron, 160,
//				SWItems.verdigris, 200,
//				Items.silicon, 80,
//				Items.graphite, 160
//			));
//			size = 3;
//			health = 200;
//
//			ambientSound = Sounds.grinding;
//			craftTime = 60f;
//			craftEffect = SWFx.denseAlloyCraft;
//			craftSound = Sounds.dullExplosion;
//			craftSoundVolume = 0.1f;
//
//			consumeItems(with(
//				Items.silicon, 2,
//				SWItems.iron, 2
//			));
//			consumeLiquid(SWLiquids.solvent, 0.1f);
//
//			outputItem = new ItemStack(SWItems.denseAlloy, 2);
//		}};

		cokeOven = new StackableGenericCrafter("coke-oven") {{
			requirements(Category.crafting, with(
				SWItems.iron, 50,
				SWItems.verdigris, 60,
				Items.graphite, 45,
				Items.silicon, 30
			));
			researchCost = with(
				SWItems.iron, 100,
				SWItems.verdigris, 120,
				Items.graphite, 85,
				Items.silicon, 60
			);
			size = 3;
			health = 240;

			craftTime = 180f;

			connectEdge = new boolean[]{true, false, true, false};

			ambientSound = Sounds.smelter;
			updateEffectStatic = SWFx.cokeBurn;
			updateEffectChance = 0.1f;
			craftEffect = SWFx.cokeCraft;

			consumeItems(with(Items.graphite, 2));
			consumeLiquid(Liquids.water, 0.1f);
			outputItems = with(SWItems.coke, 1);

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawParticles() {{
					particles = 15;

					color = Pal.turretHeat;

					blending = Blending.additive;

					particleInterp = Interp.linear;

					particleRad = 5f;
					particleSize = 3f;
				}
				},
				new DrawParticles() {{
					particles = 15;

					color = Pal.turretHeat;

					blending = Blending.additive;

					particleInterp = Interp.one;

					particleRad = 8f;
					particleSize = 3f;
				}
				},
				new DrawBitmask("-tiles", b -> {
					Point2[] edges = Edges.getEdges(3);
					int offset = Mathf.floor((size - 1f)/2f);
					Building other = null;

					int out = 0;
					for (int pos = 0; pos < size; pos++) {
						Building nearby = b.nearby(
							edges[Mathf.mod(pos - offset, edges.length)].x,
							edges[Mathf.mod(pos - offset, edges.length)].y
						);

						if (pos == 0) {
							other = nearby;

							if (other == null) break;
						} else if (nearby == other) {
							if (pos == size - 1) out |= 1;
						} else break;
					}
					other = null;
					for (int pos = 0; pos < size; pos++) {
						Building nearby = b.nearby(
							edges[Mathf.mod(pos - offset + size * 2, edges.length)].x,
							edges[Mathf.mod(pos - offset + size * 2, edges.length)].y
						);

						if (pos == 0) {
							other = nearby;

							if (other == null) break;
						} else if (nearby == other) {
							if (pos == size - 1) out |= 2;
						} else break;
					}
					return out;
				}, 96),
				new DrawGlowRegion() {{
					color = Pal.turretHeat;

					glowScale = 5f;
					glowIntensity = 0.1f;
				}
				}
			);
		}};
		engineSmelter = new SWGenericCrafter("engine-smelter") {{
			requirements(Category.crafting, with(
				SWItems.iron, 40,
				SWItems.verdigris, 40,
				Items.graphite, 35
			));
			researchCost = with(
				SWItems.iron, 80,
				SWItems.verdigris, 80,
				Items.graphite, 70
			);
			size = 3;
			health = 240;

			ambientSound = Sounds.smelter;
			ambientSoundVolume = 0.1f;

			craftEffect = updateEffectStatic = SWFx.combust;
			updateEffectChance = 0.15f;
			craftTime = 30f;

			researchConsumers = false;
			consumeItems(with(Items.sand, 1));
			consumeLiquid(SWLiquids.solvent, 0.1f);
			consume(new ConsumeRotation() {{
				startSpeed = 0.5f;
				endSpeed = 1f;
				curve = Interp.one;
			}});
			outputItems = with(Items.silicon, 1);

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawAxles(
					b -> ((HasSpin) b).spinGraph().rotation / ((HasSpin) b).spinGraph().ratios.get((HasSpin) b, 1),
					new Axle("-axle") {{
						x = 8f;
						y = 0f;

						height = 3.5f;

						pixelHeight = 7;

						paletteLight = SWPal.axleLight;
						paletteMedium = SWPal.axleMedium;
						paletteDark = SWPal.axleDark;
					}},
					new Axle("-axle") {{
						x = 0f;
						y = 8f;

						height = 3.5f;

						pixelHeight = 7;

						paletteLight = SWPal.axleLight;
						paletteMedium = SWPal.axleMedium;
						paletteDark = SWPal.axleDark;

						rotation = -90f;

						hasIcon = false;
					}},
					new Axle("-axle") {{
						x = -8f;
						y = 0f;

						height = 3.5f;

						pixelHeight = 7;

						paletteLight = SWPal.axleLight;
						paletteMedium = SWPal.axleMedium;
						paletteDark = SWPal.axleDark;

						hasIcon = false;
					}},
					new Axle("-axle") {{
						x = 0f;
						y = -8f;

						height = 3.5f;

						pixelHeight = 7;

						paletteLight = SWPal.axleLight;
						paletteMedium = SWPal.axleMedium;
						paletteDark = SWPal.axleDark;

						rotation = -90f;

						hasIcon = false;
					}}
				),
				new DrawDefault()
			);

			spinConfig = new SpinConfig() {{
				topSpeed = 1f;
				resistance = 1f/60f;

				allowedEdges = new int[][]{
					new int[]{0, 3, 6, 9},
					new int[]{0, 3, 6, 9},
					new int[]{0, 3, 6, 9},
					new int[]{0, 3, 6, 9}
				};
			}};
		}};
		waterBallMill = new SWGenericCrafter("water-ball-mill") {{
			requirements(Category.crafting, with(
				SWItems.iron, 30,
				SWItems.aluminium, 20,
				Items.silicon, 30
			));
			size = 3;
			health = 240;
			rotate = true;

			craftTime = 180f;

			regionRotated1 = 4;
			liquidOutputDirections = new int[]{1, 3};

			consume(new ConsumeRotation() {{
				startSpeed = 15f / 10f;
				endSpeed = 25f / 10f;
				curve = Interp.one;
			}});
			consumeLiquid(Liquids.water, 3f/60f);
			outputLiquids = LiquidStack.with(Liquids.ozone, 1f/60f, Liquids.hydrogen, 6f/60f);

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawLiquidTile(Liquids.water, 2f),
				new DrawParticles() {{
					color = Color.valueOf("D1EFFF");
					
					particleRad = 1f;
					particleInterp = a -> 6f + Mathf.sin(2f * Mathf.PI * a, 1, 2f);
					
					rotateScl = 1f;
				}},
				new DrawParticles() {{
					color = Color.valueOf("FFCBDD");
					
					particleRad = 1f;
					particleInterp = a -> 6f + Mathf.sin(2f * Mathf.PI * a, 1, 2f);
					particleSize = 1.5f;
					
					rotateScl = -1f;
				}},
				new DrawAxles(
					b -> ((HasSpin) b).getRotation(),
					new Axle("-axle") {{
						pixelWidth = 16;
						pixelHeight = 1;
						
						x = 10f;
						y = 0f;
						
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
						
						x = 0f;
						y = 10f;
						rotation = -90;
						
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
						
						x = -10f;
						y = 0f;
						
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
						
						x = 0f;
						y = -10f;
						rotation = -90f;
						
						width = 4f;
						height = 3.5f;
						
						paletteLight = SWPal.axleLight;
						paletteMedium = SWPal.axleMedium;
						paletteDark = SWPal.axleDark;
					}}
				),
				new DrawRegion(),
				new DrawLiquidOutputs(),
				new DrawRegion("-mill", 10f),
				new DrawRegion("-top")
			);
			
			spinConfig = new SpinConfig() {{
				resistance = 20f / 600f;
				
				topSpeed = 20f / 10f;
				
				allowedEdges = new int[][]{
					new int[]{0, 3, 6, 9},
					new int[]{0, 3, 6, 9},
					new int[]{0, 3, 6, 9},
					new int[]{0, 3, 6, 9},
				};
			}};
		}};

//		blastFurnace = new GenericCrafter("blast-furnace") {{
//			requirements(Category.crafting, BuildVisibility.hidden, with(
//			));
//			size = 3;
//			health = 240;
//
//			craftTime = 120f;
//
//			consumeItems(with(SWItems.iron, 2, SWItems.coke, 1));
//			outputItems = with(SWItems.steel, 1);
//
//			drawer = new DrawMulti(
//				new DrawDefault()
//			);
//		}};
//		pressureKiln = new GenericCrafter("pressure-kiln") {{
//			requirements(Category.crafting, BuildVisibility.hidden, with(
//			));
//			size = 3;
//			health = 240;
//
//			craftTime = 180f;
//
//			consumeItems(with(SWItems.steel, 1, Items.sand, 2));
//			consumeLiquids(LiquidStack.with(Liquids.hydrogen, 3f/60f));
//			outputItems = with(SWItems.denseAlloy, 2);
//
//			drawer = new DrawMulti(
//				new DrawDefault()
//			);
//		}};
//		wedger = new GenericCrafter("wedger") {{
//			requirements(Category.crafting, BuildVisibility.hidden, with(
//			));
//			size = 3;
//			health = 240;
//
//			craftTime = 180f;
//
//			consumeItems(with(SWItems.oxycarbide, 2, SWItems.iron, 2));
//			outputItems = with(SWItems.compound, 2);
//
//			drawer = new DrawMulti(
//				new DrawDefault()
//			);
//		}};
//		pyrolysisSynthetizer = new GenericCrafter("pyrolysis-synthetizer") {{
//			requirements(Category.crafting, BuildVisibility.hidden, with(
//			));
//			size = 3;
//			health = 240;
//
//			craftTime = 180f;
//
//			consumeItems(with(SWItems.coke, 1, Items.silicon, 1));
//			consumeLiquid(Liquids.ozone, 1f/60f);
//			outputItems = with(SWItems.oxycarbide, 1);
//
//			drawer = new DrawMulti(
//				new DrawDefault(),
//				new DrawFlame() {{
//					flameRadius = 5f;
//				}}
//			);
//		}};
//		oxidationPlant = new GenericCrafter("oxidation-plant") {{
//			requirements(Category.crafting, BuildVisibility.hidden, with(
//			));
//			size = 3;
//			health = 240;
//
//			craftTime = 180f;
//
//			consumeItems(with(SWItems.iron, 2, SWItems.aluminium, 1));
//			consumeLiquid(Liquids.ozone, 2f/60f);
//			outputItems = with(SWItems.thermite, 3);
//
//			drawer = new DrawMulti(
//				new DrawDefault()
//			);
//		}};
//
//		rte = new GenericCrafter("rte") {{
//			requirements(Category.crafting, BuildVisibility.hidden, with(
//			));
//			size = 3;
//			health = 240;
//
//			craftTime = 180f;
//
//			consumeItem(Items.thorium, 2);
//			consumeLiquid(Liquids.water, 0.2f);
//			outputItems = with(Items.silicon, 3);
//			outputLiquids = LiquidStack.with(SWLiquids.steam, 0.2f);
//
//			drawer = new DrawMulti(
//				new DrawDefault(),
//				new DrawFlame() {{
//					flameRadius = 5f;
//				}}
//			);
//		}};
//		crystalFurnace = new GenericCrafter("crystal-furnace") {{
//			requirements(Category.crafting, BuildVisibility.hidden, with(
//			));
//			size = 3;
//			health = 240;
//
//			craftTime = 60f;
//
//			consumeItems(with(Items.lead, 1, Items.sand, 2));
//			outputItems = with(Items.metaglass, 2);
//
//			drawer = new DrawMulti(
//				new DrawDefault()
//			);
//		}};
//		kitchenGarden = new GenericCrafter("kitchen-garden") {{
//			requirements(Category.crafting, BuildVisibility.hidden, with(
//			));
//			size = 3;
//			health = 240;
//
//			craftTime = 180f;
//
//			consumeLiquids(LiquidStack.with(Liquids.water, 0.1f, SWLiquids.primordialSoup, 0.1f));
//			outputLiquids = LiquidStack.with(SWLiquids.primordialSoup, 0.2f);
//
//			drawer = new DrawMulti(
//				new DrawDefault()
//			);
//		}};
	}
}
