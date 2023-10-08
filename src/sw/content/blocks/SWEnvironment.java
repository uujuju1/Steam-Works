package sw.content.blocks;

import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.meta.*;
import sw.content.*;

public class SWEnvironment {
	public static Block
	charoite, charoiteCraters, charoiteWall, charoiteBoulder,
	purpleSand, purpleSandWall, purpleSandBoulder,
	heavySnow, heavySnowWall, heavySnowBoulder,
	purpleIce, purpleIceCraters, purpleIceWall, purpleIceBoulder,

	oreNickel,

	heavyWater, deepHeavyWater, purpleSandHeavyWater, charoiteHeavyWater;

	public static void load() {
		charoiteWall = new StaticWall("charoite-wall") {{
			attributes.set(Attribute.sand, 2f);
		}};
		charoiteBoulder = new Prop("charoite-boulder") {{
			variants = 2;
		}};
		charoite = new Floor("charoite") {{
			variants = 4;
			wall = charoiteWall;
			decoration = charoiteBoulder;
		}};
		charoiteCraters = new Floor("charoite-crater") {{
			variants = 2;
			wall = charoiteWall;
			decoration = charoiteBoulder;
			blendGroup = charoite;
		}};

		purpleSandWall = new StaticWall("purple-sand-wall") {{
			attributes.set(Attribute.sand, 1);
		}};
		purpleSandBoulder = new Prop("purple-sand-boulder") {{
			variants = 2;
		}};
		purpleSand = new Floor("purple-sand") {{
			variants = 4;
			wall = purpleSandWall;
			decoration = purpleSandBoulder;
			itemDrop = Items.sand;
		}};

		heavySnowWall = new StaticWall("heavy-snow-wall");
		heavySnowBoulder = new Prop("heavy-snow-boulder") {{
			variants = 2;
		}};
		heavySnow = new Floor("heavy-snow") {{
			variants = 4;
			wall = heavySnowWall;
			decoration = heavySnowBoulder;
		}};

		purpleIceWall = new StaticWall("purple-ice-wall") {{
			attributes.set(Attribute.sand, 1.5f);
		}};
		purpleIceBoulder = new Prop("purple-ice-boulder") {{
			variants = 2;
		}};
		purpleIce = new Floor("purple-ice") {{
			variants = 4;
			wall = purpleIceWall;
			decoration = purpleIceBoulder;
		}};
		purpleIceCraters = new Floor("purple-ice-crater") {{
			variants = 2;
			wall = purpleIceWall;
			decoration = purpleIceBoulder;
			blendGroup = purpleIce;
		}};

		oreNickel = new OreBlock(SWItems.nickel);

		deepHeavyWater = new Floor("deep-heavy-water") {{
			speedMultiplier = 0.25f;
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
		purpleSandHeavyWater = new Floor("purple-sand-heavy-water") {{
			speedMultiplier = 0.75f;
			variants = 0;
			status = StatusEffects.wet;
			statusDuration = 90f;
			liquidDrop = Liquids.water;
			isLiquid = true;
			cacheLayer = CacheLayer.water;
			albedo = 0.9f;
			supportsOverlay = true;
		}};
		charoiteHeavyWater = new Floor("charoite-heavy-water") {{
			speedMultiplier = 0.75f;
			variants = 0;
			status = StatusEffects.wet;
			statusDuration = 90f;
			liquidDrop = Liquids.water;
			isLiquid = true;
			cacheLayer = CacheLayer.water;
			albedo = 0.9f;
			supportsOverlay = true;
		}};
	}
}
