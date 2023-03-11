package sw.world.heat;

import mindustry.world.meta.StatUnit;
import mindustry.world.meta.Stats;
import sw.world.meta.SWStat;

public interface HeatBlockI {
  HeatConfig heatConfig();

  default void heatStats(Stats stats) {
    stats.add(SWStat.maxHeat, heatConfig().maxHeat, StatUnit.degrees);
    stats.add(SWStat.minHeat, heatConfig().minHeat, StatUnit.degrees);
    stats.add(SWStat.heatEmissivity, heatConfig().heatEmissivity, StatUnit.degrees);
    stats.add(SWStat.heatLoss, heatConfig().heatEmissivity, StatUnit.degrees);
    stats.add(SWStat.acceptsHeat, heatConfig().acceptHeat);
    stats.add(SWStat.outputsHeat, heatConfig().outputHeat);
  }
}
