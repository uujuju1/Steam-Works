package pl.world.blocks.pressure;

import mindustry.world.meta.Stats;
import pl.world.meta.PressureStat;

public interface PressureBlock {
  default Pressure getPressure() {return null;}
  default void addPressureStats(Stats stats) {
    stats.add(PressureStat.minPressure, getPressure().minPressure);
    stats.add(PressureStat.maxPressure, getPressure().maxPressure);
  }
}
