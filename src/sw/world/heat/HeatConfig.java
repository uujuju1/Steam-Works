package sw.world.heat;

public class HeatConfig {
  public float minHeat, maxHeat, heatEmissivity, heatLoss;
  public boolean acceptHeat, outputHeat;

  public HeatConfig(float minHeat, float maxHeat, float heatEmissivity, float heatLoss, boolean acceptHeat, boolean outputHeat) {
    if (minHeat > maxHeat) throw new IllegalArgumentException("Min heat cannot be higher than max heat");
    this.minHeat = minHeat;
    this.maxHeat = maxHeat;
    this.heatEmissivity = heatEmissivity;
    this.heatLoss = heatLoss;
    this.acceptHeat = acceptHeat;
    this.outputHeat = outputHeat;
  }

  public boolean connects() {return acceptHeat || outputHeat;}
}
