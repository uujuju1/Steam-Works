package sw.content.blocks;

import arc.graphics.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.liquid.*;
import mindustry.world.blocks.payloads.*;
import sw.content.*;
import sw.world.blocks.distribution.*;

import static mindustry.type.ItemStack.*;

public class SWDistribution {
	public static Block

		mechanicalConveyor, suspensionConveyor,
		mechanicalDistributor, mechanicalSorter, mechanicalBridge,
		mechanicalGate,
		mechanicalUnloader,


		mechanicalPayloadConveyor, mechanicalPayloadRouter,


		mechanicalConduit, mechanicalConduitRouter, mechanicalConduitJunction;

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
				Items.titanium, 10,
				SWItems.verdigris, 10
			));
			canOverdrive = false;
		}};
		mechanicalPayloadRouter = new PayloadRouter("mechanical-payload-router") {{
			requirements(Category.units, with(
				Items.silicon, 10,
				Items.titanium, 10,
				SWItems.verdigris, 10
			));
			canOverdrive = false;
		}};
		//endregion

		//region liquids
		mechanicalConduitRouter = new LiquidRouter("mechanical-conduit-router") {{
			requirements(Category.liquid, with(
				SWItems.iron, 2,
				Items.silicon, 2
			));
		}};
		mechanicalConduitJunction = new LiquidJunction("mechanical-conduit-junction") {{
			requirements(Category.liquid, with(
				SWItems.iron, 3,
				Items.silicon, 3
			));
		}};
		mechanicalConduit = new Conduit("mechanical-conduit") {{
			requirements(Category.liquid, with(
				SWItems.iron, 2
			));
			leaks = false;
			junctionReplacement = mechanicalConduitJunction;
			botColor = Color.white;
		}};
		//endregion
	}
}
