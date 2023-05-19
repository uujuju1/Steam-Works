package sw.content;

import mindustry.content.*;
import mindustry.type.*;

public class SWSectorPresets {
	public static SectorPreset frozenHotspot;

	public static void load() {
		frozenHotspot = new SectorPreset("frozen-hotspot", Planets.serpulo, 175) {{
			captureWave = 36;
			difficulty = 3;
		}};
	}
}
