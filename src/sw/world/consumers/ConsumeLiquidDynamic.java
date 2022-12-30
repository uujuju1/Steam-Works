package sw.world.consumers;

import arc.func.*;
import arc.scene.ui.layout.*;
import mindustry.ui.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import mindustry.world.consumers.*;

public class ConsumeLiquidDynamic<T extends Building> extends Consume {
  public Func<T, LiquidStack[]> liquids;

  public ConsumeLiquidDynamic(Func<T, LiquidStack[]> liquids) {this.liquids = liquids;}

  @Override public void apply(Block block) {block.hasLiquids = true;}
  @Override public void display(Stats stats) {}

  @Override
  public void build(Building build, Table table) {
    LiquidStack[][] current = {liquids.get((T) build)};

    table.table(cont -> {
      table.update(() -> {
        if (current[0] != liquids.get((T) build)) {
          current[0] = liquids.get((T) build);
          rebuild(build, cont);
        }
      });
      rebuild(build, cont);
    });
  }

  private void rebuild(Building build, Table table) {
    table.clear();
    int i = 0;

    for (LiquidStack stack : liquids.get((T) build)) {
      table.add(new ReqImage(
              stack.liquid.uiIcon,
              () -> build.liquids != null && build.liquids.get(stack.liquid) > 0
      )).padRight(8).left();
      if (++i % 4 == 0) table.row();
    }
  }

  @Override public void update(Building build) {
    for (LiquidStack stack : liquids.get((T) build)) build.liquids.remove(stack.liquid, stack.amount * build.edelta());
  }

  @Override
  public float efficiency(Building build) {
    float efficiencyScl = build.edelta();
    if (efficiencyScl <= 0.00000001f) return 0f;
    if (liquids.get((T) build).length == 0) return 1f;

    float efficiencyAverage = 0f;
    for (LiquidStack stack : liquids.get((T) build)) efficiencyAverage += Math.min(build.liquids.get(stack.liquid) / (stack.amount * efficiencyScl), 1f);

    return efficiencyAverage/liquids.get((T) build).length;
  }
}