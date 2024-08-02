package sw.content.blocks;

import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.*;
import sw.content.*;
import sw.world.blocks.units.*;

import static mindustry.type.ItemStack.*;

public class SWDefense {
	public static Block
		ironWall, ironWallLarge, nickelWall, nickelWallLarge,
		compoundWall, compoundWallLarge, denseWall, denseWallLarge,

		waveRadar, unitScanner, distributionMender;

	public static void load() {
		// region walls
		nickelWall = new Wall("nickel-wall") {{
			requirements(Category.defense, with(SWItems.nickel, 6));
			health = 90 * 4;
		}};
		nickelWallLarge = new Wall("nickel-wall-large") {{
			requirements(Category.defense, mult(nickelWall.requirements, 4));
			size = 2;
			health = 90 * 4 * 4;
		}};
		ironWall = new Wall("iron-wall") {{
			requirements(Category.defense, with(SWItems.iron, 6));
			health = 100 * 4;
			absorbLasers = true;
		}};
		ironWallLarge = new Wall("iron-wall-large") {{
			requirements(Category.defense, mult(ironWall.requirements, 4));
			size = 2;
			health = 100 * 4 * 4;
			absorbLasers = true;
		}};
		compoundWall = new Wall("compound-wall") {{
			requirements(Category.defense, with(SWItems.compound, 6));
			health = 110 * 4;
		}};
		compoundWallLarge = new Wall("compound-wall-large") {{
			requirements(Category.defense, mult(compoundWall.requirements, 4));
			size = 2;
			health = 110 * 4 * 4;
		}};
		denseWall = new Wall("dense-wall") {{
			requirements(Category.defense, with(SWItems.denseAlloy, 6));
			health = 110 * 4;
			absorbLasers = true;
		}};
		denseWallLarge = new Wall("dense-wall-large") {{
			requirements(Category.defense, mult(denseWall.requirements, 4));
			size = 2;
			health = 110 * 4 * 4;
			absorbLasers = true;
		}};
		// endregion

		waveRadar = new WaveRadar("wave-radar") {{
			requirements(Category.effect, with(
				SWItems.iron, 20,
				Items.silicon, 20
			));
			health = 160;
		}};

		/*
		unitScanner = new UnitScanner("unit-scanner") {{
			requirements(Category.effect, with(
				SWItems.compound, 10,
				SWItems.iron, 30,
				Items.silicon, 40
			));
			health = 160;
			size = 3;

			consume(new ConsumeTension(10, 20));
		}};

		distributionMender = new TensionMender("distribution-mender") {{
			requirements(Category.effect, with());
			size = 2;
			health = 180;

			baseColor = phaseColor = Color.valueOf("BF92F8");
			range = 80f;
			healPercent = 8;
			phaseBoost = phaseRangeBoost = 0f;

			consume(new ConsumeTension(10, 20));
		}};

		 */
	}
}
