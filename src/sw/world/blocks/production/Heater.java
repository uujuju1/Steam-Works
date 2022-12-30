package sw.world.blocks.production;

import arc.Core;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.graphics.Pal;
import mindustry.gen.Building;
import pl.world.blocks.pressure.*;

public class Heater extends Block implements PressureBlock {
  public Pressure pressureC = new Pressure();
  public float heatSpeed = 0.014f;
  public float consumeTime = 60f;
  public float pressureSpeed = 1f;

  public Heater(String name) {
    super(name);
    solid = destructible = update = true;
  }

  @Override public Pressure getPressure() {
    return pressureC;
  }

  @Override
  public void setBars() {
    super.setBars();
    addBar("pressure", (HeaterBuild entity) -> new Bar(Core.bundle.get("bar.pressure"), Pal.accent, entity::pressureMap));
    addBar("heat", (HeaterBuild entity) -> new Bar(Core.bundle.get("bar.heat"), Color.scarlet, () -> entity.heat));
  }
  @Override
  public void setStats() {
    super.setStats();
    addPressureStats(stats);
  }

  public class HeaterBuild extends Building implements PressureBuild<Heater> {
    public PressureModule module = new PressureModule();
    public float heat;
    public float progress;

    @Override public Heater getBlock() {
      return (Heater) block;
    }
    @Override public PressureModule getModule() {
      return module;
    }

    @Override public boolean acceptsPressure(Building build, float amount) {
      return amount + getPressure() < getPressureC().maxPressure;
    }

    @Override
    public boolean shouldConsume() {
      if (getPressure() > getPressureC().maxPressure - 20f) return false;
      return enabled;
    }

    @Override
    public void updateTile() {
      updatePressure(self());
      if (efficiency > 0) {
        heat = Mathf.approachDelta(heat, 1f, heatSpeed);
        progress += getProgressIncrease(consumeTime);
      } else heat = Mathf.approachDelta(heat, 0, heatSpeed);
      if (heat > 0.75f && acceptsPressure(this, Time.delta * pressureSpeed)) getModule().addPressure(Time.delta * pressureSpeed);
      if (progress >= 1) consume();
    }

    @Override
    public void write(Writes write) {
      super.write(write);
      module.write(write);
      write.f(heat);
    }
    @Override
    public void read(Reads read, byte revision) {
      super.read(read, revision);
      module.read(read);
      heat = read.f();
    }
  }
}