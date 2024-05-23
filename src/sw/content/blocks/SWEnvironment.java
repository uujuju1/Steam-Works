package sw.content.blocks;

import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.meta.*;
import sw.content.*;

public class SWEnvironment {
	public static Block
	spinyTree, deadSpinyTree,

	oreNickel, oreIron,

	ash, fineAsh, ashWall, ashGraphite, scorchedTreeTrunk,
	fauna, denseFauna, faunaWall, leaflets,
	deadFauna, denseDeadFauna, deadFaunaWall, deadLeaflets,
	biomass, folliage, biomassWall, clumps,
	gravel, flatGravel, gravelWall, flint,
	roots, tangledRoots, rootsWall, tumbleweed,
	soil, clay, soilWall,
	marble, roughMarble, marbleWall, marbleGraphite, marbleBoulder, marbleSpike,

	solventRegular, shallowSolvent, shallowerSolvent,

	plate, plateCross, plateVent, plateDamaged, plateCrossDamaged, plateVentDamaged, plateWall;

	public static void load() {
		spinyTree = new TreeBlock("spiny-tree") {{
			attributes.set(Attribute.steam, 1);
		}};
		deadSpinyTree = new TreeBlock("dead-spiny-tree") {{
			attributes.set(Attribute.steam, 0.25f);
		}};


		oreNickel = new OreBlock(SWItems.nickel) {{
			variants = 4;
		}};
		oreIron = new OreBlock(SWItems.iron) {{
			variants = 4;
		}};


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

		tumbleweed = new Prop("tumbleweed") {{
			variants = 2;
		}};
		rootsWall = new StaticWall("roots-wall");
		roots = new Floor("roots", 4) {{
			wall = rootsWall;
			decoration = tumbleweed;
		}};
		tangledRoots = new Floor("tangled-roots", 3) {{
			wall = rootsWall;
			decoration = tumbleweed;
		}};

		soilWall = new StaticWall("soil-wall") {{
			attributes.set(Attribute.sand, 1);
		}};
		soil = new Floor("soil", 4) {{
			wall = soilWall;
		}};
		clay = new Floor("clay", 3) {{
			wall = soilWall;
		}};

		marbleWall = new StaticWall("marble-wall");
		marbleGraphite = new StaticWall("marble-graphite") {{
			itemDrop = Items.graphite;
		}};
		marbleBoulder = new Prop("marble-boulder") {{
			variants = 2;
		}};
		marbleSpike = new Prop("marble-spike") {{
			cacheLayer = CacheLayer.normal;
			destructible = true;
			solid = true;
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
	}
}
