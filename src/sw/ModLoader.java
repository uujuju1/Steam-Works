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
      if (SWVars.isMod && !Vars.headless) Core.app.post(SWShaders::load);
    });
    Events.on(EventType.MusicRegisterEvent.class, e -> SWMusics.load());
    Events.on(EventType.ContentInitEvent.class, e -> {
      if (Core.settings.getBool("sw-hide-maps", true)) {
        Vars.content.sectors().each(this::isMod, sector -> Vars.maps.all().remove(map -> map.file == sector.generator.map.file));
      }
      Vars.content.each(c -> {
        if (isMod(c)) SWContentRegionRegistry.load(c);
      });
    });
  }

  @Override
  public void init() {
    super.init();
    SWVars.init();
  }
  
  public boolean isMod(Content c) {
    return c.minfo.mod != null && c.minfo.mod.name.equals("sw");
  }

  @Override
  public void loadContent() {
    EntityMaps.load();
    SWVars.loadContent();
  }
}
