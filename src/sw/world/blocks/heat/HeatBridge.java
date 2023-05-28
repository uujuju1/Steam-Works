package sw.world.blocks.heat;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.*;
import sw.util.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

import java.util.*;


public class HeatBridge extends Block {
  public HeatConfig heatConfig = new HeatConfig();
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

  @Override
  public void setBars() {
    super.setBars();
    addBar("heat", (HeatBridgeBuild entity) -> new Bar(Core.bundle.get("bar.heat"), Pal.accent, entity::fraction));
  }
  @Override
  public void setStats() {
    super.setStats();
    heatConfig.heatStats(stats);
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

    @Override public HeatModule heat() {
      return module;
    }
    @Override public HeatConfig heatC() {
      return heatConfig;
    }

    @Override public Seq<Building> heatProximity() {
      return HasHeat.super.heatProximity().add(getLink()).removeAll(Objects::isNull);
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
