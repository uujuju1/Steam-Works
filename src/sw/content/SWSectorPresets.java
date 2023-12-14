package sw.content;

import mindustry.type.*;

public class SWSectorPresets {
	public static SectorPreset
		anthill, cLake,
		path,
	  shore,
		hotspot, greatLake, erosion, aurora;

	public static void load() {
		anthill = new SectorPreset("anthill", SWPlanets.wendi, 76) {{
			captureWave = 10;
			difficulty = 2;
		}};
		cLake = new SectorPreset("c-lake", SWPlanets.wendi, 12) {{
			captureWave = 20;
			difficulty = 4;
		}};
		path = new SectorPreset("path", SWPlanets.wendi, 19) {{
			captureWave = 30;
			difficulty = 5;
		}};
		shore = new SectorPreset("shore", SWPlanets.wendi, 55) {{
			captureWave = 30;
			difficulty = 5;
		}};

		hotspot = new SectorPreset("hotspot", SWPlanets.wendi, 1) {{
			captureWave = 20;
			difficulty = 2;
		}};
		greatLake = new SectorPreset("great-lake", SWPlanets.wendi, 56) {{
			captureWave = 20;
			difficulty = 3;
		}};
		erosion = new SectorPreset("erosion", SWPlanets.wendi, 91) {{
			captureWave = 40;
			difficulty = 4;
		}};
		aurora = new SectorPreset("aurora", SWPlanets.wendi, 66) {{
			captureWave = 30;
			difficulty = 4;
		}};
	}
}
