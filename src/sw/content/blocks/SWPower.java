package sw.content.blocks;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import mindustry.content.*;
import mindustry.entities.part.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import sw.content.*;
import sw.entities.*;
import sw.graphics.*;
import sw.world.blocks.payloads.*;
import sw.world.blocks.power.*;
import sw.world.blocks.production.*;
import sw.world.draw.*;
import sw.world.interfaces.*;
import sw.world.meta.*;

import static mindustry.type.ItemStack.*;

public class SWPower {
	public static Block
		handWheel, evaporator, waterWheel,
	
		wireShaft, wireShaftRouter, shaftGearbox,
		overheadBelt, largeOverheadBelt,
		flywheel, clutch,
	
		hydraulicFlywheel,
		winder, latch,

		shaftTransmission, mechanicalGovernor;

	public static void load() {
		handWheel = new HandCrank("hand-wheel") {{
			requirements(Category.power, with(
				Items.graphite, 15,
				SWItems.verdigris, 10
			));
			
			torque = 10/600f;
			speed = 10/10f;
			spinTime = 600f;
			
			spinConfig = new SpinConfig() {{
				allowedEdges = new int[][]{
					new int[]{0},
					new int[]{1},
					new int[]{2},
					new int[]{3}
				};
			}};
			
			drawer = new DrawMulti(
				new DrawBitmask("-tiles", b -> 0),
				new DrawRegion("-wheel", 1f, true)
			);
		}};
		evaporator = new SWGenericCrafter("evaporator") {{
			Block self = this;
			
			requirements(Category.power, with(
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
			ambientSound = Sounds.wind;
			ambientSoundVolume = 0.1f;
			
			spinConfig = new SpinConfig() {{
				connectors.add(self);
			}};
			
			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawLiquidTile(SWLiquids.solvent) {{
					alpha = 0.5f;
				}},
				new DrawDefault(),
				new DrawRegion("-blade") {{
					rotateSpeed = 5f;
				}},
				new DrawRegion("-blade") {{
					rotateSpeed = 5f;
					rotation = 30f;
				}},
				new DrawRegion("-blade") {{
					rotateSpeed = 5f;
					rotation = 60f;
				}},
				new DrawRegion("-top"),
				new DrawIcon()
			);
			
			updateEffect = SWFx.evaporate;
			updateEffectSpread = 0f;
			updateEffectChance = 0.5f;
			
			consumeLiquid(SWLiquids.solvent, 1f/60f);
			
			outputRotation = 1f;
			outputRotationForce = 1f/60f;
		}};
		waterWheel = new FrontAttributeCrafter("water-wheel") {{
			requirements(Category.power, with(
				SWItems.iron, 10,
				SWItems.aluminium, 15
			));
			rotate = true;
			drawArrow = true;
			hasAttribute = true;
			
			attribute = Attribute.water;
			frontAttribute = SWAttribute.gravity;
			baseEfficiency = 0f;
			minEfficiency = 1f;
			
			spinConfig = new SpinConfig() {{
				allowedEdges = new int[][]{
					new int[]{1, 3},
					new int[]{2, 0},
					new int[]{3, 1},
					new int[]{0, 2}
				};
			}};
			
			drawer = new DrawMulti(
				new DrawAxles(
					building -> ((HasSpin) building).getRotation() * (building.rotation > 1 ? -1f : 1f),
					Layer.block + 0.01f,
					new Axle("-axle") {{
						circular = true;
						
						pixelHeight = 1;
						height = 3.5f;
						
						rotation = -90f;
					}}
				),
				new DrawBitmask("-tiles", building -> 0, 32, Layer.block + 0.01f),
				new DrawFlaps() {{
					rotationFunc = building -> ((HasSpin) building).getRotation();
					layer = Layer.block + 0.01f;
					layerOffset = 0.01f;
					
					width = height = 8f;
					rotation = 180f;
					
					radius = 3f;
					zScale = 2f;
					
					blades = 6;
				}},
				new DrawIcon()
			);
			
			outputRotation = 2f;
			outputRotationForce = 0.25f/60f;
		}};
		
		wireShaft = new AxleBlock("wire-shaft") {{
			requirements(Category.power, with(
				SWItems.verdigris, 1,
				Items.graphite, 1,
				Items.silicon, 2
			));

			spinConfig = new SpinConfig() {{
				resistance = 1f/600f;
				allowedEdges = new int[][]{
					new int[]{0, 2},
					new int[]{1, 3},
					new int[]{2, 0},
					new int[]{3, 1}
				};
			}};

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawAxles(
					new Axle("-axle") {{
						pixelHeight = 1;
						height = 3.5f;

						paletteLight = SWPal.axleLight;
						paletteMedium = SWPal.axleMedium;
						paletteDark = SWPal.axleDark;
					}}
				),
				new DrawBitmask("-tiles", build -> {
					int tiling = 0;
					if (!rotate) return tiling;
					if (build.front() instanceof HasSpin gas && HasSpin.connects((HasSpin) build, gas.getSpinGraphDestination((HasSpin) build))) tiling |= 1;
					if (build.back() instanceof HasSpin gas && HasSpin.connects((HasSpin) build, gas.getSpinGraphDestination((HasSpin) build))) tiling |= 2;
					return tiling;
				})
			);
		}};
		wireShaftRouter = new AxleBlock("wire-shaft-router") {{
			requirements(Category.power, with(
				SWItems.verdigris, 2,
				Items.graphite, 2,
				Items.silicon, 4
			));

			spinConfig = new SpinConfig() {{
				resistance = 2f/600f;
				allowedEdges = new int[][]{
					new int[]{0, 1, 3},
					new int[]{1, 2, 0},
					new int[]{2, 3, 1},
					new int[]{3, 0, 2}
				};
			}};

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawAxles(
					new Axle("-axle") {{
						pixelHeight = 1;

						height = 3.5f;

						rotation = -90f;

						paletteLight = SWPal.axleLight;
						paletteMedium = SWPal.axleMedium;
						paletteDark = SWPal.axleDark;
					}},
					new Axle("-axle-middle") {{
						hasIcon = false;

						pixelWidth = 16;
						pixelHeight = 1;

						x = 2f;

						width = 4f;
						height = 3.5f;

						paletteLight = SWPal.axleLight;
						paletteMedium = SWPal.axleMedium;
						paletteDark = SWPal.axleDark;
					}}
				),
				new DrawBitmask("-tiles", b -> 0)
			);
		}};
		shaftGearbox = new AxleBlock("shaft-gearbox") {{
			requirements(Category.power, with(
				SWItems.verdigris, 5,
				Items.graphite, 5,
				Items.silicon, 10
			));
			size = 2;

			spinConfig = new SpinConfig() {{
				resistance = 2f/600f;
				allowedEdges = new int[][]{
					new int[]{0, 1, 4, 5},
					new int[]{2, 3, 6, 7},
					new int[]{4, 5, 0, 1},
					new int[]{6, 7, 2, 3}
				};
			}};

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawAxles(
					new Axle("-shaft") {{
						circular = true;

						pixelWidth = 32;
						pixelHeight = 7;

						y = 4f;

						width = 8f;
						height = 3.5f;
					}},
					new Axle("-shaft") {{
						circular = true;

						pixelWidth = 32;
						pixelHeight = 7;

						y = -4f;

						width = 8f;
						height = 3.5f;
					}},
					new Axle("-shaft-out") {{
						pixelWidth = 64;
						pixelHeight = 7;

						y = 4f;

						width = 16f;
						height = 3.5f;

						paletteLight = SWPal.axleLight;
						paletteMedium = SWPal.axleMedium;
						paletteDark = SWPal.axleDark;
					}},
					new Axle("-shaft-out") {{
						pixelWidth = 64;
						pixelHeight = 7;

						y = -4f;

						width = 16f;
						height = 3.5f;

						paletteLight = SWPal.axleLight;
						paletteMedium = SWPal.axleMedium;
						paletteDark = SWPal.axleDark;
					}},
					new Axle("-shaft-middle") {{
						iconOverride = "sw-shaft-gearbox-shaft-icon";

						circular = true;

						pixelHeight = 9;
						pixelWidth = 16;

						spinScl = -1f;

						width = 4f;
						height = 4.5f;
					}}
				),
				new DrawBitmask("-tiles", build -> 0) {{
					tileWidth = tileHeight = 64;
				}}
			);
		}};
		overheadBelt = new AxleBridge("overhead-belt") {{
			requirements(Category.power, with(
				SWItems.verdigris, 5,
				Items.graphite, 5,
				Items.silicon, 10
			));

			spinConfig = new SpinConfig() {{
				resistance = 3f/600f;
				allowedEdges = new int[][]{
					new int[]{0},
					new int[]{1},
					new int[]{2},
					new int[]{3}
				};
			}};

			spinScl = 1/8f;
			stroke = 0.75f;
			spacing = 3;

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawAxles(new Axle("-axle") {{
					x = 2;
					y = 0;

					width = 4f;
					height = 3.5f;

					pixelWidth = 16;
					pixelHeight = 1;

					paletteLight = SWPal.axleLight;
					paletteMedium = SWPal.axleMedium;
					paletteDark = SWPal.axleDark;
				}}),
				new DrawBitmask("-tiles", b -> 0)
			);
		}};
		largeOverheadBelt = new AxleBridge("large-overhead-belt") {{
			requirements(Category.power, with(
				SWItems.verdigris, 15,
				Items.graphite, 25,
				Items.silicon, 20,
				SWItems.aluminium, 30
			));
			size = 2;
			
			radius = 6f;
			range = 80f;
			ratioScl = 2f;

			spinConfig = new SpinConfig() {{
				resistance = 6f/600f;
				allowedEdges = new int[][]{
					new int[]{0, 1},
					new int[]{2, 3},
					new int[]{4, 5},
					new int[]{6, 7}
				};
			}};

			spinScl = 1/4f;
			stroke = 0.75f;
			spacing = 3;

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawAxles(
					new Axle("-axle") {{
						pixelWidth = 16;
						pixelHeight = 1;
						
						x = 0f;
						y = -4f;
						
						width = 16f;
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
						y = 4f;
						
						width = 16f;
						height = 3.5f;
						
						paletteLight = SWPal.axleLight;
						paletteMedium = SWPal.axleMedium;
						paletteDark = SWPal.axleDark;
					}}
				),
				new DrawBitmask("-tiles", b -> 0, 64)
			);
		}};
		flywheel = new AxleBlock("flywheel") {{
			requirements(Category.power, with(
				SWItems.steel, 200,
				SWItems.aluminium, 50,
				SWItems.verdigris, 100,
				Items.silicon, 30,
				Items.graphite, 40
			));
			
			size = 2;
			
			spinConfig = new SpinConfig() {{
				resistance = 10f/600f;
				inertia = 25f;
				
				allowedEdges = new int[][]{
					new int[]{0, 1, 2, 7},
					new int[]{1, 2, 3, 4},
					new int[]{3, 4, 5, 6},
					new int[]{5, 6, 7, 0}
				};
			}};
			
			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawAxles(
					new Axle("-axle") {{
						hasIcon = false;
						
						pixelWidth = 2;
						pixelHeight = 1;
						
						width = 2f;
						height = 3.5f;
						
						x = 4f;
						y = 7f;
						rotation = -90f;
						
						paletteLight = SWPal.axleLight;
						paletteMedium = SWPal.axleMedium;
						paletteDark = SWPal.axleDark;
					}},
					new Axle("-axle") {{
						pixelWidth = 2;
						pixelHeight = 1;
						
						width = 2f;
						height = 3.5f;
						
						x = 7f;
						y = 4f;
						
						paletteLight = SWPal.axleLight;
						paletteMedium = SWPal.axleMedium;
						paletteDark = SWPal.axleDark;
					}},
					new Axle("-axle") {{
						hasIcon = false;
						
						pixelWidth = 2;
						pixelHeight = 1;
						
						width = 2f;
						height = 3.5f;
						
						x = 7f;
						y = -4f;
						
						paletteLight = SWPal.axleLight;
						paletteMedium = SWPal.axleMedium;
						paletteDark = SWPal.axleDark;
					}},
					new Axle("-axle") {{
						hasIcon = false;
						
						pixelWidth = 2;
						pixelHeight = 1;
						
						width = 2f;
						height = 3.5f;
						
						x = 4f;
						y = -7f;
						rotation = -90f;
						
						paletteLight = SWPal.axleLight;
						paletteMedium = SWPal.axleMedium;
						paletteDark = SWPal.axleDark;
					}}
				),
				new DrawBitmask("-tiles", b -> 0, 64),
				new DrawRegion("-wheel", 1) {{
					layer = Layer.power;
				}}
			) {
				@Override public TextureRegion[] icons(Block block) {
					return new TextureRegion[]{Core.atlas.find(block.name + "-icon")};
				}
			};
		}};
		clutch = new AxleClutch("clutch") {{
			requirements(Category.power, with(
				SWItems.aluminium, 25,
				SWItems.verdigris, 20,
				Items.silicon, 10,
				Items.graphite, 50
			));
			
			clutchStrength = 20f/600f;
			
			spinConfig = new SpinConfig() {{
				allowedEdges = new int[][]{
					new int[]{0, 2},
					new int[]{1, 3},
					new int[]{2, 0},
					new int[]{3, 1}
				};
			}};
			
			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawAxles(
					b -> b.back() instanceof HasSpin spin ? spin.getRotation() : 0f,
					new Axle("-axle") {{
						pixelWidth = 3;
						pixelHeight = 1;
						
						x = -2.5f;
						y = 0f;
						
						width = 3f;
						height = 3.5f;
						
						paletteLight = SWPal.axleLight;
						paletteMedium = SWPal.axleMedium;
						paletteDark = SWPal.axleDark;
					}}
				),
				new DrawAxles(
					b -> b.front() instanceof HasSpin spin ? spin.getRotation() : 0f,
					new Axle("-axle") {{
						pixelWidth = 3;
						pixelHeight = 1;
						
						x = 2.5f;
						y = 0f;
						
						width = 3f;
						height = 3.5f;
						
						paletteLight = SWPal.axleLight;
						paletteMedium = SWPal.axleMedium;
						paletteDark = SWPal.axleDark;
					}}
				),
				new DrawBitmask("-tiles", build -> 0)
			);
		}};

		shaftTransmission = new ShaftTransmission("shaft-transmission") {{
			requirements(Category.power, with(
				SWItems.verdigris, 50,
				SWItems.iron, 40,
				Items.silicon, 30
			));
			size = 2;

			highEdges = new int[]{1, 2};
			lowEdges = new int[]{0, 3};
			spinConfig = new SpinConfig() {{
				resistance = 3f/600f;
				allowedEdges = new int[][]{
					new int[]{0, 1, 4, 5},
					new int[]{2, 3, 6, 7},
					new int[]{4, 5, 0, 1},
					new int[]{6, 7, 2, 3}
				};
			}};

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawAxles(
					new Axle("-axle-end") {{
						iconOverride = "sw-shaft-transmission-axle-icon";

						pixelWidth = 16;
						pixelHeight = 1;

						spinScl = 0.5f;

						x = 6f;
						y = 4f;

						width = 4f;
						height = 3.5f;

						paletteLight = SWPal.axleLight;
						paletteMedium = SWPal.axleMedium;
						paletteDark = SWPal.axleDark;
					}},
					new Axle("-axle-end") {{
						hasIcon = false;

						pixelWidth = 16;
						pixelHeight = 1;

						x = 6f;
						y = -4f;

						width = 4f;
						height = 3.5f;

						paletteLight = SWPal.axleLight;
						paletteMedium = SWPal.axleMedium;
						paletteDark = SWPal.axleDark;
					}},
					new Axle("-axle-end") {{
						hasIcon = false;

						pixelWidth = 16;
						pixelHeight = 1;

						spinScl = 0.5f;

						x = -6f;
						y = 4f;

						width = 4f;
						height = 3.5f;

						paletteLight = SWPal.axleLight;
						paletteMedium = SWPal.axleMedium;
						paletteDark = SWPal.axleDark;
					}},
					new Axle("-axle-end") {{
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
					}},
					new Axle("-axle-small") {{
						hasIcon = false;
						circular = true;

						pixelWidth = 32;
						pixelHeight = 1;

						y = -4;

						width = 8f;
						height = 2f;
					}},
					new Axle("-axle-middle") {{
						hasIcon = false;
						circular = true;

						pixelWidth = 16;
						pixelHeight = 1;

						spinScl = -(35f/60f);

						y = -0.75f;

						width = 4f;
						height = 4.5f;
					}},
					new Axle("-axle-large") {{
						hasIcon = false;
						circular = true;

						pixelWidth = 32;
						pixelHeight = 1;

						spinScl = 0.5f;

						y = 4f;

						width = 8f;
						height = 5f;
					}}
				),
				new DrawBitmask("-tiles", build -> 0) {{
					tileWidth = tileHeight = 64;
				}}
			);
		}};
		mechanicalGovernor = new AxleBrake("mechanical-governor") {{
		
		}};
		
		hydraulicFlywheel = new RotationBattery("hydraulic-flywheel") {{
			requirements(Category.units, with(
			
			));
			size = 2;
			
			maxWindup = 6000;
			speed = 1/10f;
			resistanceScale = 300;
			resistanceMagnitude = 1.122f;
			outputForce = 10/600f;
			
			rotate = false;
			
			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawAxles(
					new Axle("-axle") {{
						pixelWidth = 1;
						pixelHeight = 1;
						
						width = 2.5f;
						height = 7f;
						
						paletteLight = Color.valueOf("BAA697");
						paletteMedium = Color.valueOf("947D72");
						paletteDark = Color.valueOf("6A504A");
					}}
				),
				new DrawParts() {{
					for (int i : Mathf.signs) {
						parts.add(
							new RegionPart("-piston" + (i == 1 ? "-top" : "-bottom")) {{
								outline = false;
								
								x = 3.25f * i;
								y = 5.25f * i;
								moveY = -3.5f * i;
								
								progress = PartProgress.reload;
							}}
						);
					}
				}},
				new DrawDefault()
			);
		}};
		winder = new PayloadSpinLoader("winder") {{
			requirements(Category.units, with(
			
			));
			size = 3;
			
			drawer = new DrawMulti(
				new DrawRegion("-bottom") {{
					buildingRotate = true;
				}},
				new DrawAxles(
					new Axle("-axle") {{
						pixelHeight = 1;
						pixelWidth = 96;
						
						width = 24f;
						height = 3.5f;
						
						rotation = -90f;
						
						paletteLight = SWPal.axleLight;
						paletteMedium = SWPal.axleMedium;
						paletteDark = SWPal.axleDark;
					}}
				),
				new DrawDefault(),
				new DrawRegion("-top") {{
					layer = Layer.blockOver + 0.1f;
				}},
				new DrawRegion("-gear", 1f) {{
					layer = Layer.blockOver + 0.1f;
				}}
			);
			
			spinConfig = new SpinConfig() {{
				resistance = 5/600f;
				
				allowedEdges = new int[][]{
					new int[]{3, 9},
					new int[]{6, 0},
					new int[]{9, 3},
					new int[]{0, 6}
				};
			}};
		}};
		latch = new PayloadSpinLoader("latch") {{
			requirements(Category.units, with(
			
			));
			size = 3;
			
			reverse = true;
			
			drawer = new DrawMulti(
				new DrawRegion("-bottom") {{
					buildingRotate = true;
				}},
				new DrawAxles(
					new Axle("-axle") {{
						pixelHeight = 1;
						pixelWidth = 96;
						
						width = 24f;
						height = 3.5f;
						
						rotation = -90f;
						
						paletteLight = SWPal.axleLight;
						paletteMedium = SWPal.axleMedium;
						paletteDark = SWPal.axleDark;
					}}
				),
				new DrawDefault(),
				new DrawParts() {{
					name = "-gears";
					for (int i = 0; i < 4; i++) {
						int finalI = i;
						parts.addAll(
							new RegionPart("-gear") {{
								x = Geometry.d4x(finalI) * 6.5f;
								y = Geometry.d4y(finalI) * 6.5f;
								moveRot = 360f * (finalI % 2 == 0 ? -1f : 1f);
								
								layer = Layer.blockOver + 0.1f;
								
								outline = false;
								
								progress = PartProgress.recoil.loop(360f);
							}}
						);
					}
				}},
				new DrawRegion("-top") {{
					layer = Layer.blockOver + 0.1f;
				}}
			);
			
			spinConfig = new SpinConfig() {{
				allowedEdges = new int[][]{
					new int[]{3, 9},
					new int[]{6, 0},
					new int[]{9, 3},
					new int[]{0, 6}
				};
			}};
		}};
	}
}
