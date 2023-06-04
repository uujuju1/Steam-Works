package sw;

import mindustry.mod.*;
import sw.core.*;

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
    SWVars.loadContent();
  }
}
