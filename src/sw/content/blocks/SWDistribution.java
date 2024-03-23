package sw.content.blocks;

import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.payloads.*;
import sw.content.*;
import sw.world.blocks.distribution.*;
import sw.world.blocks.liquids.*;
import sw.world.meta.*;

import static mindustry.type.ItemStack.*;

public class SWDistribution {
	public static Block

		resistantConveyor, suspensionConveyor,
		mechanicalDistributor, mechanicalTunnel,
		mechanicalGate,
		mechanicalUnloader,

		mechanicalPayloadConveyor, mechanicalPayloadRouter,

		mechanicalConduit,

		lowWire, coatedWire,
		wireAdapter,

		lowRouter, coatedRouter,

		lowBridge, coatedBridge,

		wireJunction;

	public static void load() {

		// region distribution
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
			health = 109;
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
		mechanicalConduit = new MechanicalConduit("mechanical-conduit") {{
			requirements(Category.liquid, with());
			health = 100;
		}};
		//endregion

		lowWire = new TensionWire("low-tier-wire") {{
			requirements(Category.power, with());
			tensionConfig = new TensionConfig() {{
				staticTension = 2.5f;
			}};
		}};
		coatedWire = new TensionWire("coated-tier-wire") {{
			requirements(Category.power, with());
			tensionConfig = new TensionConfig() {{
				staticTension = 2.5f;
				tier = 1;
			}};
		}};
		wireAdapter = new TensionWire("wire-adapter") {{
			requirements(Category.power, with());
			tensionConfig = new TensionConfig() {{
				tier = -1;
				staticTension = 5;
			}};
		}};

		lowRouter = new TensionRouter("low-tier-wire-router") {{
			requirements(Category.power, with());
			tensionConfig = new TensionConfig() {{
				staticTension = 5;
			}};
		}};
		coatedRouter = new TensionRouter("coated-tier-wire-router") {{
			requirements(Category.power, with());
			tensionConfig = new TensionConfig() {{
				staticTension = 5;
				tier = 1;
			}};
		}};

		lowBridge = new TensionBridge("low-tier-wire-bridge") {{
			requirements(Category.power, with());
			tensionConfig = new TensionConfig() {{
				staticTension = 5;
			}};
		}};
		coatedBridge = new TensionBridge("coated-tier-wire-bridge") {{
			requirements(Category.power, with());
			tensionConfig = new TensionConfig() {{
				staticTension = 5;
				tier = 1;
			}};
		}};

		wireJunction = new TensionJunction("wire-junction") {{
			requirements(Category.power, with());
		}};
	}
}
