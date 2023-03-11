package sw.world.blocks.heat;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Building;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.Block;
import sw.util.SWMath;
import sw.world.heat.HasHeat;
import sw.world.heat.HeatBlockI;
import sw.world.heat.HeatConfig;
import sw.world.modules.HeatModule;

import static sw.util.SWDraw.getRegions;

public class HeatPipe extends Block implements HeatBlockI {
  HeatConfig heatConfig = new HeatConfig(-200f, 500f, 0.4f, 0.1f, true, true);
  public TextureRegion[] regions, heatRegions, topRegions;

  public HeatPipe(String name) {
    super(name);
    update = sync = true;
    destructible = true;
    underBullets = true;
  }

  @Override public HeatConfig heatConfig() {
    return heatConfig;
  }

  @Override
  public void setBars() {
    super.setBars();
    addBar("heat", (HeatPipeBuild entity) -> new Bar(Core.bundle.get("bar.heat"), Pal.accent, () -> SWMath.heatMap(entity.module().heat, heatConfig().minHeat, heatConfig().maxHeat)));
  }
  @Override
  public void setStats() {
    super.setStats();
    heatStats(stats);
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
      for (int i = 0; i < 4; i++) if (nearby(i) instanceof HasHeat next && next.type().heatConfig().connects()) index += 1 << i;
      return regions[index];
    }

    @Override public HeatModule module() {
      return module;
    }
    @Override public HeatBlockI type() {
      return (HeatBlockI) block;
    }

    @Override public void updateTile() {
      updateHeat(this);
    }
    @Override
    public void draw() {
      Draw.rect(getRegion(regions), x, y, 0);
      drawHeat(getRegion(heatRegions), this);
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
