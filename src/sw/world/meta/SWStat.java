package sw.world.meta;

import mindustry.world.meta.*;

public class SWStat {
  public static final StatCat spin = new StatCat("sw-spin");

  public static final StatUnit spinMinute = new StatUnit("sw-spin-minute");

  public static final Stat spinResistance = new Stat("sw-spin-resistance", spin);

  public static final Stat recipes = new Stat("sw-recipes");
}
