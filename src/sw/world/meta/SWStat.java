package sw.world.meta;

import mindustry.world.meta.Stat;

public class SWStat {
  public static final Stat recipes = new Stat("sw-recipes");
  public static final Stat maxHeat = new Stat("sw-max-heat", SWStatCat.heat);
  public static final Stat heatTresh = new Stat("sw-heat-tresh", SWStatCat.heat);
}
