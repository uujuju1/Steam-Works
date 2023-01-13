package sw.world.recipes;

import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawDefault;

public class GenericRecipe {
  public ItemStack[] consumeItems = ItemStack.empty;
  public ItemStack[] outputItems = ItemStack.empty;
  public LiquidStack[] consumeLiquids = LiquidStack.empty;
  public LiquidStack[] outputLiquids = LiquidStack.empty;
  public Effect craftEffect = Fx.none;
  public Effect updateEffect = Fx.none;
  public DrawBlock drawer = new DrawDefault();
  public float consumePower = 1f;
  public float updateEffectChance = 0.03f;
  public float warmupSpeed = 0.019f;
  public float craftTime = 60f;

  public boolean checkUnlocked() {
    if (!Vars.state.isCampaign()) return true;
    for (ItemStack stack : consumeItems) if (!(stack.item.unlocked())) return false;
    for (LiquidStack stack : consumeLiquids) if (!(stack.liquid.unlocked())) return false;
    return true;
  }
}
