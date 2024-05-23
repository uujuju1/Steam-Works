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

		resistantConveyor, suspensionConveyor,
		mechanicalDistributor, mechanicalTunnel,
		mechanicalGate,
		mechanicalUnloader,

		mechanicalPayloadConveyor, mechanicalPayloadRouter,


		mechanicalConduit, mechanicalConduitRouter, mechanicalConduitJunction;


	public static void load() {
		// region items
		resistantConveyor = new MechanicalConveyor("resistant-conveyor") {{
			requirements(Category.distribution, with(SWItems.nickel, 1));
			health = 100;
			speed = 0.04f;
			displayedSpeed = 5f;
			researchCost = with(
				SWItems.nickel, 30
			);
		}};
		suspensionConveyor = new MechanicalConveyor("suspension-conveyor") {{
			requirements(Category.distribution, with(SWItems.iron, 1));
			health = 100;
			speed = 0.04f;
			displayedSpeed = 5f;
			armored = true;
		}};
		mechanicalDistributor = new MechanicalDistributorSorter("mechanical-distributor") {{
			requirements(Category.distribution, with(SWItems.nickel, 5));
			health = 100;
			solid = false;
		}};
		mechanicalTunnel = new MechanicalTunnel("mechanical-tunnel") {{
			requirements(Category.distribution, with(
				SWItems.nickel, 5,
				SWItems.iron, 5
			));
			health = 100;
		}};
		mechanicalGate = new MechanicalGate("mechanical-gate") {{
			requirements(Category.distribution, with(
				SWItems.nickel, 5,
				SWItems.iron, 3
			));
			health = 100;
		}};
		mechanicalUnloader = new DirectionalUnloader("mechanical-unloader") {{
			requirements(Category.distribution, with(
				SWItems.nickel, 6,
				SWItems.iron, 4
			));
			health = 100;
			speed = 2f;
			solid = false;
			underBullets = true;
			regionRotated1 = 1;
		}};

		mechanicalPayloadConveyor = new PayloadConveyor("mechanical-payload-conveyor") {{
			requirements(Category.units, with(
				Items.silicon, 10,
				Items.titanium, 10,
				SWItems.nickel, 10
			));
			canOverdrive = false;
		}};
		mechanicalPayloadRouter = new PayloadRouter("mechanical-payload-router") {{
			requirements(Category.units, with(
				Items.silicon, 10,
				Items.titanium, 10,
				SWItems.nickel, 10
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
