package sw.world.meta;

import mindustry.world.meta.*;

public class SWStat {
  public static final StatCat tension = new StatCat("sw-tension");

  public static final StatUnit tensionUnits = new StatUnit("sw-tension-units");

  public static final Stat recipes = new Stat("sw-recipes");
  public static final Stat shield = new Stat("sw-shield", StatCat.general);

  public static final Stat
    maxTension = new Stat("sw-max-tension", tension),
    staticTension = new Stat("sw-static-tension", tension),
    requiredTension = new Stat("sw-required-tension", tension);

}
