package sw.content.blocks;

import arc.math.*;
import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.liquid.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.draw.*;
import sw.content.*;
import sw.entities.*;
import sw.graphics.*;
import sw.world.blocks.distribution.*;
import sw.world.blocks.liquid.*;
import sw.world.blocks.payloads.*;
import sw.world.draw.*;
import sw.world.meta.*;

import static mindustry.type.ItemStack.*;

public class SWDistribution {
	public static Block

		mechanicalConveyor, suspensionConveyor, belt,

		mechanicalDistributor, mechanicalSorter, mechanicalBridge,
		mechanicalGate,
		mechanicalUnloader,


		mechanicalPayloadConveyor, mechanicalPayloadRouter,
		payloadCatapult,


		mechanicalConduit, mechanicalConduitRouter, mechanicalConduitJunction, mechanicalConduitBridge, mechanicalConduitTunnel;

	public static void load() {
		// region items
		mechanicalConveyor = new MechanicalConveyor("mechanical-conveyor") {{
			requirements(Category.distribution, with(
				SWItems.verdigris, 1
			));
			health = 100;
			speed = 0.04f;
			displayedSpeed = 5f;
			researchCost = with(
				SWItems.verdigris, 30
			);
		}};
		suspensionConveyor = new MechanicalConveyor("suspension-conveyor") {{
			requirements(Category.distribution, with(
				SWItems.iron, 1
			));
			health = 100;
			speed = 0.04f;
			displayedSpeed = 5f;
			armored = true;
		}};

		belt = new BeltConveyor("belt") {{
			requirements(Category.distribution, with(
				SWItems.iron, 2,
				Items.silicon, 2
			));

			drawer = new DrawMulti(
				new DrawBitmask(
					"-frames",
					b -> {
						int frames = 4 * Mathf.mod((int) (((BeltConveyorBuild) b).getRotation() / 360f * movementScale), 4);
						int tile = 0;
						if (b.front() instanceof BeltConveyorBuild && b.front().team == b.team && b.rotation == b.front().rotation) tile |= 1;
						if (b.back() instanceof BeltConveyorBuild && b.back().team == b.team && b.rotation == b.back().rotation) tile |= 2;
						return tile | frames;
					},
					32,
					Layer.block - 0.2f
				),
				new DrawConveyor() {{
					layer = Layer.block - 0.1f;
				}},
				new DrawBitmask(
					"-cover-bottom",
					b -> ((BeltConveyorBuild) b).nextBuilds().contains(other -> !(other instanceof BeltConveyorBuild)) ? 0 : 1,
					32
				),
				new DrawCondition(
					new DrawAxles(
						new Axle("-cover-axle") {{
							rotation = -90f;

							width = 8f;
							height = 3.5f;

							pixelWidth = 32;
							pixelHeight = 7;

							paletteLight = SWPal.axleLight;
							paletteMedium = SWPal.axleMedium;
							paletteDark = SWPal.axleDark;
						}}
					),
					b -> ((BeltConveyorBuild) b).nextBuilds().contains(other -> !(other instanceof BeltConveyorBuild))
				),
				new DrawBitmask(
					"-cover",
					b -> ((BeltConveyorBuild) b).nextBuilds().contains(other -> !(other instanceof BeltConveyorBuild)) ? 0 : 1,
					32
				)
			);

			spinConfig = new SpinConfig() {{
				allowedEdges = new int[][]{
					new int[]{1, 3},
					new int[]{0, 2},
					new int[]{3, 1},
					new int[]{2, 0}
				};
			}};
		}};

		mechanicalDistributor = new Router("mechanical-distributor") {{
			requirements(Category.distribution, with(
				SWItems.verdigris, 5
			));
			health = 100;
			solid = false;
		}};
		mechanicalSorter = new MechanicalSorter("mechanical-sorter") {{
			requirements(Category.distribution, with(
				SWItems.verdigris, 1,
				Items.graphite, 1
			));
		}};
		mechanicalBridge = new DuctBridge("mechanical-bridge") {{
			requirements(Category.distribution, with(
				SWItems.verdigris, 5,
				Items.graphite, 5
			));
			researchCost = with(
				SWItems.verdigris, 10,
				Items.graphite, 10
			);
			health = 100;
			range = 10;

			((MechanicalConveyor) mechanicalConveyor).bridgeReplacement = this;
			((MechanicalConveyor) suspensionConveyor).bridgeReplacement = this;
		}};
		mechanicalGate = new MechanicalGate("mechanical-gate") {{
			requirements(Category.distribution, with(
				SWItems.verdigris, 5,
				Items.graphite, 3
			));
			researchCost = with(
				SWItems.verdigris, 10,
				Items.graphite, 10
			);
			health = 100;
		}};
		mechanicalUnloader = new DirectionalUnloader("mechanical-unloader") {{
			requirements(Category.distribution, with(
				SWItems.verdigris, 6,
				Items.silicon, 2,
				Items.graphite, 4
			));
			researchCost = with(
				SWItems.verdigris, 12,
				Items.silicon, 4,
				Items.graphite, 8
			);
			health = 100;
			speed = 2f;
			solid = false;
			underBullets = true;
			regionRotated1 = 1;
		}};
		//endregion

		//region payloads
		mechanicalPayloadConveyor = new PayloadConveyor("mechanical-payload-conveyor") {{
			requirements(Category.units, with(
				Items.silicon, 10,
				SWItems.aluminium, 10,
				SWItems.verdigris, 10
			));
			canOverdrive = false;
			
			interp = Interp.linear;
			payloadLimit = 2f;
		}};
		mechanicalPayloadRouter = new PayloadRouter("mechanical-payload-router") {{
			requirements(Category.units, with(
				Items.silicon, 10,
				SWItems.aluminium, 10,
				SWItems.verdigris, 10
			));
			canOverdrive = false;
			
			interp = Interp.linear;
			payloadLimit = 2f;
		}};
		payloadCatapult = new PayloadCatapult("payload-catapult") {{
			requirements(Category.units, with(
				Items.silicon, 30,
				SWItems.aluminium, 50,
				SWItems.verdigris, 25
			));
			size = 3;
			
			consumeLiquid(Liquids.ozone, 1f/60f);
			
			shootEffect = SWFx.ozoneLaunch;
		}};
		//endregion

		//region liquids
		mechanicalConduit = new LiquidPipe("mechanical-conduit") {{
			requirements(Category.liquid, with(
				SWItems.iron, 2
			));
			padding = 0.75f;
			
			liquidCapacity = 20f;
		}};
		mechanicalConduitRouter = new LiquidRouter("mechanical-conduit-router") {{
			requirements(Category.liquid, with(
				SWItems.iron, 2,
				Items.graphite, 2
			));
			
			liquidCapacity = 120f;
		}};
		mechanicalConduitJunction = new LiquidJunction("mechanical-conduit-junction") {{
			requirements(Category.liquid, with(
				SWItems.iron, 3,
				Items.graphite, 3
			));

			((LiquidPipe) mechanicalConduit).junctionReplacement = this;
		}};
		mechanicalConduitBridge = new LiquidPipeBridge("mechanical-conduit-bridge") {{
			requirements(Category.liquid, with(
				SWItems.iron, 10,
				Items.graphite, 10
			));

			range = 10;
		}};
		mechanicalConduitTunnel = new LiquidPipeTunnel("mechanical-conduit-tunnel") {{
			requirements(Category.liquid, with(
				SWItems.iron, 10,
				Items.graphite, 10
			));

			range = 10;
		}};
		//endregion
	}
}
