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

public class HeatRadiator extends Block {
  public HeatConfig heatConfig = new HeatConfig();
  public TextureRegion heatRegion, topRegion;

  public HeatRadiator(String name) {
    super(name);
    solid = destructible = true;
    update = sync = true;
  }

  @Override
  public void setBars() {
    super.setBars();
    addBar("heat", (HeatRadiatorBuild entity) -> new Bar(Core.bundle.get("bar.heat"), Pal.accent, entity::fraction));
  }
  @Override
  public void setStats() {
    super.setStats();
    heatConfig.heatStats(stats);
  }

  @Override
  public void load() {
    super.load();
    heatRegion = Core.atlas.find(name + "-heat");
    topRegion = Core.atlas.find(name + "-top");
  }

  public class HeatRadiatorBuild extends Building implements HasHeat {
    HeatModule module = new HeatModule();

    @Override public HeatModule heat() {
      return module;
    }
    @Override public HeatConfig heatC() {
      return heatConfig;
    }

    @Override public void updateTile() {
      updateHeat(this);
    }

    @Override
    public void onProximityAdded() {
      super.onProximityAdded();
      hGraph().builds.addUnique(this);
      hGraph().reloadConnections();
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
      super.draw();
      drawHeat(heatRegion, this);
      Draw.rect(topRegion, x, y, 0);
    }

    @Override
    public void read(Reads read, byte revision) {
      super.read(read, revision);
      heat().read(read);
    }
    @Override
    public void write(Writes write) {
      super.write(write);
      heat().write(write);
    }
  }
}
