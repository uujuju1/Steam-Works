package sw.content.blocks;

import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.meta.*;
import sw.content.*;
import sw.world.blocks.environment.*;

public class SWEnvironment {
	public static Block
	spinyTree, deadSpinyTree,

	cliff,


	oreNickel, oreIron, fissure, oreGraphite,

	sandstoneBoulder, sandstoneWall,
	sandstone, spongestone,

	concretion, soilBoulder, soilWall,
	soil, clay,

	andesitePlate, andesiteBoulder, andesiteWall,
	andesite, striatedAndesite,

	solventShards, solventIceWall, solventIceBoulder,
	solventRegular, shallowSolvent, shallowerSolvent, solventIce, solventSnow,

	glacierWall,
	glacier, glacierCrater, weatheredGlacier,

	blueSolventIceWall,
	blueSolventIce, blueSolventIceCrater, crystallineBlue,

	plate, plateCross, plateVent, plateWall,

	limiter,

	multiFloor, multiOverlay, multiWall;

	public static void load() {
		spinyTree = new TreeBlock("spiny-tree") {{
			attributes.set(Attribute.steam, 1);
		}};
		deadSpinyTree = new TreeBlock("dead-spiny-tree") {{
			attributes.set(Attribute.steam, 0.25f);
		}};


		cliff = new SWCliff("cliff");


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

		// region sandstone
		sandstoneWall = new StaticWall("sandstone-wall") {{
			attributes.set(Attribute.sand, 1);
		}};
		sandstoneBoulder = new Prop("sandstone-boulder") {{
			variants = 2;
		}};
		sandstone = new Floor("sandstone", 4) {{
			wall = sandstoneWall;
			decoration = sandstoneBoulder;
		}};
		spongestone = new Floor("spongestone", 4) {{
			wall = sandstoneWall;
			decoration = sandstoneBoulder;
			blendGroup = sandstone;
		}};
		// endregion

		// region soil
		soilWall = new StaticWall("soil-wall") {{
			attributes.set(Attribute.sand, 1);
		}};
		soilBoulder = new Prop("soil-boulder") {{
			variants = 2;
		}};
		concretion = new OverlayFloor("concretion") {{
			variants = 4;
		}};
		soil = new Floor("soil", 4) {{
			wall = soilWall;
			decoration = soilBoulder;
		}};
		clay = new Floor("clay", 4) {{
			wall = soilWall;
			decoration = soilBoulder;
			blendGroup = soil;
		}};
		// endregion

		// region andesite
		andesiteWall = new StaticWall("andesite-wall") {{
			attributes.set(Attribute.sand, 1);
		}};
		andesiteBoulder = new Prop("andesite-boulder") {{
			variants = 2;
		}};
		andesitePlate = new OverlayFloor("andesite-plate") {{
			variants = 4;
		}};
		andesite = new Floor("andesite", 4) {{
			wall = andesiteWall;
			decoration = andesiteBoulder;
		}};
		striatedAndesite = new Floor("striated-andesite", 4) {{
			wall = andesiteWall;
			decoration = andesiteBoulder;
			blendGroup = andesite;
		}};
		// endregion

		// region solvent
		solventIceWall = new StaticWall("solvent-ice-wall");
		solventIceBoulder = new Prop("solvent-ice-boulder") {{
			variants = 2;
		}};
		solventShards = new OverlayFloor("solvent-shards") {{
			variants = 4;
		}};
		solventRegular = new Floor("solvent-regular", 0) {{
			cacheLayer = CacheLayer.water;
			isLiquid = true;
			liquidDrop = SWLiquids.solvent;
			wall = solventIceWall;
			decoration = solventIceBoulder;
		}};
		shallowSolvent = new Floor("shallow-solvent") {{
			cacheLayer = CacheLayer.water;
			isLiquid = true;
			liquidDrop = SWLiquids.solvent;
			wall = solventIceWall;
			decoration = solventIceBoulder;
		}};
		shallowerSolvent = new Floor("shallower-solvent") {{
			cacheLayer = CacheLayer.water;
			isLiquid = true;
			liquidDrop = SWLiquids.solvent;
			wall = solventIceWall;
			decoration = solventIceBoulder;
		}};
		solventIce = new Floor("solvent-ice", 4) {{
			wall = solventIceWall;
			decoration = solventIceBoulder;
		}};
		solventSnow = new Floor("solvent-snow", 4) {{
			wall = solventIceWall;
			decoration = solventIceBoulder;
		}};
		// endregion

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


		limiter = new Limiter("limiter");


		multiFloor = new MultiFloor("multi-floor");



		multiOverlay = new MultiOverlayFloor("multi-overlay");



		multiWall = new MultiStaticWall("multi-wall");



	}
}
