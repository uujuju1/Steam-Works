package sw.content.blocks;

import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import sw.content.*;
import sw.world.blocks.environment.*;

public class SWEnvironment {
	public static Block
	cliff,

	oreNickel, oreIron, fissure, oreGraphite,

	souesite, fissuredSouesite, souesiteCrater, largeSouesiteCrater, souesiteWall, souesiteBoulder,
	souesiteShallowerSolvent, souesiteShallowSolvent,
	agedSouesite, agedSouesiteWall,

	tuff, shapedTuff, tuffWall, columnarTuff, tuffConcretion, tuffPile,
	tuffShallowerSolvent, tuffShallowSolvent,

	solvent,
	solventCrystal,

	plate, plateCross, plateVent, plateWall,

	limiter;

	public static void load() {
//		spinyTree = new TreeBlock("spiny-tree") {{
//			attributes.set(Attribute.steam, 1);
//		}};
//		deadSpinyTree = new TreeBlock("dead-spiny-tree") {{
//			attributes.set(Attribute.steam, 0.25f);
//		}};


		cliff = new SWCliff("cliff");


		limiter = new Limiter("limiter");


		// region ores
		oreNickel = new OreBlock(SWItems.nickel) {{
			variants = 4;
		}};
		oreIron = new OreBlock(SWItems.iron) {{
			variants = 4;
		}};
		fissure = new OreBlock("fissure", Items.sand) {{
			wallOre = true;
			variants = 4;
		}};
		oreGraphite = new OreBlock(Items.graphite) {{
			wallOre = true;
			variants = 4;
		}};
		// endregion

		//region souesite
		souesiteWall = new StaticWall("souesite-wall");
		souesiteBoulder = new Prop("souesite-boulder") {{
			variants = 2;
		}};
		souesiteCrater = new Floor("souesite-crater", 2) {{
			wall = souesiteWall;
			decoration = souesiteBoulder;
		}};
		largeSouesiteCrater = new SteamVent("large-souesite-crater") {{
			variants = 0;
			effect = Fx.none;
		}};
		souesite = new Floor("souesite", 4) {{
			wall = souesiteWall;
			decoration = souesiteBoulder;
			((SteamVent) largeSouesiteCrater).parent = this;
			((SteamVent) largeSouesiteCrater).blendGroup = this;
		}};
		fissuredSouesite = new Floor("fissured-souesite", 4) {{
			wall = souesiteWall;
			decoration = souesiteBoulder;
		}};

		agedSouesiteWall = new StaticWall("aged-souesite-wall");
		agedSouesite = new Floor("aged-souesite") {{
			wall = agedSouesiteWall;
		}};

		souesiteShallowerSolvent = new Floor("souesite-shallower-solvent", 2) {{
			wall = souesiteWall;
			cacheLayer = CacheLayer.water;
			isLiquid = true;
			liquidDrop = SWLiquids.solvent;
		}};
		souesiteShallowSolvent = new Floor("souesite-shallow-solvent", 2) {{
			wall = souesiteWall;
			cacheLayer = CacheLayer.water;
			isLiquid = true;
			liquidDrop = SWLiquids.solvent;
		}};
		//endregion

		//region tuff
		tuffWall = new StaticWall("tuff-wall");
		tuffPile = new Prop("tuff-pile") {{
			variants = 2;
		}};
		columnarTuff = new TallBlock("columnar-tuff") {{
			variants = 2;
		}};
		tuffConcretion = new OverlayFloor("tuff-concretion") {{
			variants = 4;
		}};
		tuff = new Floor("tuff", 4) {{
			wall = tuffWall;
			decoration = tuffPile;
		}};
		shapedTuff = new Floor("shaped-tuff", 4) {{
			wall = tuffWall;
			decoration = tuffPile;
		}};

		tuffShallowerSolvent = new Floor("tuff-shallower-solvent", 2) {{
			wall = tuffWall;
			cacheLayer = CacheLayer.water;
			isLiquid = true;
			liquidDrop = SWLiquids.solvent;
		}};
		tuffShallowSolvent = new Floor("tuff-shallow-solvent", 2) {{
			wall = tuffWall;
			cacheLayer = CacheLayer.water;
			isLiquid = true;
			liquidDrop = SWLiquids.solvent;
		}};
		//endregion

		//region solvent
		solventCrystal = new TallBlock("solvent-crystal") {{
			variants = 2;
		}};
		solvent = new Floor("solvent-regular", 0) {{
			cacheLayer = CacheLayer.water;
			isLiquid = true;
			liquidDrop = SWLiquids.solvent;
		}};
		//endregion

		/*

		// region glacier
		glacierWall = new StaticWall("glacier-wall");
		glacier = new Floor("glacier", 4) {{
			wall = glacierWall;
		}};
		glacierCrater = new Floor("glacier-crater", 2) {{
			wall = glacierWall;
		}};
		weatheredGlacier = new Floor("weathered-glacier", 4) {{
			wall = glacierWall;
		}};
		// endregion

		// region blue ice
		blueSolventIceWall = new StaticWall("blue-solvent-ice-wall");
		blueSolventIce = new Floor("blue-solvent-ice", 4) {{
			wall = blueSolventIceWall;
		}};
		blueSolventIceCrater = new Floor("blue-solvent-ice-crater", 2) {{
			wall = blueSolventIceWall;
		}};
		crystallineBlue = new Floor("crystalline-blue", 4) {{
			wall = blueSolventIceWall;
		}};
		// endregion

		//region erosion
		erosionWall = new StaticWall("erosion-wall");
		erosionBoulder = new Prop("erosion-boulder") {{
			variants = 2;
		}};
		erosion = new Floor("erosion", 4) {{
			wall = erosionWall;
			decoration = erosionBoulder;
		}};
		erosionCrater = new Floor("erosion-crater", 2) {{
			wall = erosionWall;
			decoration = erosionCrater;
		}};
		//endregion

		//region melt stone
		meltStoneWall = new StaticWall("melt-stone-wall");
		meltStoneBoulder = new Prop("melt-stone-boulder") {{
			variants = 2;
		}};
		meltStone = new Floor("melt-stone", 4) {{
			wall = meltStoneWall;
			decoration = meltStoneBoulder;
		}};
		meltPebbles = new Floor("melt-pebbles", 4) {{
			wall = meltStoneWall;
			decoration = meltStoneBoulder;
		}};
		//endregion

		*/

		// region plate
		plateWall = new StaticWall("plate-wall");
		plate = new Floor("plate", 4) {{
			wall = plateWall;
		}};
		plateVent = new Floor("plate-vent", 0) {{
			wall = plateWall;
			blendGroup = plate;
		}};
		plateCross = new Floor("plate-cross", 0) {{
			wall = plateWall;
			blendGroup = plate;
		}};
		// endregion
	}
}
