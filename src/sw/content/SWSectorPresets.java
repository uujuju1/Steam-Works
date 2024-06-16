package sw.content;

import mindustry.type.*;

public class SWSectorPresets {
	public static SectorPreset intro, piedmont;

	public static void load() {
		intro = new SectorPreset("intro", SWPlanets.wendi, 69) {{
			captureWave = 10;
			alwaysUnlocked = true;
		}};
		piedmont = new SectorPreset("piedmont", SWPlanets.wendi, 3) {{
			captureWave = 15;
		}};
	}
}
