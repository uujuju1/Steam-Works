package sw.world.meta;

import mindustry.world.meta.*;

public class SWStat {
  public static final Stat recipes = new Stat("sw-recipes");

//  heat
  public static final Stat
    shield = new Stat("sw-shield", StatCat.general),

    maxHeat = new Stat("sw-max-heat", SWStatCat.heat),
    minHeat = new Stat("sw-min-heat", SWStatCat.heat),
    heatEmissivity = new Stat("sw-heat-emissivity", SWStatCat.heat),
    heatLoss = new Stat("sw-heat-loss", SWStatCat.heat),
    heatUse = new Stat("sw-heat-use", SWStatCat.heat),
    heatUseOnce = new Stat("sw-heat-use-once", SWStatCat.heat),
    outputHeat = new Stat("sw-output-heat", SWStatCat.heat),
    acceptsHeat = new Stat("sw-accepts-heat", SWStatCat.heat),
    outputsHeat = new Stat("sw-outputs-heat", SWStatCat.heat),
    heatTresh = new Stat("sw-heat-tresh", SWStatCat.heat),

//  force
    maxForce = new Stat("sw-max-force", SWStatCat.force),
    beltSize = new Stat("sw-belt-in", SWStatCat.force),
    baseResistance = new Stat("sw-resistance", SWStatCat.force),
    resistanceScl = new Stat("sw-resistance-scl", SWStatCat.force),
    minSpeedConsume = new Stat("sw-speed-min", SWStatCat.force),
    maxSpeedConsume = new Stat("sw-speed-max", SWStatCat.force),
    strengthConsume = new Stat("sw-strength-cons", SWStatCat.force);
}
