package sw.content.blocks;

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

		waveRadar;

	public static void load() {
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

		waveRadar = new WaveRadar("wave-radar") {{
			requirements(Category.effect, with());
			health = 160;
		}};
	}
}
