package sw.world.blocks.heat;

import arc.*;
import arc.graphics.g2d.*;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

import static sw.util.SWDraw.*;

public class HeatPipe extends Block {
  public HeatConfig heatConfig = new HeatConfig();
  public TextureRegion[] regions, heatRegions;

  public HeatPipe(String name) {
    super(name);
    update = sync = true;
    destructible = true;
    underBullets = true;
  }

  @Override
  public void setBars() {
    super.setBars();
    addBar("heat", (HeatPipeBuild entity) -> new Bar(Core.bundle.get("bar.heat"), Pal.accent, entity::fraction));
  }
  @Override
  public void setStats() {
    super.setStats();
    heatConfig.heatStats(stats);
  }

  @Override
  public void load() {
    super.load();
    regions = getRegions(Core.atlas.find(name + "-tiles"), 8, 2, 32);
    heatRegions = getRegions(Core.atlas.find(name + "-heat"), 8, 2, 32);
  }

  public class HeatPipeBuild extends Building implements HasHeat {
    HeatModule module = new HeatModule();

    public TextureRegion getRegion(TextureRegion[] regions) {
      int index = 0;
      for (int i = 0; i < 4; i++) if (nearby(i) instanceof HasHeat next && next.heatC().connects()) index += 1 << i;
      return regions[index];
    }

    @Override public HeatModule heat() {
      return module;
    }
    @Override public HeatConfig heatC() {
      return heatConfig;
    }

    @Override
    public void onProximityAdded() {
      super.onProximityAdded();
      hGraph().builds.addUnique(this);
      hGraph().reloadConnections();
      hGraph().checkEntity();
    }
    @Override
    public void onProximityRemoved() {
      super.onProximityRemoved();
      hGraph().builds.remove(this);
      hGraph().links.removeAll(b -> b.has(this));
    }
    @Override
    public void onProximityUpdate() {
      super.onProximityUpdate();
      hGraph().reloadConnections();
    }

    @Override
    public void draw() {
      Draw.rect(getRegion(regions), x, y, 0);
      drawHeat(getRegion(heatRegions));
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
