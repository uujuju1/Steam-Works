package sw.world.blocks.production;

import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.type.ItemStack;
import mindustry.world.blocks.production.GenericCrafter;

/**
 * @author Uujuju
 * crafts items based on erekir's impact drill
 */
public class StackCrafter extends GenericCrafter {
  public Effect stackCraftEffect = Fx.none;
  public int stacks = 3;

  public StackCrafter(String name) {
    super(name);
  }

  public class StackCrafterBuild extends GenericCrafterBuild {
    public int stack;

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
    public void write(Writes write) {
      super.write(write);
      write.i(stack);
    }
    @Override
    public void read(Reads read, byte revision) {
      super.read(read, revision);
      stack = read.i();
    }
  }
}
