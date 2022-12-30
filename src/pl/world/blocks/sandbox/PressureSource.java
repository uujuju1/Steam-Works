package pl.world.blocks.sandbox;

import arc.Core;
import arc.scene.ui.layout.Table;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Building;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.meta.BuildVisibility;
import pl.world.blocks.pressure.*;

public class PressureSource extends Block implements PressureBlock {
  public Pressure pressureC = new Pressure();

  public PressureSource(String name) {
    super(name);
    buildVisibility = BuildVisibility.sandboxOnly;
    solid = destructible = update = configurable = sync = true;
  }

  @Override public Pressure getPressure() {
    return pressureC;
  }

  @Override
  public void setBars() {
    super.setBars();
    addBar("pressure", (PressureSourceBuild entity) -> new Bar(Core.bundle.get("bar.pressure"), Pal.accent, entity::pressureMap));
  }
  @Override
  public void setStats() {
    super.setStats();
    addPressureStats(stats);
  }

  public class PressureSourceBuild extends Building implements PressureBuild<PressureSource> {
    public PressureModule module = new PressureModule();
    public float current;

    @Override public PressureSource getBlock() {
      return (PressureSource) block;
    }
    @Override public PressureModule getModule() {
      return module;
    }

    @Override public void buildConfiguration(Table table) {
      table.slider(getPressureC().minPressure, getPressureC().maxPressure, 1, current, f -> current = f);
    }

    @Override
    public void updatePressure(Building to) {
      getModule().setPressure(current);
      PressureBuild.super.updatePressure(to);
    }

    @Override public void updateTile() {
      updatePressure(self());
    }

    @Override
    public void write(Writes write) {
      super.write(write);
      write.f(current);
    }
    @Override
    public void read(Reads read, byte revision) {
      super.read(read, revision);
      current = read.f();
    }
  }
}
