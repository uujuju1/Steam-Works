package sw.content.blocks;

import arc.graphics.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;
import mindustry.world.meta.*;
import sw.content.*;
import sw.world.blocks.production.*;

import static mindustry.type.ItemStack.*;

public class SWProduction {
	public static Block
		mechanicalBore, mechanicalCrusher, hydraulicDrill, dehydrator,

		liquidCollector;

	public static void load() {
		mechanicalBore = new BeamDrill("mechanical-bore") {{
			requirements(Category.production, with(
				SWItems.nickel, 25
			));
			researchCost = with(
				SWItems.nickel, 100
			);
			size = 2;
			health = 160;
			glowIntensity = pulseIntensity = 0f;
			optionalBoostIntensity = 1f;
			drillTime = 480f;
			tier = 1;
			range = 5;
			boostHeatColor = Color.black;
			ambientSound = Sounds.drill;
			ambientSoundVolume = 0.04f;
		}};
		mechanicalCrusher = new WallCrafter("mechanical-crusher") {{
			requirements(Category.production, with(
				SWItems.nickel, 25,
				Items.graphite, 25
			));
			researchCost = with(
				SWItems.nickel, 100,
				Items.graphite, 100
			);
			size = 2;
			health = 160;
			drillTime = 120f;
			ambientSound = Sounds.drill;
			ambientSoundVolume = 0.04f;
		}};
		hydraulicDrill = new Drill("hydraulic-drill") {{
			requirements(Category.production, with(
				SWItems.nickel, 40,
				Items.graphite, 50
			));
			researchCost = with(
				SWItems.nickel, 160,
				Items.graphite, 200
			);
			size = 2;
			health = 160;
			tier = 2;
			drillTime = hardnessDrillMultiplier = 240;
		}};

		dehydrator = new WallGenericCrafter("dehydrator") {{
			requirements(Category.production, with(
				SWItems.nickel, 50,
				SWItems.iron, 35,
				Items.silicon, 45
			));
			size = 3;
			attribute = Attribute.steam;
			hasLiquids = true;

			outputLiquids = LiquidStack.with(SWLiquids.steam, 0.1f);
		}};

		liquidCollector = new Pump("liquid-collector") {{
			requirements(Category.production, with(
				SWItems.iron, 20,
				Items.silicon, 10
			));
		}};
	}
}
