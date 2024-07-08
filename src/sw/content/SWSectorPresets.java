package sw.content;

import arc.*;
import mindustry.*;
import mindustry.game.*;
import mindustry.type.*;
import sw.dream.*;
import sw.dream.event.*;

public class SWSectorPresets {
	public static SectorPreset
		intro, piedmont, trinity, light,
		yggdrasil;

	public static void load() {
		intro = new SectorPreset("intro", SWPlanets.wendi, 69) {{
			captureWave = 10;
			alwaysUnlocked = true;
		}};
		piedmont = new SectorPreset("piedmont", SWPlanets.wendi, 12) {{
			captureWave = 15;
		}};
		trinity = new SectorPreset("trinity", SWPlanets.wendi, 87) {{
			captureWave = 20;
		}};
		light = new SectorPreset("light", SWPlanets.wendi, 40) {{
			captureWave = 20;
		}};
		yggdrasil = new SectorPreset("yggdrasil", SWPlanets.wendi, 0);
		Events.on(EventType.WorldLoadEvent.class, e -> {
			DreamCore.instance.event(null);
			if (Vars.state.getSector() == yggdrasil.sector) DreamCore.instance.event(new YggdrasilEvent());
		});
	}
}
