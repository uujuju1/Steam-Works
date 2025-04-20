package sw;

import arc.*;
import arc.util.*;
import mindustry.*;
import mindustry.game.*;
import mindustry.mod.*;
import mindustry.ui.fragments.*;
import sw.audio.*;
import sw.entities.units.*;
import sw.graphics.*;
import sw.ui.*;
//import sw.gen.*;

@SuppressWarnings("unused")
public class ModLoader extends Mod {
  public ModLoader() {
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
		Events.on(EventType.FileTreeInitEvent.class, e -> {
      if (SWVars.isMod) Core.app.post(SWShaders::load);
    });
    Events.on(EventType.MusicRegisterEvent.class, e -> ModMusic.load());
  }

  @Override
  public void init() {
    super.init();
    SWVars.init();
  }

  @Override
  public void loadContent() {
    EntityMaps.load();
    SWVars.loadContent();
  }
}
