package pl.world.blocks.distribution;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Building;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.Block;
import pl.world.blocks.pressure.*;

public class PressurePipe extends Block implements PressureBlock {
  public Pressure pressureC = new Pressure();
  public TextureRegion[] regions;

  public PressurePipe(String name) {
    super(name);
    solid = destructible = update = true;
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
    addBar("pressure", (PressurePipeBuild entity) -> new Bar(Core.bundle.get("bar.pressure"), Pal.accent, entity::pressureMap));
  }
  @Override
  public void setStats() {
    super.setStats();
    addPressureStats(stats);
  }

  @Override
  public void load() {
    super.load();
    regions = getRegions(Core.atlas.find(name + "-tiles"), 32, 8, 2);
  }

  public TextureRegion[] getRegions(TextureRegion base, int tilesize, int width, int height) {
    int size = width * height;
    TextureRegion[] out = new TextureRegion[size];
    for (int i = 0; i < size; i++) {
      TextureRegion res = new TextureRegion(base);
      float w = (i % width)/10f;
      float h = Mathf.floor(i/(float) width)/10f;

      res.width = res.height = tilesize;
      res.u = Mathf.map(w + 0.002f, 0, width/10f, base.u, base.u2);
      res.v = Mathf.map(h + 0.002f, 0, height/10f, base.v, base.v2);
      res.u2 = Mathf.map(w + 0.098f, 0, width/10f, base.u, base.u2);
      res.v2 = Mathf.map(h + 0.098f, 0, height/10f, base.v, base.v2);
      out[i] = res;
    }
    return out;
  }

  public class PressurePipeBuild extends Building implements PressureBuild<PressurePipe> {
    public PressureModule module = new PressureModule();

    public int getIndex() {
      int out = 0;
      for (int i = 0; i < 4; i++) {
        if (nearby(i) instanceof PressureBuild next) {
          if (getPressureBuilds(self()).contains(nearby(i)) && next.acceptsPressure(this, 0) || next.outputsPressure(this, 0))
            out += (1 << i);
        }
      }
      return out;
    }

    @Override public PressurePipe getBlock() {
      return (PressurePipe) block;
    }
    @Override public PressureModule getModule() {
      return module;
    }

    @Override public void updateTile() {
      updatePressure(self());
    }

    @Override public void draw() {
      Draw.rect(regions[getIndex()], x, y, 0);
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
