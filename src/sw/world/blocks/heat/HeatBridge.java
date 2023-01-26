package sw.world.blocks.heat;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.math.geom.Point2;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.Tmp;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.meta.StatUnit;
import sw.util.SWMath;
import sw.world.meta.SWStat;
import sw.world.modules.HeatModule;
import sw.world.heat.HasHeat;

import static mindustry.Vars.tilesize;

public class HeatBridge extends Block {
  public TextureRegion bridgeRegion, endRegion;

  public int range = 5;
  public float maxHeat = 100f;
  public float heatLoss = 0.1f;

  public HeatBridge(String name) {
    super(name);
    update = sync = true;
    destructible = true;
    underBullets = true;
    solid = true;
//    saveConfig = copyConfig = true;
    configurable = true;

    config(Integer.class, (HeatBridgeBuild tile, Integer val) -> tile.link = val);
  }

  @Override
  public void setBars() {
    super.setBars();
    addBar("heat", (HeatBridgeBuild entity) -> new Bar(Core.bundle.get("bar.heat"), Pal.accent, () -> SWMath.heatMap(entity.module().heat, 0f, maxHeat)));
  }

  @Override
  public void setStats() {
    super.setStats();
    stats.add(SWStat.maxHeat, maxHeat, StatUnit.degrees);
  }

  @Override
  public void load() {
    super.load();
    bridgeRegion = Core.atlas.find(name + "-bridge");
    endRegion = Core.atlas.find(name + "-end");
  }

  @Override
  public void drawPlace(int x, int y, int rotation, boolean valid) {
    super.drawPlace(x, y, rotation, valid);
    for (Point2 next: Geometry.d4) {
      Drawf.dashLine(Pal.placing,
              x * tilesize + next.x * (tilesize/2f),
              y * tilesize + next.y * (tilesize/2f),
              (x + next.x * range) * tilesize,
              (y + next.y * range) * tilesize
      );
    }
  }

  public class HeatBridgeBuild extends Building implements HasHeat {
    HeatModule module = new HeatModule();
    public int link = -1;

    public void drawBridge() {
      Draw.reset();
      Draw.z(Layer.power);
      if (Vars.world.tile(link).build == null) return;
      Building next = Vars.world.tile(link).build;

      float angle = Math.round(Angles.angle(x, y, next.x, next.y));
      float rot = (angle + 90) % 180 - 90;
      float dst = Math.round(Mathf.dst(x/8f, y/8f, next.x/8f, next.y/8f));

      if (angle == 180 || angle == 90) Draw.xscl = -1;
      if (angle == 180) Draw.yscl = 1;

      Draw.rect(endRegion, x, y, rot);
      Draw.xscl *= -1;
      Draw.rect(endRegion, next.x, next.y, rot);

      Draw.scl();

      for (int i = 0; i < dst - 1; i++) {
        Tmp.v1.trns(angle, (i + 1) * 8);

        Draw.rect(bridgeRegion, x + Tmp.v1.x, y + Tmp.v1.y, rot);
      }
      Draw.reset();
    }

    public boolean linkValid(Tile from, Tile to) {
      if (from.x == to.x) return Math.abs(from.y - to.y) <= range;
      if (from.y == to.y) return Math.abs(from.x - to.x) <= range;
      return false;
    }

    @Override public HeatModule module() {
      return module;
    }

    @Override
    public Seq<HasHeat> nextBuilds(Building from) {
      Seq<HasHeat> out = HasHeat.super.nextBuilds(from);
      if (link != -1 && Vars.world.tile(link).build instanceof HasHeat next) out.add(next);
      return out;
    }

    @Override
    public boolean onConfigureBuildTapped(Building other) {
      if (other.pos() == pos() || other.pos() == link) {
        configure(-1);
        return false;
      }
      if (linkValid(tile(), other.tile()) && other instanceof HeatBridgeBuild next) {
        if (next.link == pos()) {
          next.configure(-1);
        }
        configure(next.pos());
        return true;
      }
      return true;
    }

    @Override
    public void updateTile() {
      if (module().heat < 0) module().setHeat(0f);
      if (module().heat > maxHeat) kill();
      for (HasHeat build : nextBuilds(self())) transferHeat(build.module());
      module().subHeat(heatLoss * Time.delta);
    }

    @Override
    public void draw() {
      super.draw();
      if (link != -1) drawBridge();
    }
    @Override
    public void drawSelect() {
      super.drawSelect();
      for (Point2 next: Geometry.d4) {
        Drawf.dashLine(Pal.accent,
                x + next.x * (tilesize/2f),
                y + next.y * (tilesize/2f),
                x + next.x * range * tilesize,
                y + next.y * range * tilesize
        );
      }
    }

    @Override
    public void read(Reads read, byte revision) {
      super.read(read, revision);
      module.read(read);
      link = read.i();
    }
    @Override
    public void write(Writes write) {
      super.write(write);
      module.write(write);
      write.i(link);
    }
  }
}
