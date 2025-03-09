package sw.content.blocks;

import mindustry.content.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import sw.content.*;
import sw.entities.*;
import sw.graphics.*;
import sw.world.blocks.power.*;
import sw.world.blocks.production.*;
import sw.world.draw.*;
import sw.world.interfaces.*;
import sw.world.meta.*;

import static mindustry.type.ItemStack.*;

public class SWPower {
	public static Block
		evaporator,
		wireShaft, wireShaftRouter, shaftGearbox, overheadBelt,

		shaftTransmission;

	public static void load() {
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
					new Axle("-shaft") {{
						pixelHeight = 7;
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
			rotate = false;

			spinConfig = new SpinConfig() {{
				resistance = 2f/600f;
			}};

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawAxles(
					new Axle("-shaft") {{
						iconOverride = "sw-wire-shaft-router-shaft-icon-horizontal";
						pixelHeight = 7;

						height = 3.5f;

						paletteLight = SWPal.axleLight;
						paletteMedium = SWPal.axleMedium;
						paletteDark = SWPal.axleDark;
					}},
					new Axle("-shaft") {{
						iconOverride = "sw-wire-shaft-router-shaft-icon-vertical";
						pixelHeight = 7;

						rotation = -90f;

						height = 3.5f;

						paletteLight = SWPal.axleLight;
						paletteMedium = SWPal.axleMedium;
						paletteDark = SWPal.axleDark;
					}}
				),
				new DrawDefault()
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

		shaftTransmission = new ShaftTransmission("shaft-transmission") {{
			requirements(Category.power, with(
				SWItems.verdigris, 50,
				SWItems.iron, 40,
				Items.silicon, 30
			));
			size = 2;

			scaledEdges = new int[]{1, 2};
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
					new Axle("-shaft-end") {{
						iconOverride = "sw-shaft-transmission-shaft-end-icon-top";

						pixelWidth = 64;
						pixelHeight = 7;

						spinScl = 0.5f;

						y = 4f;

						width = 16f;
						height = 3.5f;
					}},
					new Axle("-shaft-end") {{
						iconOverride = "sw-shaft-transmission-shaft-end-icon-bottom";

						pixelWidth = 64;
						pixelHeight = 7;

						y = -4f;

						width = 16f;
						height = 3.5f;
					}},
					new Axle("-shaft-small") {{
						pixelHeight = 4;
						pixelWidth = 32;

						y = -4;

						width = 8f;
						height = 2f;
					}},
					new Axle("-shaft-middle") {{
						pixelHeight = 9;
						pixelWidth = 16;

						spinScl = -(35f/60f);

						y = -0.75f;

						width = 4f;
						height = 4.5f;
					}},
					new Axle("-shaft-big") {{
						pixelHeight = 10;
						pixelWidth = 32;

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

		evaporator = new SWGenericCrafter("evaporator") {{
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
				topSpeed = 1f;
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

			updateEffectStatic = SWFx.evaporate;
			updateEffectChance = 0.5f;

			consumeLiquid(SWLiquids.solvent, 1f/60f);

			outputRotation = 1f;
			outputRotationForce = 1f/60f;
		}};
	}
}
