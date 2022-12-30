package pl.world.consumers;

import arc.Core;
import arc.scene.ui.layout.Table;
import pl.world.blocks.pressure.PressureBuild;
import mindustry.ui.ReqImage;
import mindustry.gen.Building;
import mindustry.world.consumers.Consume;

public class ConsumePressureTrigger extends Consume {
  public float amount;

  public ConsumePressureTrigger(float amount) {
    this.amount = amount;
  }

  private boolean valid(PressureBuild build) {
    return build.getModule().pressure >= amount;
  }
  private void rebuild(Building build, Table table) {
    table.clear();
    table.add(new ReqImage(
            Core.atlas.find("sw-icon-pressure"),
            () -> valid((PressureBuild) build))
    );
  }

  @Override public void trigger(Building build) {
    if (build instanceof PressureBuild) ((PressureBuild) build).getModule().subPressure(amount);
  }

  @Override public void build(Building build, Table table) {
    if (build instanceof PressureBuild) table.table(cont -> table.update(() -> rebuild(build, cont)));
  }

  @Override public float efficiency(Building build) {
    if (build instanceof PressureBuild) return valid((PressureBuild) build) ? 1f : 0f;
    return 0f;
  }
}
