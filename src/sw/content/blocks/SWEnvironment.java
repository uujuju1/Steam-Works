package sw.content.blocks;

import arc.math.geom.*;
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


	oreNickel, oreIron, fissure, oreGraphite,


	ash, fineAsh, ashWall, ashGraphite, scorchedTreeTrunk,

	fauna, denseFauna, faunaWall, leaflets,

	deadFauna, denseDeadFauna, deadFaunaWall, deadLeaflets,

	biomass, folliage, biomassWall, clumps,

	gravel, flatGravel, gravelWall, flint,

	rootsWall, tumbleweed, rootVent,
	giantRoots, roots, tangledRoots,

	concretion, soilBoulder, soilWall,
	soil, clay, dryRiver,

	marble, roughMarble, marbleWall, marbleGraphite, marbleBoulder, marbleSpike,


	solventRegular, shallowSolvent, shallowerSolvent,


	plate, plateCross, plateVent, plateDamaged, plateCrossDamaged, plateVentDamaged, plateWall,

	multiFloor, multiOverlay, multiWall;

	public static void load() {
		spinyTree = new TreeBlock("spiny-tree") {{
			attributes.set(Attribute.steam, 1);
		}};
		deadSpinyTree = new TreeBlock("dead-spiny-tree") {{
			attributes.set(Attribute.steam, 0.25f);
		}};


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


		// region ash
		scorchedTreeTrunk = new Prop("scorched-tree-trunk") {{
			variants = 2;
		}};
		ashWall = new StaticWall("ash-wall") {{
			attributes.set(Attribute.sand, 1);
		}};
		ashGraphite = new StaticWall("ash-graphite") {{
			itemDrop = Items.graphite;
		}};
		ash = new Floor("ash", 4) {{
			wall = ashWall;
			decoration = scorchedTreeTrunk;
		}};
		fineAsh = new Floor("fine-ash", 3) {{
			wall = ashWall;
			decoration = scorchedTreeTrunk;
		}};
		// endregion

		leaflets = new Prop("leaflets") {{
			variants = 2;
		}};
		faunaWall = new StaticWall("fauna-wall");
		fauna = new Floor("fauna", 4) {{
			wall = faunaWall;
			decoration = leaflets;
		}};
		denseFauna = new Floor("dense-fauna", 3) {{
			wall = faunaWall;
			decoration = leaflets;
		}};

		deadLeaflets = new Prop("dead-leaflets") {{
			variants = 2;
		}};
		deadFaunaWall = new StaticWall("dead-fauna-wall");
		deadFauna = new Floor("dead-fauna", 4) {{
			wall = deadFauna;
			decoration = deadLeaflets;
		}};
		denseDeadFauna = new Floor("dense-dead-fauna", 3) {{
			wall = deadFauna;
			decoration = deadLeaflets;
		}};

		clumps = new Prop("clumps") {{
			variants = 2;
		}};
		biomassWall = new StaticWall("biomass-wall");
		biomass = new Floor("biomass", 4) {{
			wall = biomassWall;
			decoration = clumps;
		}};
		folliage = new Floor("folliage", 3) {{
			wall = biomassWall;
			decoration = clumps;
		}};

		// region gravel
		flint = new OverlayFloor("flint") {{
			variants = 4;
		}};
		gravelWall = new StaticWall("gravel-wall") {{
			attributes.set(Attribute.sand, 0.5f);
		}};
		gravel = new Floor("gravel", 4) {{
			wall = gravelWall;
		}};
		flatGravel = new Floor("flat-gravel", 3) {{
			wall = gravelWall;
		}};
		// endregion

		// region roots
		tumbleweed = new Prop("tumbleweed") {{
			variants = 2;
		}};
		rootsWall = new StaticWall("roots-wall");
		giantRoots = new Floor("giant-roots", 4) {{
			wall = rootsWall;
			decoration = tumbleweed;
		}};
		roots = new Floor("roots", 4) {{
			wall = rootsWall;
			decoration = tumbleweed;
		}};
		tangledRoots = new Floor("tangled-roots", 4) {{
			wall = rootsWall;
			decoration = tumbleweed;
		}};
		rootVent = new GasVent("root-vent") {{
			density = tile -> {
				int next = 0;
				for(Point2 point : Geometry.d4) {
					if (tile.nearby(point).overlay() == this) next++;
				}
				return Math.max(0, next - 1);
			};
			maxDensity = 4;
			variants = 4;
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
		dryRiver = new Floor("dry-river", 4) {{
			wall = soilWall;
			decoration = soilBoulder;
		}};
		soil = new Floor("soil", 4) {{
			wall = soilWall;
			decoration = soilBoulder;
			blendGroup = dryRiver;
		}};
		clay = new Floor("clay", 4) {{
			wall = soilWall;
			decoration = soilBoulder;
			blendGroup = dryRiver;
		}};
		// endregion

		// region marble
		marbleWall = new StaticWall("marble-wall");
		marbleGraphite = new StaticWall("marble-graphite") {{
			itemDrop = Items.graphite;
		}};
		marbleBoulder = new Prop("marble-boulder") {{
			variants = 2;
		}};
		marbleSpike = new TallBlock("marble-spike") {{
			cacheLayer = CacheLayer.normal;
			variants = 4;
		}};
		marble = new Floor("marble", 4) {{
			decoration = marbleBoulder;
			wall = marbleWall;
		}};
		roughMarble = new Floor("rough-marble", 3) {{
			wall = marbleWall;
			decoration = marbleBoulder;
		}};
		// endregion


		// region solvent
		solventRegular = new Floor("solvent-regular", 0) {{
			cacheLayer = CacheLayer.water;
			isLiquid = true;
			liquidDrop = SWLiquids.solvent;
		}};
		shallowSolvent = new Floor("shallow-solvent") {{
			cacheLayer = CacheLayer.water;
			isLiquid = true;
			liquidDrop = SWLiquids.solvent;
		}};
		shallowerSolvent = new Floor("shallower-solvent") {{
			cacheLayer = CacheLayer.water;
			isLiquid = true;
			liquidDrop = SWLiquids.solvent;
		}};
		// endregion


		// region plate
		plateWall = new StaticWall("plate-wall");
		plate = new Floor("plate", 4) {{
			wall = plateWall;
		}};
		plateVent = new Floor("plate-vent", 0) {{
			wall = plateWall;
		}};
		plateCross = new Floor("plate-cross", 0) {{
			wall = plateWall;
			blendGroup = plate;
		}};
		plateDamaged = new Floor("plate-damaged", 4) {{
			wall = plateWall;
		}};
		plateVentDamaged = new Floor("plate-vent-damaged", 0) {{
			wall = plateWall;
		}};
		plateCrossDamaged = new Floor("plate-cross-damaged", 0) {{
			wall = plateWall;
			blendGroup = plate;
		}};
		// endregion

		multiFloor = new MultiFloor("multi-floor");



		multiOverlay = new MultiOverlayFloor("multi-overlay");



		multiWall = new MultiStaticWall("multi-wall");



	}
}
