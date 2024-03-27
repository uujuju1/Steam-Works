package sw;

import mindustry.mod.*;
import sw.core.*;
import sw.dream.*;
import sw.gen.*;

public class ModLoader extends Mod {
  public ModLoader() {
    ModEventHandler.init();
    new DreamCore();
  }

  @Override
  public void init() {
    super.init();
    SWVars.init();
  }

  @Override
  public void loadContent() {
    EntityRegistry.register();
    SWVars.loadContent();
  }
}
