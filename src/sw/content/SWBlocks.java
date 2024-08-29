package sw.content;

import arc.util.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.units.*;
import sw.content.blocks.*;
import sw.world.blocks.sandbox.*;
import sw.world.blocks.units.*;

import static mindustry.type.ItemStack.*;

public class SWBlocks {
	public static Block
    subFactory,
		constructor,
		upgrader,

		allSource;

	public static void load() {
		SWCrafting.load();
		SWDefense.load();
		SWDistribution.load();
		SWEnvironment.load();
		SWProduction.load();
		SWPower.load();
		SWStorage.load();
		SWTurrets.load();

		// region units
		subFactory = new UnitFactory("submarine-factory") {{
			requirements(Category.units, with(
				SWItems.compound, 120,
				Items.lead, 140,
				Items.graphite, 100
			));
			size = 3;
			health = 160;
			consumePower(1.5f);
			plans.add(new UnitPlan(SWUnitTypes.recluse, 60f * 50f, with(Items.silicon, 15, Items.metaglass, 25, SWItems.compound, 20)));
		}};

		constructor = new UnitFactory("constructor") {{
			requirements(Category.units, with(
				SWItems.nickel, 150,
				SWItems.iron, 120,
				Items.silicon, 100
			));
			size = 3;
			health = 200;
			plans.add(
				new UnitPlan(SWUnitTypes.fly, 40 * Time.toSeconds, with(Items.silicon, 15, SWItems.iron, 10)),
				new UnitPlan(SWUnitTypes.sentry, 80 * Time.toSeconds, with(Items.silicon, 25, SWItems.denseAlloy, 10)),
				new UnitPlan(SWUnitTypes.recluse, 80 * Time.toSeconds, with(Items.silicon, 15, SWItems.compound, 20))
			);
			consumePower(1f);
			consumeLiquid(SWLiquids.steam, 0.3f);
		}};
		upgrader = new MultiReconstructor("upgrader") {{
			requirements(Category.units, with(
				SWItems.nickel, 200,
				SWItems.iron, 240,
				Items.silicon, 220
			));
			size = 5;
			health = 320;
			constructTime = 10 * 60f;
			upgrades.addAll(
				new UnitType[]{SWUnitTypes.fly, SWUnitTypes.spin},
				new UnitType[]{SWUnitTypes.spin, SWUnitTypes.gyro},
				new UnitType[]{SWUnitTypes.sentry, SWUnitTypes.tower},
				new UnitType[]{SWUnitTypes.tower, SWUnitTypes.castle}
			);
			consumeItems(with(Items.silicon, 400));
		}};
		// endregion

		// region sandbox
		allSource = new ResourceSource("all-source") {{
			health = 2147483647;
		}};
		// endregion
	}
}
