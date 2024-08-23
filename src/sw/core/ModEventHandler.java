package sw.core;

import arc.*;
import arc.util.*;
import mindustry.*;
import mindustry.game.*;
import mindustry.ui.fragments.*;
import sw.audio.*;
import sw.graphics.*;
import sw.ui.*;

public class ModEventHandler {
	public static void init() {
		Events.on(EventType.ClientLoadEvent.class, e -> {
			if (Core.settings.getBool("sw-menu-enabled", true)) {
				try {
					Reflect.set(MenuFragment.class, Vars.ui.menufrag, "renderer", new SWMenuRenderer());
				} catch (Exception ex) {
					Log.err("Failed to replace renderer", ex);
				}
			}
			EventHints.initHints();
		});
//		Events.run(EventType.Trigger.update, () -> {
//			ScriptedSectorPreset.scripts.get(Vars.state.map.name(), () -> {}).run();
//		});
		Events.on(EventType.MusicRegisterEvent.class, e -> ModMusic.load());
	}
}
