package pl.world.blocks.distribution;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Building;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.Block;
import pl.world.blocks.pressure.*;

public class PressureValve extends Block implements PressureBlock {
  public Pressure pressureC = new Pressure();

  public PressureValve(String name) {
    super(name);
    rotate = true;
    solid = update = sync = destructible = configurable = true;
  }

  @Override public Pressure getPressure() {
    return pressureC;
  }

  @Override
  public boolean canReplace(Block other) {
    if ((other instanceof PressurePipe) || (other instanceof PressureValve)) return true;
    return super.canReplace(other);
  }

  @Override
  public void setBars() {
    super.setBars();
    addBar("pressure", (PressureValveBuild entity) -> new Bar(Core.bundle.get("bar.pressure"), Pal.accent, entity::pressureMap));
  }

  @Override
  public void setStats() {
    super.setStats();
    addPressureStats(stats);
  }

  public class PressureValveBuild extends Building implements PressureBuild<PressureValve> {
    public PressureModule module = new PressureModule();
    public float open;

    @Override public PressureValve getBlock() {
      return (PressureValve) block;
    }
    @Override public PressureModule getModule() {
      return module;
    }
    @Override
    public Seq<Building> getPressureBuilds(Building with) {
      Seq<Building> builds = new Seq<>();
      if (front() instanceof PressureBuild) builds.add(front());
      if (back() instanceof PressureBuild) builds.add(back());
      return builds;
    }

    @Override public boolean acceptsPressure(Building build, float amount) {
      return build == back();
    }
    @Override public boolean outputsPressure(Building build, float amount) {
      return build == front();
    }

    @Override
    public void updatePressure(Building to) {
      if (getPressure() < getPressureC().minPressure || getPressure() > getPressureC().maxPressure) to.kill();
      for (Building build : getPressureBuilds(to)) {
        float value = (getPressure() - ((PressureBuild) build).getPressure()) * getPressureC().transferRate * open;
        if (outputsPressure(build, value) && ((PressureBuild) build).acceptsPressure(to, value)) {
          getModule().transfer(((PressureBuild) build).getModule(), value, getPressureC().internalSize / ((PressureBuild) build).getPressureC().internalSize);
        }
      }
      getModule().setPressure(getPressure() * getPressureC().lossRate);
    }
    @Override public void updateTile() {
      updatePressure(self());
    }

    @Override public void buildConfiguration(Table table) {
      table.slider(0, 1, 0.01f, open, f -> open = f);
    }

    @Override
    public void draw() {
      Draw.rect(region, x, y, (rotdeg() + 90f) % 180f - 90f);
      Lines.stroke(1f);
      Lines.lineAngle(x, y, rotdeg() + (90f * (1f - open)), 4f);
    }

    @Override
    public void write(Writes write) {
      super.write(write);
      module.write(write);
      write.f(open);
    }
    @Override
    public void read(Reads read, byte revision) {
      super.read(read, revision);
      module.read(read);
      open = read.f();
    }
  }
}
