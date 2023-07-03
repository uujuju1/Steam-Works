package sw.world.blocks.units;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.units.UnitFactory.*;
import mindustry.world.meta.*;
import sw.entities.comp.*;

import static mindustry.Vars.*;

public class SingleUnitFactory extends Block {
  public TextureRegion topRegion;
  public UnitPlan unitPlan = new UnitPlan(UnitTypes.flare, 60f, ItemStack.empty);

  public SingleUnitFactory(String name) {
    super(name);
    destructible = update = sync = true;
  }

  @Override
  public void setStats() {
    super.setStats();
    stats.add(Stat.output, table -> {
      table.row();
      table.table(Styles.grayPanel, t -> {
        if(unitPlan.unit.isBanned()){
          t.image(Icon.cancel).color(Pal.remove).size(40);
          return;
        }

        if(unitPlan.unit.unlockedNow()){
          t.image(unitPlan.unit.uiIcon).size(40).pad(10f).left().scaling(Scaling.fit);
          t.table(info -> {
            info.add(unitPlan.unit.localizedName).left();
            info.row();
            info.add(Strings.autoFixed(unitPlan.time / 60f, 1) + " " + Core.bundle.get("unit.seconds")).color(Color.lightGray);
          }).left();

          t.table(req -> {
            req.right();
            for(int i = 0; i < unitPlan.requirements.length; i++){
              if(i % 6 == 0){
                req.row();
              }

              ItemStack stack = unitPlan.requirements[i];
              req.add(new ItemDisplay(stack.item, stack.amount, false)).pad(5);
            }
          }).right().grow().pad(10f);
        }else{
          t.image(Icon.lock).color(Pal.darkerGray).size(40);
        }
      }).growX().pad(5);
      table.row();
    });
  }

  @Override
  public void setBars() {
    super.setBars();
    addBar("progress", (SingleUnitFactoryBuild e) -> new Bar("bar.progress", Pal.ammo, e::progress));
  }

  @Override
  public void load() {
    super.load();
    topRegion = Core.atlas.find(name + "-top");
  }

  @Override
  public TextureRegion[] icons() {
    return new TextureRegion[]{region, topRegion};
  }

  public class SingleUnitFactoryBuild extends Building {
    public float progress, totalProgress, warmup;
    public Unit unit;
		public int unitId = -1;

    public boolean canBuild() {
      return efficiency > 0 && !(unit instanceof BuildingTetherc);
    }

    @Override public float progress() {
      return progress;
    }
    @Override public float totalProgress() {
      return totalProgress;
    }
    @Override public float warmup() {
      return warmup;
    }

    @Override
    public int getMaximumAccepted(Item item) {
      for (ItemStack stack : unitPlan.requirements) {
        if (stack.item == item) return stack.amount * 2;
      }
      return 0;
    }

    @Override
    public void updateTile() {
	    if(unit != null && (unit.dead || !unit.isAdded())) unit = null;
	    if(unitId != -1) {
		    unit = Groups.unit.getByID(unitId);
		    if(unit == null || !net.client()) unitId = -1;
	    }

      if (canBuild()) {
        progress += getProgressIncrease(unitPlan.time) * warmup * Vars.state.rules.unitBuildSpeedMultiplier;
        totalProgress += edelta() * Vars.state.rules.unitBuildSpeedMultiplier;
        warmup = Mathf.approachDelta(warmup, 1, 0.014f * Vars.state.rules.unitBuildSpeedMultiplier);

        if (progress >= 1f) {
          progress %= 1f;

          unit = unitPlan.unit.create(team);
          if(unit instanceof CrafterUnit u) u.building(this);
          unit.set(x, y);
          unit.rotation = 90f;
          unit.add();
          Call.unitTetherBlockSpawned(tile, unit.id);
        }
      } else {
        warmup = Mathf.approachDelta(warmup, 0, 0.014f);
        progress = Mathf.approachDelta(warmup, 0, 0.014f);
      }
    }

    @Override
    public void draw() {
      super.draw();
      Draw.draw(Layer.blockOver, () -> Drawf.construct(this, unitPlan.unit, 0, progress, warmup * Vars.state.rules.unitBuildSpeedMultiplier, totalProgress));
      Draw.z(Layer.blockOver + 0.01f);
      Draw.rect(topRegion, x, y, 0);
    }

    @Override
    public void write(Writes write) {
      super.write(write);
      write.f(progress);
      write.f(totalProgress);
      write.f(warmup);
    }
    @Override
    public void read(Reads read, byte revision) {
      super.read(read, revision);
      progress = read.f();
      totalProgress = read.f();
      warmup = read.f();
    }
  }
}
