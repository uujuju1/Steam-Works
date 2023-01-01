package pl.world.blocks.production;

import arc.Core;
import arc.math.Mathf;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.blocks.production.GenericCrafter;
import pl.world.blocks.pressure.*;

public class PressureGenericCrafter extends GenericCrafter implements PressureBlock {
  public Pressure pressureC = new Pressure();
  public float pressureSpeed = 1;

  public PressureGenericCrafter(String name) {
    super(name);
  }

  @Override public Pressure getPressure() {
    return pressureC;
  }

  @Override
  public void setBars() {
    super.setBars();
    addBar("pressure", (PressureGenericCrafterBuild entity) -> new Bar(Core.bundle.get("bar.pressure"), Pal.accent, entity::pressureMap));
  }
  @Override
  public void setStats() {
    super.setStats();
    addPressureStats(stats);
  }

  public class PressureGenericCrafterBuild extends GenericCrafterBuild implements PressureBuild<PressureGenericCrafter> {
    public PressureModule module = new PressureModule();

    @Override public PressureGenericCrafter getBlock() {
      return (PressureGenericCrafter) block;
    }
    @Override public PressureModule getModule() {
      return module;
    }

    @Override
    public void updateTile() {
      super.updateTile();
      updatePressure(self());
      if (efficiency > 0) module.setPressure(Mathf.approachDelta(getPressure(), getPressureC().maxPressure, pressureSpeed));
    }

    @Override
    public void write(Writes write) {
      super.write(write);
      module.write(write);
    }
    @Override
    public void read(Reads read, byte revision) {
      super.read(read, revision);
      module.read(read);
    }
  }
}
