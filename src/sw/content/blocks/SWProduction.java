package sw.content.blocks;

import arc.math.geom.*;
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
		mechanicalBore, hydraulicDrill, dehydrator,

		liquidCollector;

	public static void load() {
		mechanicalBore = new RangedDrill("mechanical-bore") {{
			requirements(Category.production, with(
				SWItems.nickel, 25
			));
			researchCost = with(
				SWItems.nickel, 100
			);
			size = 2;
			health = 160;
			drillTime = 480f;
			tier = 1;
			range = 5;
			ambientSound = Sounds.combustion;
		}};
		hydraulicDrill = new AreaDrill("hydraulic-drill") {{
			requirements(Category.production, with(
				SWItems.nickel, 40,
				Items.graphite, 50
			));
			researchCost = with(
				SWItems.nickel, 160,
				Items.graphite, 200
			);
			liquidBoostIntensity = 1f;
			size = 2;
			health = 160;
			tier = 2;
			drillTime = hardnessDrillMultiplier = 240;
			drillEffect = SWFx.groundCrack;
			drillEffectRnd = 0f;
			ambientSound = Sounds.drillCharge;
			mineRect = new Rect(0, 0, 4, 4);
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
			requirements(Category.liquid, with(
				SWItems.iron, 20,
				Items.silicon, 10
			));
		}};
	}
}
