package sw.entities.comp;

import arc.math.Mathf;
import arc.util.*;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.*;
import mindustry.type.ItemStack;
import mindustry.world.blocks.payloads.BuildPayload;
import sw.entities.SWEntityMapping;
import sw.type.SWUnitType;

/**
 * @author Uujuju
 * A unit fused with a genericCrafter.
 * Can't use liquids or power.
 */
public class CrafterUnit extends BuildingTetherPayloadUnit {
  public float progress, totalProgress, warmup;

  @Override public String toString() {
    return "CrafterUnit#" + id;
  }
  @Override public SWUnitType type() {
    return (SWUnitType) super.type();
  }
  @Override public int classId() {
    return SWEntityMapping.idMap.get(getClass());
  }

  @Override
  public void update() {
    super.update();
    boolean crafting = true;
    if (payloads.isEmpty()) return;
    if (payloads.first() instanceof BuildPayload payload && type().recipe != null && payload.build.items != null) {
      for (ItemStack stack : type().recipe.consumeItems) {
        if (payload.build.items().get(stack.item) < stack.amount) crafting = false;
      }
      for (ItemStack stack : type().recipe.outputItems) {
        if (payload.build.items().get(stack.item) > payload.build.block.itemCapacity - stack.amount) crafting = false;
      }

      if (crafting) {
        progress += Time.delta;
        totalProgress += Time.delta;
        warmup = Mathf.approachDelta(warmup, 1, type().recipe.warmupSpeed);

        if (Mathf.chance(type().recipe.updateEffectChance)) type().recipe.updateEffect.at(this);

        if (progress >= type().recipe.craftTime) {
          progress %= 1f;
          for (ItemStack stack : type().recipe.consumeItems) payload.build.items().remove(stack);
          for (ItemStack stack : type().recipe.outputItems) payload.build.items().add(stack.item, stack.amount);
          type().recipe.craftEffect.at(this);
        }
      }
    }
  }

  @Override public boolean canPickup(Building build) {
    return build.block.size == 2 && super.canPickup(build);
  }

  @Override
  public void read(Reads read) {
    super.read(read);
    progress = read.f();
    totalProgress = read.f();
    warmup = read.f();
  }
  @Override
  public void write(Writes write) {
    super.write(write);
    write.f(progress);
    write.f(totalProgress);
    write.f(warmup);
  }
}
