package sw.content.blocks;

import arc.graphics.*;
import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.meta.*;
import sw.content.*;
import sw.world.blocks.environment.*;

public class SWEnvironment {
	public static Block
		charoite, charoiteCraters, charoiteWall, charoiteBoulder, charoiteGasVent,
		grime, grimeWall, grimeBoulder, grimeGasVent,
		sediment, sedimentWall, sedimentBoulder, sedimentGasVent,
		moss, mossWall, mossBoulder, mossGasVent,
	purpleIce, purpleIceCraters, purpleIceWall, purpleIceBoulder,

	oreNickel, graphiteCharoiteWallOre, graphiteGrimeWallOre,

	chloro, deepChloro, shallowChloro;

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
		charoiteGasVent = new GasVent("charoite-gas-vent") {{
			parent = blendGroup = charoite;
			fluid = SWLiquids.steam;
			cloudEffectColor = Color.white;
		}};

		grimeWall = new StaticWall("grime-wall") {{
			attributes.set(Attribute.sand, 1);
		}};
		grimeBoulder = new Prop("grime-boulder") {{
			variants = 2;
		}};
		grime = new Floor("grime") {{
			variants = 4;
			wall = grimeWall;
			decoration = grimeBoulder;
		}};
		grimeGasVent = new GasVent("grime-gas-vent") {{
			parent = blendGroup = grime;
			fluid = SWLiquids.steam;
			cloudEffectColor = Color.white;
		}};

		sedimentWall = new StaticWall("sediment-wall");
		sedimentBoulder = new Prop("sediment-boulder") {{
			variants = 2;
		}};
		sediment = new Floor("sediment") {{
			variants = 4;
			wall = sedimentWall;
			decoration = sedimentBoulder;
		}};
		sedimentGasVent = new GasVent("sediment-gas-vent") {{
			parent = blendGroup = sediment;
			fluid = SWLiquids.fungi;
			cloudEffectColor = Color.valueOf("9AC49A");
		}};

		mossWall = new StaticWall("moss-wall");
		mossBoulder = new Prop("moss-boulder") {{
			variants = 2;
		}};
		moss = new Floor("moss") {{
			variants = 4;
			wall = mossWall;
			decoration = mossBoulder;
		}};
		mossGasVent = new GasVent("moss-gas-vent") {{
			parent = blendGroup = moss;
			fluid = SWLiquids.fungi;
			cloudEffectColor = Color.valueOf("9AC49A");
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
		graphiteCharoiteWallOre = new StaticWall("graphite-charoite-wall-ore") {{
			itemDrop = Items.graphite;
		}};
		graphiteGrimeWallOre = new StaticWall("graphite-grime-wall-ore") {{
			itemDrop = Items.graphite;
		}};

		deepChloro = new Floor("deep-chloro") {{
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
		chloro = new Floor("chloro") {{
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
		shallowChloro = new Floor("shallow-chloro") {{
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
