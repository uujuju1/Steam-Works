package sw.world.blocks.heat;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Building;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.meta.StatUnit;
import sw.util.*;
import sw.world.meta.SWStat;
import sw.world.modules.HeatModule;
import sw.world.heat.HasHeat;

import static sw.util.SWDraw.*;

public class HeatPipe extends Block {
  public TextureRegion[] regions, heatRegions, topRegions;

  public float maxHeat = 100f;
  public float heatLoss = 0.1f;

  public HeatPipe(String name) {
    super(name);
    update = sync = true;
    destructible = true;
    underBullets = true;
  }

  @Override
  public void setBars() {
    super.setBars();
    addBar("heat", (HeatPipeBuild entity) -> new Bar(Core.bundle.get("bar.heat"), Pal.accent, () -> SWMath.heatMap(entity.module().heat, 0f, maxHeat)));
  }

  @Override
  public void setStats() {
    super.setStats();
    stats.add(SWStat.maxHeat, maxHeat, StatUnit.degrees);
  }

  @Override
  public void load() {
    super.load();
    regions = getRegions(Core.atlas.find(name + "-tiles"), 8, 2, 32);
    heatRegions = getRegions(Core.atlas.find(name + "-heat"), 8, 2, 32);
    topRegions = getRegions(Core.atlas.find(name + "-top"), 8, 2, 32);
  }

  public class HeatPipeBuild extends Building implements HasHeat {
    HeatModule module = new HeatModule();

    public TextureRegion getRegion(TextureRegion[] regions) {
      int index = 0;
      for (int i = 0; i < 4; i++) {
        if (nearby(i) instanceof HasHeat next && connects(next) && next.connects(this)) index += 1<<i;
      }
      return regions[index];
    }

    @Override public HeatModule module() {
      return module;
    }

    @Override public boolean overflows(float amount) {
      return module().heat + amount >= maxHeat;
    }

    @Override
    public void updateTile() {
      if (module().heat < 0) module().setHeat(0f);
      if (module().heat > maxHeat) kill();
      for (HasHeat build : nextBuilds(self())) transferHeat(build.module());
      module().subHeat(heatLoss * Time.delta);
    }
    @Override
    public void draw() {
      Draw.rect(getRegion(regions), x, y, 0);
      Draw.color(Pal.accent);
      Draw.alpha(SWMath.heatGlow(module().heat));
      Draw.rect(getRegion(heatRegions), x, y, 0);
      Draw.reset();
      Draw.rect(getRegion(topRegions), x, y, 0);
    }

    @Override
    public void read(Reads read, byte revision) {
      super.read(read, revision);
      module.read(read);
    }
    @Override
    public void write(Writes write) {
      super.write(write);
      module.write(write);
    }
  }
}
