package sw.content;

import mindustry.type.*;

public class SWSectorPresets {
	public static SectorPreset hotspot, greatLake;

	public static void load() {
		hotspot = new SectorPreset("hotspot", SWPlanets.aboba, 1) {{
			captureWave = 20;
			difficulty = 3;
		}};
		greatLake = new SectorPreset("great-lake", SWPlanets.aboba, 56) {{
			captureWave = 20;
			difficulty = 3;
		}};
	}
}
