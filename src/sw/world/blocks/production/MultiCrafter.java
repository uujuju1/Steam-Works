package sw.world.blocks.production;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;
import sw.content.*;
import sw.world.blocks.production.MultiCrafterRecipe.*;
import sw.world.consumers.*;
import sw.world.meta.*;
import sw.world.recipes.*;

import java.util.concurrent.atomic.*;

import static sw.world.meta.TableSelection.*;

public class MultiCrafter extends Block {
  public Seq<GenericRecipe> recipes = new Seq<>();

  public MultiCrafter(String name) {
    super(name);
    configurable = true;
    hasItems = true;
    solid = update = sync = destructible = true;
    saveConfig = copyConfig = true;

    consume(new ConsumeItemDynamic((MultiCrafterBuild e) -> e.currentPlan != -1 ? e.getRecipe().consumeItems : ItemStack.empty));
    consume(new ConsumeLiquidDynamic<>(MultiCrafterBuild::getLiquidCons));
    consume(new ConsumePowerDynamic(e -> ((MultiCrafterBuild) e).getPowerCons()));

    config(GenericRecipe.class, MultiCrafterBuild::changeRecipe);
  }

  @Override
  public void setStats() {
    super.setStats();
    stats.add(SWStat.recipes, t -> {
      t.left();
      for (GenericRecipe recipe : recipes) {
        if (!(recipe.checkUnlocked())) {
          t.table(recipeTable -> {
            recipeTable.button(button -> {
              for (ItemStack stack : recipe.outputItems) button.image(stack.item.uiIcon).padLeft(5);
              for (LiquidStack stack : recipe.outputLiquids) button.image(stack.liquid.uiIcon).padLeft(5);
            }, () -> {}).growX().row();
            recipeTable.image(Icon.cancel).color(Color.scarlet).growX();
          }).growX().row();
          continue;
        }
        Table table = new Table(Styles.black3);
        table.add(Core.bundle.get("category.crafting")).color(Pal.accent).pad(3).left().row();
        table.table(input -> {
          input.add(Core.bundle.get("stat.input") + ":").color(Color.lightGray);

          for (ItemStack stack : recipe.consumeItems) input.add(new ItemImage(stack)).padLeft(5);
          for (LiquidStack stack : recipe.consumeLiquids) input.image(stack.liquid.uiIcon).padLeft(5);

        }).left().pad(3).padLeft(6).row();
        table.table(time -> {
          time.add(Core.bundle.get("stat.productiontime") + ":").color(Color.lightGray).padRight(5);
          StatValues.number(recipe.craftTime/60f, StatUnit.seconds).display(time);
        }).left().pad(3).padLeft(6).row();
        table.table(power -> {
          power.add(Core.bundle.get("stat.poweruse") + ":").color(Color.lightGray).padRight(5);
          StatValues.number(recipe.consumePower * 60f, StatUnit.powerSecond).display(power);
        }).left().pad(3).padLeft(6).row();
        table.table(output -> {
          output.add(Core.bundle.get("stat.output") + ":").color(Color.lightGray);

          for (ItemStack stack : recipe.outputItems) output.add(new ItemImage(stack)).padLeft(5);
          for (LiquidStack stack : recipe.outputLiquids) output.image(stack.liquid.uiIcon).padLeft(5);
        }).left().pad(3).padLeft(6).row();
        table.left().margin(5).row();
        AtomicBoolean shown = new AtomicBoolean(false);
        t.table(recipeTable -> {
          recipeTable.button(button -> {
            for (ItemStack stack : recipe.outputItems) button.image(stack.item.uiIcon).padLeft(5);
            for (LiquidStack stack : recipe.outputLiquids) button.image(stack.liquid.uiIcon).padLeft(5);
          }, () -> {
            shown.set(!shown.get());
          }).growX().row();
          recipeTable.collapser(table, false, shown::get);
        }).growX().row();
      }
    });
  }

  @Override
  public void setBars() {
    super.setBars();

    removeBar("liquid");
    recipes.each(this::addRecipeBars);
  }

  public void addRecipeBars(GenericRecipe recipe) {
    for (LiquidStack stack : recipe.consumeLiquids) if (!barMap.containsKey("liquid-" + stack.liquid.name)) addLiquidBar(stack.liquid);
    for (LiquidStack stack : recipe.outputLiquids) if (!barMap.containsKey("liquid-" + stack.liquid.name)) addLiquidBar(stack.liquid);
  }

  @Override public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {recipes.get(0).drawer.drawPlan(this, plan, list);}
  @Override public TextureRegion[] icons() {return recipes.get(0).drawer.finalIcons(this);}
  @Override public void getRegionsToOutline(Seq<TextureRegion> out) {recipes.get(0).drawer.getRegionsToOutline(this, out);}

