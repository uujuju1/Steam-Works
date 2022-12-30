package pl.world.meta;

import mindustry.world.meta.Stat;

public class PressureStat {
  public static final Stat
  minPressure = new Stat("sw-min-pressure", PressureStatCat.pressure),
  maxPressure = new Stat("sw-max-pressure", PressureStatCat.pressure);
}
