package sw.content;

import arc.*;
import mindustry.game.*;
import mindustry.type.*;
import sw.dream.*;
import sw.dream.event.*;

public class SWSectorPresets {
	public static SectorPreset intro, piedmont, yggdrasil;

	public static void load() {
		intro = new SectorPreset("intro", SWPlanets.wendi, 69) {{
			captureWave = 10;
			alwaysUnlocked = true;
		}};
		piedmont = new SectorPreset("piedmont", SWPlanets.wendi, 3) {{
			captureWave = 15;
		}};
		yggdrasil = new SectorPreset("yggdrasil", SWPlanets.wendi, 0);
		Events.on(EventType.WorldLoadEvent.class, e -> {
			DreamCore.instance.event(null);
		});
		Events.on(EventType.SectorLaunchEvent.class, e -> {
			if (e.sector.preset == yggdrasil) {
				DreamCore.instance.event(new YggdrasilEvent());
			}
		});
	}
}
