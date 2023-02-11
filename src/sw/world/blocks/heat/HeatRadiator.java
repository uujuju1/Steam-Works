package sw.world.blocks.heat;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Building;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.meta.StatUnit;
import sw.world.heat.HasHeat;
import sw.world.heat.HeatBlockI;
import sw.world.heat.HeatConfig;
import sw.world.meta.SWStat;
import sw.world.modules.HeatModule;

import static sw.util.SWDraw.getRegions;
import static sw.util.SWMath.*;

public class HeatRadiator extends Block implements HeatBlockI {
  HeatConfig heatConfig = new HeatConfig(-200f, 500f, 0.4f, 0.1f, true, true);
  public TextureRegion[] regions, heatRegions, topRegions;
  public float minHeatLoss = 100f;

  public HeatRadiator(String name) {
    super(name);
    solid = destructible = true;
    update = sync = true;
    rotate = true;
  }

  @Override public HeatConfig heatConfig() {return heatConfig;}

  @Override
  public void setBars() {
    super.setBars();
    addBar("heat", (HeatRadiatorBuild entity) -> new Bar(Core.bundle.get("bar.heat"), Pal.accent, () -> heatMap(entity.module().heat, 0f, heatConfig().maxHeat)));
  }
  @Override
  public void setStats() {
    super.setStats();
    stats.add(SWStat.maxHeat, heatConfig().maxHeat, StatUnit.degrees);
    stats.add(SWStat.heatTresh, minHeatLoss, StatUnit.degrees);
  }

  @Override
  public void load() {
    super.load();
    regions = getRegions(Core.atlas.find(name + "-tiles"), 4, 4, 32);
    heatRegions = getRegions(Core.atlas.find(name + "-heat"), 4, 4, 32);
    topRegions = getRegions(Core.atlas.find(name + "-top"), 4, 4, 32);
  }

  public class HeatRadiatorBuild extends Building implements HasHeat {
    HeatModule module = new HeatModule();

    @Override public HeatModule module() {
      return module;
    }
    @Override public HeatBlockI type() {return (HeatBlockI) block;}

    public TextureRegion getRegion(TextureRegion[] regions) {
      int index = 0;
      if (front() instanceof HasHeat next && next.type().heatConfig().connects()) index++;
      if (back() instanceof HasHeat next && next.type().heatConfig().connects()) index+=2;
      index += rotation * 4;
      return regions[index];
    }

    @Override public boolean outputsHeat(HasHeat to, float amount) {
      return !to.overflows(amount) && HasHeat.super.outputsHeat(to, amount) && connects(to);
    }

    @Override
    public Seq<HasHeat> nextBuilds(Building from) {
      Seq<HasHeat> out = new Seq<>();
      if (front() instanceof HasHeat next) {
        float amount = heatTransferDelta(0.2f, module().heat, next.module().heat, true);
        if (next.acceptsHeat(this, amount) && outputsHeat(next, amount)) out.add(next);
      }
      if (back() instanceof HasHeat next) {
        float amount = heatTransferDelta(0.2f, module().heat, next.module().heat, true);
        if (next.acceptsHeat(this, amount) && outputsHeat(next, amount)) out.add(next);
      }
      return out;
    }

    @Override
    public void updateTile() {
      if (module().heat < heatConfig().minHeat) module().setHeat(heatConfig().minHeat);
      if (module().heat > heatConfig().maxHeat) kill();
      for (HasHeat build : nextBuilds(self())) transferHeat(build.module());
      module().subHeat(heatConfig().heatLoss * Math.max(1, module().heat - minHeatLoss) * Time.delta);
    }
    @Override
    public void draw() {
      Draw.rect(getRegion(regions), x, y, 0);
      Draw.color(Pal.accent);
      Draw.alpha(heatGlow(module().heat));
      Draw.rect(getRegion(heatRegions), x, y, 0);
      Draw.reset();
      Draw.rect(getRegion(topRegions), x, y, 0);
    }

    @Override
    public void read(Reads read, byte revision) {
      super.read(read, revision);
      module().read(read);
    }
    @Override
    public void write(Writes write) {
      super.write(write);
      module().write(write);
    }
  }
}
