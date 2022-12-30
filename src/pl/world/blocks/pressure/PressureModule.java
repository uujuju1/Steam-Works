package pl.world.blocks.pressure;

import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.world.modules.BlockModule;

public class PressureModule extends BlockModule {
  public float pressure;

  public void setPressure(float value) {
    pressure = value;
  }
  public void addPressure(float value) {
    pressure += value;
  }
  public void subPressure(float value) {
    pressure -= value;
  }

  public void transfer(PressureModule to, float value, float insideRatio) {
    subPressure(value * Time.delta);
    to.addPressure(value * insideRatio * Time.delta);
  }
  public void transfer(PressureModule to, float insideRatio) {
    transfer(to, (pressure - to.pressure) * 0.4f, insideRatio);
  }

  @Override
  public void write(Writes write) {
    write.f(pressure);
  }
  @Override
  public void read(Reads read) {
    pressure = read.f();
  }
}
