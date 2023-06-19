package sw.content;

import mindustry.content.*;
import mindustry.type.*;

public class SWSectorPresets {
	public static SectorPreset hotspot, greatLake;

	public static void load() {
		hotspot = new SectorPreset("hotspot", Planets.serpulo, 175) {{
			captureWave = 20;
			difficulty = 3;
		}};
		greatLake = new SectorPreset("great-lake", Planets.serpulo, 191) {{
			captureWave = 20;
			difficulty = 3;
		}};
	}
}
