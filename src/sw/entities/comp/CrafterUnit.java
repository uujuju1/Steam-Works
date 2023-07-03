package sw.entities.comp;

import arc.math.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.blocks.storage.*;
import sw.entities.*;
import sw.type.*;

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

  public boolean canCraft() {
    boolean crafting = true;
    if (!payloads.isEmpty() || !(payloads.first() instanceof BuildPayload b)) return false;
    for (ItemStack stack : type().recipe.consumeItems) {
      if (b.build.items().get(stack.item) < stack.amount) crafting = false;
    }
    for (ItemStack stack : type().recipe.outputItems) {
      if (b.build.items().get(stack.item) > b.build.block.itemCapacity - stack.amount) crafting = false;
    }
    return crafting;
  }
  public void craft() {
    if (payloads.isEmpty()) return;
    if (payloads.first() instanceof BuildPayload payload) {
      progress %= 1f;
      for (ItemStack stack : type().recipe.consumeItems) payload.build.items().remove(stack);
      for (ItemStack stack : type().recipe.outputItems) payload.build.items().add(stack.item, stack.amount);
      type().recipe.craftEffect.at(this);
    }
  }

  @Override
  public void update() {
    super.update();
    if (payloads.isEmpty()) return;
    if (payloads.first() instanceof BuildPayload payload && type().recipe != null && payload.build.items != null) {
      if (canCraft()) {
        progress += Time.delta;
        totalProgress += Time.delta;
        warmup = Mathf.approachDelta(warmup, 1, type().recipe.warmupSpeed);

        if (Mathf.chance(type().recipe.updateEffectChance)) type().recipe.updateEffect.at(this);

        if (progress > 1f) craft();
      }
    }
  }

  @Override public boolean canPickup(Building build) {
    return build.block.size == 2 && build.block instanceof StorageBlock && super.canPickup(build);
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
