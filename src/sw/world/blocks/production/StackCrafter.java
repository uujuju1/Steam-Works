package sw.world.blocks.production;

import arc.Core;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.gen.Building;
import mindustry.graphics.Pal;
import mindustry.type.ItemStack;
import mindustry.ui.Bar;
import mindustry.world.blocks.production.GenericCrafter;
import pl.world.blocks.pressure.*;

/**
 * @author Uujuju
 * crafts items based on erekir's impact drill
 */
public class StackCrafter extends GenericCrafter implements PressureBlock {
  public Pressure pressureC = new Pressure();
  public Effect stackCraftEffect = Fx.none;
  public int stacks = 3;

  public StackCrafter(String name) {
    super(name);
  }

  @Override public Pressure getPressure() {
    return pressureC;
  }

  @Override
  public void setBars() {
    super.setBars();
    addBar("pressure", (StackCrafterBuild entity) -> new Bar(Core.bundle.get("bar.pressure"), Pal.accent, entity::pressureMap));
  }
  @Override
  public void setStats() {
    super.setStats();
    addPressureStats(stats);
  }

  public class StackCrafterBuild extends GenericCrafterBuild implements PressureBuild<StackCrafter> {
    public PressureModule module = new PressureModule();
    public int stack;

    @Override public StackCrafter getBlock() {
      return (StackCrafter) block;
    }
    @Override public PressureModule getModule() {
      return module;
    }

    @Override public boolean acceptsPressure(Building build, float amount) {
      return amount + getPressure() < getPressureC().maxPressure;
    }

    @Override
    public void craft() {
      consume();
      stack++;
      if (stack >= stacks) if (outputItems != null) {
        for (ItemStack output : outputItems) {
          for (int i = 0; i < output.amount * stacks; i++) offload(output.item);
        }
        stack = 0;
        stackCraftEffect.at(x, y);
      }
      if(wasVisible) craftEffect.at(x, y);
      progress %= 1f;
    }

    @Override
    public void updateTile() {
      super.updateTile();
      updatePressure(self());
    }

    @Override
    public void write(Writes write) {
      super.write(write);
      write.i(stack);
      module.write(write);
    }
    @Override
    public void read(Reads read, byte revision) {
      super.read(read, revision);
      stack = read.i();
      module.read(read);
    }
  }
}
