package sw.world.blocks.heat;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.gen.Building;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.Block;
import sw.world.heat.HasHeat;
import sw.world.meta.SWStat;
import sw.world.modules.HeatModule;

import static sw.util.SWDraw.getRegions;
import static sw.util.SWMath.*;

public class HeatRadiator extends Block {
  public TextureRegion[] regions, heatRegions, topRegions;
  public float maxHeat = 250;
  public float minHeatLoss = 100;
  public HeatRadiator(String name) {
    super(name);
    solid = destructible = true;
    update = sync = true;
    rotate = true;
  }

  @Override
  public void setBars() {
    super.setBars();
    addBar("heat", (HeatRadiatorBuild entity) -> new Bar(Core.bundle.get("bar.heat"), Color.scarlet, () -> heatMap(entity.module().heat, 0f, maxHeat)));
  }

  @Override
  public void setStats() {
    super.setStats();
    stats.add(SWStat.maxHeat, maxHeat);
    stats.add(SWStat.heatTresh, minHeatLoss);
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

    public TextureRegion getRegion(TextureRegion[] regions) {
      int index = 0;
      if (front() instanceof HasHeat next && next.connects(this) && connects(next)) index++;
      if (back() instanceof HasHeat next && next.connects(this) && connects(next)) index+=2;
      index += rotation * 4;
      return regions[index];
    }

    @Override public boolean acceptsHeat(HasHeat from, float amount) {
      return connects(from) && HasHeat.super.acceptsHeat(from, amount);
    }
    @Override public boolean outputsHeat(HasHeat to, float amount) {
      return !to.overflows(amount) && HasHeat.super.outputsHeat(to, amount) && connects(to);
    }

    @Override public boolean connects(HasHeat to) {
      return front() == to || back() == to;
    }

    @Override public HeatModule module() {
      return module;
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
      if (module().heat < 0) module().heat = 0;
      if (module().heat > maxHeat) kill();
      for (HasHeat build : nextBuilds(self())) transferHeat(build.module());
      module().subHeat(0.001f * Math.max(0, module().heat - minHeatLoss) * Time.delta);
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
  }
}
