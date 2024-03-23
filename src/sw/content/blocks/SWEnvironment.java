package sw.content.blocks;

import mindustry.world.*;
import mindustry.world.blocks.environment.*;

public class SWEnvironment {
	public static Block
	ash, ashWall,
	fauna, faunaWall,
	deadFauna, deadFaunaWall,
	biomass, biomassWall,
	gravel, gravelWall,
	roots, rootsWall,
	soil, soilWall,
	marble, marbleWall,
	plate, plateCross, plateVent, plateWall;

	public static void load() {
		ashWall = new StaticWall("ash-wall");
		ash = new Floor("ash", 4) {{
			wall = ashWall;
		}};
		faunaWall = new StaticWall("fauna-wall");
		fauna = new Floor("fauna", 4) {{
			wall = faunaWall;
		}};
		deadFaunaWall = new StaticWall("dead-fauna-wall");
		deadFauna = new Floor("dead-fauna", 4) {{
			wall = deadFauna;
		}};
		biomassWall = new StaticWall("biomass-wall");
		biomass = new Floor("biomass", 4) {{
			wall = biomassWall;
		}};
		gravelWall = new StaticWall("gravel-wall");
		gravel = new Floor("gravel", 4) {{
			wall = gravelWall;
		}};
		rootsWall = new StaticWall("roots-wall");
		roots = new Floor("roots", 4) {{
			wall = rootsWall;
		}};
		soilWall = new StaticWall("soil-wall");
		soil = new Floor("soil", 4) {{
			wall = soilWall;
		}};
		marbleWall = new StaticWall("marble-wall");
		marble = new Floor("marble", 4) {{
			wall = marbleWall;
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
