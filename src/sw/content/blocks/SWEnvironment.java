package sw.content.blocks;

import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;

public class SWEnvironment {
	public static Block
	charoite, charoiteWall,
	purpleSand, purpleSandWall,
	heavySnow, heavySnowWall,
	purpleIce, purpleIceWall,
	heavyWater, deepHeavyWater;

	public static void load() {
		charoite = new Floor("charoite") {{
			variants = 4;
		}};
		charoiteWall = new StaticWall("charoite-wall");

		purpleSand = new Floor("purple-sand") {{
			variants = 4;
			itemDrop = Items.sand;
		}};
		purpleSandWall = new StaticWall("purple-sand-wall");

		heavySnow = new Floor("heavy-snow") {{
			variants = 4;
		}};
		heavySnowWall = new StaticWall("heavy-snow-wall");

		purpleIce = new Floor("purple-ice") {{
			variants = 4;
		}};
		purpleIceWall = new StaticWall("purple-ice-wall");

		heavyWater = new Floor("heavy-water") {{
			speedMultiplier = 0.5f;
			variants = 0;
			status = StatusEffects.wet;
			statusDuration = 90f;
			liquidDrop = Liquids.water;
			isLiquid = true;
			cacheLayer = CacheLayer.water;
			albedo = 0.9f;
			supportsOverlay = true;
		}};
		deepHeavyWater = new Floor("deep-heavy-water") {{
			speedMultiplier = 0.5f;
			variants = 0;
			status = StatusEffects.wet;
			statusDuration = 90f;
			liquidDrop = Liquids.water;
			isLiquid = true;
			cacheLayer = CacheLayer.water;
			albedo = 0.9f;
			supportsOverlay = true;
			drownTime = 200f;
		}};
	}
}
