package sw.world.blocks.heat;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Building;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.Block;
import sw.util.SWMath;
import sw.world.meta.SWStat;
import sw.world.modules.HeatModule;
import sw.world.heat.HasHeat;

public class HeatPipe extends Block {
  public TextureRegion[] regions;
  public TextureRegion[] heatRegions;
  public TextureRegion[] topRegions;

  public float maxHeat = 100f;
  public HeatPipe(String name) {
    super(name);
    update = sync = true;
    destructible = true;
    underBullets = true;
  }

  @Override
  public void setBars() {
    super.setBars();
    addBar("heat", (HeatPipeBuild entity) -> new Bar(Core.bundle.get("bar.heat"), Color.scarlet, () -> SWMath.heatMap(entity.module().heat, 0f, maxHeat)));
  }

  @Override
  public void setStats() {
    super.setStats();
    stats.add(SWStat.maxHeat, maxHeat);
  }

  @Override
  public void load() {
    super.load();
    regions = getRegions(Core.atlas.find(name + "-tiles"), 8, 2, 32);
    heatRegions = getRegions(Core.atlas.find(name + "-heat"), 8, 2, 32);
    topRegions = getRegions(Core.atlas.find(name + "-top"), 8, 2, 32);
  }

  public TextureRegion[] getRegions(TextureRegion base, int width, int height, int size) {
    int arraySize = width * height;
    TextureRegion[] out = new TextureRegion[arraySize];
    for (int i = 0; i < arraySize; i++) {
      TextureRegion n = new TextureRegion(base);
      float ix = i % width;
      float iy = Mathf.floor((float) i/width);

      n.width = n.height = size;
      n.u = Mathf.map(ix + 0.01f, 0, width, base.u, base.u2);
      n.u2 = Mathf.map(ix + 0.99f, 0, width, base.u, base.u2);
      n.v = Mathf.map(iy + 0.01f, 0, height, base.v, base.v2);
      n.v2 = Mathf.map(iy + 0.99f, 0, height, base.v, base.v2);

      out[i] = n;
    }
    return out;
  }

  public class HeatPipeBuild extends Building implements HasHeat {
    HeatModule module = new HeatModule();

    public TextureRegion getRegion(TextureRegion[] regions) {
      int index = 0;
      for (int i = 0; i < 4; i++) {
        if (nearby(i) instanceof HasHeat) index += 1<<i;
      }
      return regions[index];
    }

    @Override public HeatModule module() {
      return module;
    }

    @Override
    public void updateTile() {
      if (module().heat < 0) module().heat = 0;
      if (module().heat > maxHeat) kill();
      for (HasHeat build : nextBuilds(self())) transferHeat(build.module());
      module().subHeat(0.001f * Time.delta);
    }
    @Override
    public void draw() {
      Draw.rect(getRegion(regions), x, y, 0);
      Draw.color(Pal.accent);
      Draw.alpha(SWMath.heatMap(module().heat, 0, maxHeat));
      Draw.rect(getRegion(heatRegions), x, y, 0);
      Draw.reset();
      Draw.alpha(1);
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
