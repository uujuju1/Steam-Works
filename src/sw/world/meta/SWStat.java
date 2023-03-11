package sw.world.meta;

import mindustry.world.meta.Stat;

public class SWStat {
  public static final Stat recipes = new Stat("sw-recipes");

  public static final Stat maxHeat = new Stat("sw-max-heat", SWStatCat.heat);
  public static final Stat minHeat = new Stat("sw-min-heat", SWStatCat.heat);

  public static final Stat heatEmissivity = new Stat("sw-heat-emissivity", SWStatCat.heat);
  public static final Stat heatLoss = new Stat("sw-heat-loss", SWStatCat.heat);

  public static final Stat heatUse = new Stat("sw-heat-use", SWStatCat.heat);
  public static final Stat outputHeat = new Stat("sw-output-heat", SWStatCat.heat);

  public static final Stat acceptsHeat = new Stat("sw-accepts-heat", SWStatCat.heat);
  public static final Stat outputsHeat = new Stat("sw-outputs-heat", SWStatCat.heat);

  public static final Stat heatTresh = new Stat("sw-heat-tresh", SWStatCat.heat);
}
