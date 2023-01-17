package sw.world.modules;

import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.world.modules.BlockModule;

public class HeatModule extends BlockModule {
  public float heat;

  public float addHeat(float amount) {
    return heat+=amount;
  }
  public float subHeat(float amount) {
    return heat-=amount;
  }

  @Override public void write(Writes write) {
    write.f(heat);
  }
  @Override public void read(Reads read) {
    heat = read.f();
  }
}
