package sw;

import mindustry.mod.*;
import sw.core.*;
//import sw.gen.*;

@SuppressWarnings("unused")
public class ModLoader extends Mod {
  public ModLoader() {
    ModEventHandler.init();
  }

  @Override
  public void init() {
    super.init();
    SWVars.init();
  }

  @Override
  public void loadContent() {
//    EntityRegistry.register();
    SWVars.loadContent();
  }
}
