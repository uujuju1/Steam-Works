package sw.core;

import arc.*;
import mindustry.game.*;

public class ModEventHandler {
	public static void init() {
		Events.on(EventType.ClientLoadEvent.class, e -> ModSettings.load());
		Events.on(EventType.MusicRegisterEvent.class, e -> ModMusic.load());
	}
}
