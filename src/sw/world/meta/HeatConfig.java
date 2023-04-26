package sw.world.meta;

public class HeatConfig implements Cloneable {
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

  public HeatConfig copy() {
    try {
      return (HeatConfig) clone();
    } catch (CloneNotSupportedException a) {
      throw new RuntimeException("no clones?", a);
    }
  }

  public boolean connects() {return acceptHeat || outputHeat;}
}
