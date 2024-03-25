package sw.content.blocks;

import mindustry.world.*;
import mindustry.world.blocks.environment.*;

public class SWEnvironment {
	public static Block
	spinyTree, deadSpinyTree,

	ash, fineAsh, ashWall,
	fauna, denseFauna, faunaWall,
	deadFauna, denseDeadFauna, deadFaunaWall,
	biomass, folliage, biomassWall,
	gravel, flatGravel, gravelWall,
	roots, tangledRoots, rootsWall,
	soil, clay, soilWall,
	marble, roughMarble, marbleWall, marbleBoulder,
	plate, plateCross, plateVent, plateWall;

	public static void load() {
		spinyTree = new TreeBlock("spiny-tree");
		deadSpinyTree = new TreeBlock("dead-spiny-tree");

		ashWall = new StaticWall("ash-wall");
		ash = new Floor("ash", 4) {{
			wall = ashWall;
		}};
		fineAsh = new Floor("fine-ash", 3) {{
			wall = ashWall;
		}};

		faunaWall = new StaticWall("fauna-wall");
		fauna = new Floor("fauna", 4) {{
			wall = faunaWall;
		}};
		denseFauna = new Floor("dense-fauna", 3) {{
			wall = faunaWall;
		}};

		deadFaunaWall = new StaticWall("dead-fauna-wall");
		deadFauna = new Floor("dead-fauna", 4) {{
			wall = deadFauna;
		}};
		denseDeadFauna = new Floor("dense-dead-fauna", 3) {{
			wall = deadFauna;
		}};

		biomassWall = new StaticWall("biomass-wall");
		biomass = new Floor("biomass", 4) {{
			wall = biomassWall;
		}};
		folliage = new Floor("folliage", 3) {{
			wall = biomassWall;
		}};

		gravelWall = new StaticWall("gravel-wall");
		gravel = new Floor("gravel", 4) {{
			wall = gravelWall;
		}};
		flatGravel = new Floor("flat-gravel", 3) {{
			wall = gravelWall;
		}};

		rootsWall = new StaticWall("roots-wall");
		roots = new Floor("roots", 4) {{
			wall = rootsWall;
		}};
		tangledRoots = new Floor("tangled-roots", 3) {{
			wall = rootsWall;
		}};

		soilWall = new StaticWall("soil-wall");
		soil = new Floor("soil", 4) {{
			wall = soilWall;
		}};
		clay = new Floor("clay", 3) {{
			wall = soilWall;
		}};

		marbleWall = new StaticWall("marble-wall");
		marbleBoulder = new Prop("marble-boulder") {{
			variants = 2;
		}};
		marble = new Floor("marble", 4) {{
			decoration = marbleBoulder;
			wall = marbleWall;
		}};
		roughMarble = new Floor("rough-marble", 3) {{
			wall = marbleWall;
			decoration = marbleBoulder;
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
		}};
	}
}
