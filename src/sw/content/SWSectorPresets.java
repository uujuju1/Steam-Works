package sw.content;

import mindustry.content.*;
import mindustry.type.*;

public class SWSectorPresets {
	public static SectorPreset hotspot;

	public static void load() {
		hotspot = new SectorPreset("hotspot", Planets.serpulo, 175) {{
			captureWave = 36;
			difficulty = 3;
		}};
	}
}
