package sw.world.consumers;

import arc.math.Mathf;
import arc.util.Time;
import mindustry.gen.Building;
import mindustry.world.consumers.Consume;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.Stats;
import sw.world.interfaces.HasHeat;
import sw.world.meta.SWStat;

public class ConsumeHeat extends Consume {
  public float amount;
  public float min;
  public boolean scales;

  public ConsumeHeat(float amount, float min, boolean scales) {
    this.amount = amount;
    this.min = min;
    this.scales = scales;
  }

  @Override public void update(Building build) {
    if (build instanceof HasHeat next) next.module().subHeat(amount * efficiencyMultiplier(build) * Time.delta);
  }

  @Override public float efficiency(Building build) {
    return build instanceof HasHeat next && next.module().heat >= min ? 1f : 0f;
  }
  @Override public float efficiencyMultiplier(Building build) {
    if (!scales) return 1f;
    return build instanceof HasHeat next ? Mathf.map(next.module().heat, 0, min, 0, 1) : 0f;
  }

  @Override public void display(Stats stats) {
    stats.add(SWStat.heatUse, amount * Time.toSeconds, StatUnit.degrees);
  }
}
