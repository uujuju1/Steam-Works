package sw;

import mindustry.mod.Mod;

public class ModLoader extends Mod {
  public ModLoader() {

  }

  @Override
  public void loadContent() {
    SWVars.loadContent();
  }
}
