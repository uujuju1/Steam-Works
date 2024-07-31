package sw.content;

import arc.*;
import mindustry.*;
import mindustry.game.*;
import mindustry.type.*;
import sw.dream.*;
import sw.dream.event.*;

public class SWSectorPresets {
	public static SectorPreset
		yggdrasil;

	public static void load() {
		yggdrasil = new SectorPreset("yggdrasil", SWPlanets.unknown, 0);
		Events.on(EventType.WorldLoadEvent.class, e -> {
			DreamCore.instance.event(null);
			if (Vars.state.getSector() == yggdrasil.sector) DreamCore.instance.event(new YggdrasilEvent());
		});
	}
}
