package sw.world.blocks.units;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
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
    stats.add(Stat.input, t -> {for (ItemStack stack : unitPlan.requirements) t.add(new ItemImage(stack)).pad(3f);});
    stats.add(Stat.output, t -> t.image(unitPlan.unit.fullIcon).size(32f).pad(5));
  }

  @Override
  public void setBars() {
    super.setBars();
    addBar("progress", (SingleUnitFactoryBuild e) -> new Bar("bar.progress", Pal.ammo, e::fraction));
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

    public float fraction() {
      return progress;
    }

    @Override
    public void updateTile() {
	    if(unit != null && (unit.dead || !unit.isAdded())) unit = null;
	    if(unitId != -1) {
		    unit = Groups.unit.getByID(unitId);
		    if(unit == null || !net.client()) unitId = -1;
	    }

      if (efficiency > 0 && unit == null) {
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
