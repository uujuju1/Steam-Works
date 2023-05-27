package sw.world.blocks.heat;

import arc.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.blocks.production.*;
import mindustry.world.meta.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class HeatGenericCrafter extends GenericCrafter {
  public HeatConfig heatConfig = new HeatConfig();
  public float outputHeat = -1f;

  public HeatGenericCrafter(String name) {
    super(name);
  }

  @Override
  public void setStats() {
    super.setStats();
    heatConfig.heatStats(stats);
    if (outputHeat >= 0) stats.add(SWStat.outputHeat, outputHeat, StatUnit.degrees);
  }
  @Override
  public void setBars() {
    super.setBars();
    addBar("heat", (HeatGenericCrafterBuild entity) -> new Bar(Core.bundle.get("bar.heat"), Pal.accent, entity::fraction));
  }

  public class HeatGenericCrafterBuild extends GenericCrafterBuild implements HasHeat {
    HeatModule module = new HeatModule();

    @Override public HeatModule heat() {
      return module;
    }
    @Override public HeatConfig heatC() {
      return heatConfig;
    }

    @Override
    public void updateTile() {
      super.updateTile();
      if (efficiency > 0 && outputHeat >= 0) heat().addHeat(outputHeat * efficiencyScale() * Time.delta);
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
    public void write(Writes write) {
      super.write(write);
      heat().write(write);
    }
    @Override
    public void read(Reads read, byte revision) {
      super.read(read, revision);
      heat().read(read);
    }
  }
}
