package sw.world.blocks.heat;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Point2;
import arc.struct.Seq;
import arc.util.Nullable;
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
import sw.util.SWDraw;
import sw.util.SWMath;
import sw.world.heat.HasHeat;
import sw.world.heat.HeatBlockI;
import sw.world.heat.HeatConfig;
import sw.world.modules.HeatModule;

import java.util.Objects;


public class HeatBridge extends Block implements HeatBlockI {
  HeatConfig heatConfig = new HeatConfig(-200f, 500f, 0.4f, 0.1f, true, true);
  public TextureRegion bridgeRegion, endRegion;

  public int range = 5;

  public HeatBridge(String name) {
    super(name);
    update = sync = true;
    destructible = true;
    underBullets = true;
    solid = true;
    saveConfig = copyConfig = true;
    configurable = true;

    config(Point2.class, (HeatBridgeBuild tile, Point2 next) -> tile.link = new Point2(next).add(Point2.unpack(tile.pos())).pack());
    config(Integer.class, (HeatBridgeBuild tile, Integer i) -> tile.link = i);
  }

  public boolean linkValid(Point2 p1, Point2 p2) {
    if (p1.x == p2.x) return Math.abs(p1.y - p2.y) <= range + 1;
    if (p1.y == p2.y) return Math.abs(p1.x - p2.x) <= range + 1;
    return false;
  }

  @Override public HeatConfig heatConfig() {return heatConfig;}

  @Override
  public void setBars() {
    super.setBars();
    addBar("heat", (HeatBridgeBuild entity) -> new Bar(Core.bundle.get("bar.heat"), Pal.accent, () -> SWMath.heatMap(entity.module().heat, heatConfig().minHeat, heatConfig().maxHeat)));
  }
  @Override
  public void setStats() {
    super.setStats();
    heatStats(stats);
  }

  @Override
  public void drawPlace(int x, int y, int rotation, boolean valid) {
    super.drawPlace(x, y, rotation, valid);
    for (int i = 0; i < 4; i++) {
      Tmp.v1.trns(i * 90, 6f);
      Tmp.v2.trns(i * 90, 8 * (range + 1));
      Drawf.dashLine(Pal.accent, x * 8 + Tmp.v1.x, y * 8 + Tmp.v1.y, x * 8 + Tmp.v2.x, y * 8 + Tmp.v2.y);
    }
  }

  @Override
  public void load() {
    super.load();
    bridgeRegion = Core.atlas.find(name + "-bridge");
    endRegion = Core.atlas.find(name + "-end");
  }

  public class HeatBridgeBuild extends Building implements HasHeat {
    HeatModule module = new HeatModule();
    public int link = -1;

    public @Nullable Building getLink() {
      return Vars.world.build(link);
    }

    public void drawBridge() {
      Draw.reset();
      Draw.z(Layer.power);
      if (getLink() == null) return;
      Building next = getLink();

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

    @Override public HeatModule module() {
      return module;
    }
    @Override public HeatBlockI type() {
      return (HeatBlockI) block;
    }

    @Override public Seq<Building> heatProximity(Building build) {
      return HasHeat.super.heatProximity(build).add(getLink()).removeAll(Objects::isNull);
    }

    @Override
    public boolean onConfigureBuildTapped(Building other) {
      if (other == this) {
        configure(-1);
        return true;
      }
      if (linkValid(Point2.unpack(pos()), Point2.unpack(other.pos())) && other instanceof HeatBridgeBuild next) {
        if (next.link == pos()) {
          configure(-1);
          next.configure(Point2.unpack(pos()).sub(Point2.unpack(next.pos())));
          return true;
        }
        if (next.pos() != link) {
          configure(Point2.unpack(next.pos()).sub(Point2.unpack(pos())));
        } else {
          configure(-1);
        }
      }
      return false;
    }

    @Override public void updateTile() {
      updateHeat(this);
    }

    @Override
    public void draw() {
      super.draw();
      drawBridge();
    }
    @Override
    public void drawSelect() {
      super.drawSelect();
      SWDraw.square(Pal.accent, x, y,8f, 0f);
      if (getLink() != null) SWDraw.square(Pal.accent, getLink().x, getLink().y, 8f, 0f);
      for (int i = 0; i < 4; i++) {
        Tmp.v1.trns(i * 90, 6f);
        Tmp.v2.trns(i * 90, 8 * (range + 1));
        if (getLink() != null) {
          float angle = Math.round(tile().angleTo(getLink().x, getLink().y));
          if (angle == i * 90) {
            Tmp.v1.trns(angle, 7.75f);
            SWDraw.line(
                    Pal.accent,
                    x + Tmp.v1.x,
                    y + Tmp.v1.y,
                    angle,
                    tile().dst(getLink().x, getLink().y) - 15.5f
            );
            continue;
          }
        }
        Drawf.dashLine(Pal.accent, x + Tmp.v1.x, y + Tmp.v1.y, x + Tmp.v2.x, y + Tmp.v2.y);
      }
    }
    @Override
    public void drawConfigure() {
      SWDraw.square(Pal.accent, x, y,8f, 0f);
      if (getLink() != null) {
        float angle = Math.round(tile().angleTo(getLink().x, getLink().y));
        SWDraw.square(Pal.accent, getLink().x, getLink().y, 8f, 0f);

        Tmp.v1.trns(angle, 7.75f);
        SWDraw.line(
                Pal.accent,
                x + Tmp.v1.x,
                y + Tmp.v1.y,
                angle,
                tile().dst(getLink().x, getLink().y) - 15.5f
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
