package sw;

import arc.*;
import mindustry.game.*;
import mindustry.mod.Mod;
import sw.ui.*;

public class ModLoader extends Mod {
  public ModLoader() {
    Events.on(EventType.ClientLoadEvent.class, e -> ModSettings.load());
  }

  @Override
  public void init() {
    super.init();
    SWVars.init();
  }

  @Override
  public void loadContent() {
    SWVars.loadContent();
  }
}