  @Override
  public void load() {
    super.load();
    for (GenericRecipe recipe : recipes) recipe.drawer.load(this);
  }

  public class MultiCrafterBuild extends Building {
    public int currentPlan = -1;
    public float progress;
    public float totalProgress;
    public float warmup;
    public GenericRecipe currentRecipe;

    public @Nullable GenericRecipe getRecipe() {
      return currentPlan == -1 ? null : recipes.get(currentPlan);
    }
    public float getPowerCons() {
      return getRecipe() != null ? getRecipe().consumePower : 0f;
    }
    public LiquidStack[] getLiquidCons() {
      return getRecipe() != null ? getRecipe().consumeLiquids : LiquidStack.empty;
    }

    public void changeRecipe(GenericRecipe recipe) {
      currentPlan = recipes.indexOf(recipe);
      progress = totalProgress = warmup = 0f;
      SWFx.changeEffect.at(x, y, rotdeg(), block);
    }

    public void dumpOutputs() {
      if(getRecipe() != null && timer(timerDump, dumpTime / timeScale)){
        if (getRecipe().outputItems == null) return;
        for(ItemStack output : getRecipe().outputItems){
          dump(output.item);
        }
      }
    }

    @Override public float warmup() {return warmup;}
    @Override public float progress() {return progress;}
    @Override public float totalProgress() {return totalProgress;}

    @Override
    public void onProximityUpdate() {
      super.onProximityUpdate();
      for (Building build : proximity) {
        if (build instanceof MultiCrafterRecipeBuild recipe && recipe.front() == this && recipe.block().parent == block) {
          currentRecipe = recipe.recipe();
          break;
        }
      }
    }

    @Override
    public void buildConfiguration(Table table) {
      genericRecipeSelection(table, recipes, this::configure, this::getRecipe);
    }

    @Override
    public boolean acceptItem(Building source, Item item) {
      if (getRecipe() == null) return false;
      return Structs.contains(getRecipe().consumeItems, stack -> stack.item == item && items.get(item) < stack.amount * 2);
    }
    @Override
    public boolean acceptLiquid(Building source, Liquid liquid) {
      if (getRecipe() == null) return false;
      return liquids.get(liquid) < block.liquidCapacity && Structs.contains(getRecipe().consumeLiquids, stack -> stack.liquid == liquid);
    }

    @Override
    public boolean shouldConsume() {
      if (getRecipe() == null) return false;
      for (ItemStack stack : getRecipe().outputItems) if (items.get(stack.item) >= getMaximumAccepted(stack.item)) return false;
      for (LiquidStack stack : getRecipe().outputLiquids) if (liquids.get(stack.liquid) >= block.liquidCapacity) return false;
      return enabled;
    }

    @Override
    public void updateTile() {
      if (efficiency > 0 && getRecipe() != null) {
        warmup = Mathf.approachDelta(warmup, 1f, getRecipe().warmupSpeed);
        progress += getProgressIncrease(getRecipe().craftTime) * warmup;
        totalProgress += edelta() * warmup;

        if (wasVisible && Mathf.chance(getRecipe().updateEffectChance)) getRecipe().updateEffect.at(x + Mathf.range(size * 4f), y + Mathf.range(size * 4f));
        for(LiquidStack output : getRecipe().outputLiquids) handleLiquid(this, output.liquid, Math.min(output.amount * getProgressIncrease(1f), liquidCapacity - liquids.get(output.liquid)));
        if (progress >= 1f) {
          progress %= 1f;
          consume();
          if (wasVisible) getRecipe().craftEffect.at(x, y);
          if (getRecipe() != null) for (ItemStack out : getRecipe().outputItems) for (int i = 0; i < out.amount; i++) offload(out.item);
        }
      } else {
        warmup = Mathf.approachDelta(warmup, 0f, 0.019f);
      }
      dumpOutputs();
    }

    @Override
    public void draw() {
      if (getRecipe() != null) {
        getRecipe().drawer.draw(this);
      } else {
        recipes.get(0).drawer.draw(this);
      }
    }
    @Override
    public void drawLight() {
      if (getRecipe() != null) {
        getRecipe().drawer.drawLight(this);
      } else {
        recipes.get(0).drawer.drawLight(this);
      }
    }

    @Override
    public void write(Writes w) {
      super.write(w);
      w.f(warmup);
      w.f(progress);
      w.f(totalProgress);
      w.i(currentPlan);
    }
    @Override
    public void read(Reads r, byte revision) {
      super.read(r, revision);
      warmup = r.f();
      progress = r.f();
      totalProgress = r.f();
      currentPlan = r.i();
    }
  }
}