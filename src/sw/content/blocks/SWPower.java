package sw.content.blocks;

import arc.struct.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import sw.content.*;
import sw.math.*;
import sw.world.blocks.power.*;
import sw.world.blocks.production.*;
import sw.world.draw.*;
import sw.world.interfaces.*;
import sw.world.meta.*;

import static mindustry.type.ItemStack.*;

public class SWPower {
	public static Block
		boiler,
		wireShaft, wireShaftRouter, shaftGearbox,

		shaftTransmission;

	public static void load() {
		wireShaft = new WireShaft("wire-shaft") {{
			requirements(Category.power, with(
				SWItems.nickel, 5,
				SWItems.iron, 5
			));

			spinConfig = new SpinConfig() {{
				resistance = 1f/600f;
				connections = new Seq[]{
					BlockGeometry.sides1,
					BlockGeometry.sides2,
					BlockGeometry.sides3,
					BlockGeometry.sides4
				};
			}};

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawAxles(
					new DrawAxles.Axle("-shaft") {{
						pixelHeight = 7;
						height = 3.5f;
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
		wireShaftRouter = new WireShaft("wire-shaft-router") {{
			requirements(Category.power, with(
				SWItems.nickel, 10,
				SWItems.iron, 5
			));
			rotate = false;

			spinConfig = new SpinConfig() {{
				resistance = 2f/600f;
			}};

			drawer = new DrawMulti(
				new DrawAxles(
					new DrawAxles.Axle("-shaft") {{
						iconOverride = "sw-wire-shaft-router-shaft-icon-horizontal";
						pixelHeight = 7;

						height = 3.5f;
					}},
					new DrawAxles.Axle("-shaft") {{
						iconOverride = "sw-wire-shaft-router-shaft-icon-vertical";
						pixelHeight = 7;

						rotation = -90f;

						height = 3.5f;
					}}
				),
				new DrawDefault()
			);
		}};
		shaftGearbox = new WireShaft("shaft-gearbox") {{
			requirements(Category.power, with(
				SWItems.nickel, 10,
				SWItems.iron, 5
			));
			size = 2;

			rotate = false;

			spinConfig = new SpinConfig() {{
				resistance = 2f/600f;
				connections = new Seq[]{
					BlockGeometry.sides21,
					BlockGeometry.sides22,
					BlockGeometry.sides23,
					BlockGeometry.sides24
				};
			}};

			drawer = new DrawMulti(
				new DrawAxles(
					new DrawAxles.Axle("-shaft") {{
						iconOverride = "sw-shaft-gearbox-shaft-icon-top";

						pixelWidth = 64;
						pixelHeight = 7;

						y = 4f;

						width = 16f;
						height = 3.5f;
					}},
					new DrawAxles.Axle("-shaft") {{
						iconOverride = "sw-shaft-gearbox-shaft-icon-bottom";

						pixelWidth = 64;
						pixelHeight = 7;

						y = -4f;

						width = 16f;
						height = 3.5f;
					}},
					new DrawAxles.Axle("-shaft-middle") {{
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

		shaftTransmission = new ShaftTransmission("shaft-transmission") {{
			requirements(Category.power, with(
				SWItems.nickel, 50,
				SWItems.iron, 40,
				Items.silicon, 30
			));
			size = 2;

			spinConfig = new SpinConfig() {{
				resistance = 3f/600f;
				connections = new Seq[]{
					BlockGeometry.sides21,
					BlockGeometry.sides22,
					BlockGeometry.sides23,
					BlockGeometry.sides24
				};
			}};
		}};

		boiler = new SWGenericCrafter("boiler") {{
			requirements(Category.power, with(
				SWItems.iron, 20,
				SWItems.compound, 40,
				Items.silicon, 35
			));
			size = 2;
			health = 160;

			spinConfig = new SpinConfig() {{
				topSpeed = 6f;
				connections = new Seq[]{
					BlockGeometry.half2,
					BlockGeometry.half2,
					BlockGeometry.half2,
					BlockGeometry.half2
				};
			}};

			drawer = new DrawMulti(
				new DrawDefault(),
				new DrawRegion("-rotator") {{
					spinSprite = true;
					rotateSpeed = 2f;
				}}
			);

			updateEffectStatic = SWFx.burnElevation;

			consumeItem(Items.graphite, 1);
			consumeLiquid(SWLiquids.solvent, 0.1f);
			craftTime = 30;

			outputRotation = 6f;
			outputRotationForce = 1f/120f;
		}};
	}
}
