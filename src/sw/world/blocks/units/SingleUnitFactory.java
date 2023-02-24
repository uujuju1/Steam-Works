package sw.world.blocks.units;

import arc.Core;
import arc.Events;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.content.UnitTypes;
import mindustry.game.EventType.UnitCreateEvent;
import mindustry.gen.Building;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.type.ItemStack;
import mindustry.ui.ItemImage;
import mindustry.world.Block;
import mindustry.world.blocks.units.UnitFactory.UnitPlan;
import mindustry.world.meta.Stat;

public class SingleUnitFactory extends Block {
  public TextureRegion topRegion;
  public UnitPlan unitPlan = new UnitPlan(UnitTypes.flare, 60f, ItemStack.empty);
  public int unitCapacity = 1;

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
    public boolean canCreate() {
      Seq<Building> otherFactories = team.data().getBuildings(block);
      return unitPlan.unit.useUnitCap ? team.data().countType(unitPlan.unit) < team.data().unitCap : team.data().countType(unitPlan.unit) < otherFactories.size * unitCapacity;
    }

    @Override
    public void updateTile() {
      if (efficiency > 0 && canCreate()) {
        progress += getProgressIncrease(unitPlan.time) * warmup * Vars.state.rules.unitBuildSpeedMultiplier;
        totalProgress += edelta() * Vars.state.rules.unitBuildSpeedMultiplier;
        warmup = Mathf.approachDelta(warmup, 1, 0.014f * Vars.state.rules.unitBuildSpeedMultiplier);

        if (progress >= 1f) {
          progress %= 1f;

          Unit unit = unitPlan.unit.spawn(team, this);
          Events.fire(new UnitCreateEvent(unit, this));
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
