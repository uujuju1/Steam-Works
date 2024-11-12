package sw.world.meta;

import mindustry.world.meta.*;

public class SWStat {
  public static final StatCat spin = new StatCat("sw-spin");

  public static final StatUnit
    spinMinuteSecond = new StatUnit("sw-spin-minute-second"),
    spinMinute = new StatUnit("sw-spin-minute");

  public static final Stat
    spinOutput = new Stat("sw-spin-output", spin),
    spinOutputForce = new Stat("sw-spin-output-force", spin),
    spinResistance = new Stat("sw-spin-resistance", spin);

  public static final Stat recipes = new Stat("sw-recipes");
}
