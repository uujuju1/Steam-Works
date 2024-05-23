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
			try{
				Reflect.set(MenuFragment.class, Vars.ui.menufrag, "renderer", new SWMenuRenderer());
			}catch(Exception ex){
				Log.err("Failed to replace renderer", ex);
			}
			ModSettings.load();
			EventHints.initHints();
		});
		Events.on(EventType.MusicRegisterEvent.class, e -> ModMusic.load());
	}
}
