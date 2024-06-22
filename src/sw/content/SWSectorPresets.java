package sw.content;

import mindustry.type.*;

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
//		yggdrasil = new SectorPreset("yggdrasil", SWPlanets.unknown, 0) {{
//			Events.on(EventType.WorldLoadEvent.class, e -> {
//				if (DreamCore.instance.currentEvent instanceof YggdrasilEvent event && event.canLeave) DreamCore.instance.event(null);
//			});
//			rules = r -> {
//				DreamCore.instance.event(new YggdrasilEvent());
//			};
//		}};
	}
}
