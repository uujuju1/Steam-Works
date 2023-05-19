package sw.world.meta;

import mindustry.world.meta.*;

public class HeatConfig {
  public float minHeat = -200f, maxHeat = 2200f, heatEmissivity =  0.4f, heatLoss = 0f;
  public boolean acceptHeat = true, outputHeat = true;

  public HeatConfig() {
    if (minHeat > maxHeat) throw new IllegalArgumentException("Min heat cannot be higher than max heat");
  }

  public void heatStats(Stats stats) {
    stats.add(SWStat.maxHeat, maxHeat, StatUnit.degrees);
    stats.add(SWStat.minHeat, minHeat, StatUnit.degrees);
    stats.add(SWStat.heatEmissivity, heatEmissivity, StatUnit.degrees);
    stats.add(SWStat.heatLoss, heatEmissivity, StatUnit.degrees);
    stats.add(SWStat.acceptsHeat, acceptHeat);
    stats.add(SWStat.outputsHeat, outputHeat);
  }

  public boolean connects() {return acceptHeat || outputHeat;}
}
