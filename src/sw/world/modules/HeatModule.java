package sw.world.modules;

import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.world.modules.BlockModule;

public class HeatModule extends BlockModule {
  public float heat;

  public void addHeat(float amount) {
    heat+=amount;
  }
  public void subHeat(float amount) {
    heat-=amount;
  }
  public void setHeat(float amount) {
    heat = amount;
  }

  @Override public void write(Writes write) {
    write.f(heat);
  }
  @Override public void read(Reads read) {
    heat = read.f();
  }
}
