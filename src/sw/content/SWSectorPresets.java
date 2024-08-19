package sw.content;

import arc.*;
import arc.struct.*;
import mindustry.*;
import mindustry.game.*;
import mindustry.type.*;
import sw.dream.*;
import sw.dream.event.*;
import sw.ui.dialog.*;

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

	public static void init() {
		new SectorLaunchDialog.SectorNode(SWPlanets.wendi.sectors.get(0), 0f, 0f, Seq.with(), () -> {
			new SectorLaunchDialog.SectorNode(SWPlanets.wendi.sectors.get(1), 74.5f, 0f, Seq.with(), () -> {
				new SectorLaunchDialog.SectorNode(SWPlanets.wendi.sectors.get(2), 74.5f, 74.5f, Seq.with(SWPlanets.wendi.sectors.get(0)), () -> {});
			});
		});
	}
}
