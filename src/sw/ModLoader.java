package sw;

import arc.*;
import arc.util.*;
import mindustry.*;
import mindustry.ctype.*;
import mindustry.game.*;
import mindustry.mod.*;
import mindustry.ui.fragments.*;
import sw.annotations.Annotations.*;
import sw.entities.units.*;
import sw.gen.*;
import sw.graphics.*;
import sw.ui.*;

@SuppressWarnings("unused")
@EnsureLoad // Needed for the ContentRegionRegistry
@ProcessAssets // Needed for the asset classes
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
    Events.on(EventType.MusicRegisterEvent.class, e -> SWMusics.load());
    Events.on(EventType.ContentInitEvent.class, e -> Vars.content.each(c -> {
      if (c instanceof MappableContent content) SWContentRegionRegistry.load(content);
    }));
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
