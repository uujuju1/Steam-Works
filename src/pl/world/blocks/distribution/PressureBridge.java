package pl.world.blocks.distribution;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Point2;
import arc.struct.Seq;
import arc.util.Tmp;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.core.Renderer;
import mindustry.entities.TargetPriority;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.logic.Ranged;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.meta.BlockGroup;
import pl.world.blocks.pressure.*;

import static mindustry.Vars.*;

public class PressureBridge extends Block implements PressureBlock {
  public TextureRegion bridgeRegion, endRegion;
  public Pressure pressureC = new Pressure();
  public float range = 80f;

  public PressureBridge(String name) {
    super(name);
    update = true;
    solid = true;
    configurable = true;
    group = BlockGroup.transportation;
    noUpdateDisabled = true;
    copyConfig = false;
    priority = TargetPriority.transport;

    config(Point2.class, (PressureBridgeBuild tile, Point2 point) -> tile.currentLink = Point2.pack(tile.tileX() + point.x, tile.tileY() + point.y));
    config(Integer.class, (PressureBridgeBuild tile, Integer i) -> tile.currentLink = i);
  }

  @Override public Pressure getPressure() {
    return pressureC;
  }

  @Override
  public void setBars() {
    super.setBars();
    addBar("pressure", (PressureBridgeBuild entity) -> new Bar(Core.bundle.get("bar.pressure"), Pal.accent, entity::pressureMap));
  }
  @Override
  public void setStats() {
    super.setStats();
    addPressureStats(stats);
  }

  @Override
  public void drawPlace(int x, int y, int rotation, boolean valid) {
    super.drawPlace(x, y, rotation, valid);
    Drawf.dashCircle(x + offset, y + offset, range, Pal.accent);
  }

  @Override
  public void load() {
    super.load();
    bridgeRegion = Core.atlas.find(name + "-bridge");
    endRegion = Core.atlas.find(name + "-end");
  }

  public class PressureBridgeBuild extends Building implements PressureBuild<PressureBridge>, Ranged {
    public PressureModule module = new PressureModule();
    public int currentLink;

    public boolean valid(Building other) {
      return other instanceof PressureBridgeBuild && Mathf.dst(x, y, other.x, other.y) < range();
    }

    public Building getLink() {
      if (world.tile(currentLink) != null) return world.tile(currentLink).build;
      return null;
    }

    @Override public float range() {return range;}

    @Override public PressureBridge getBlock() {
      return (PressureBridge) block;
    }
    @Override public PressureModule getModule() {
      return module;
    }
    @Override public Seq<Building> getPressureBuilds(Building with) {
      if (getLink() == this || getLink() == null) return PressureBuild.super.getPressureBuilds(with);
      return PressureBuild.super.getPressureBuilds(with).add(world.tile(currentLink).build);
    }

    @Override
    public void draw() {
      super.draw();
      if (getLink() != null) {
        Building b = getLink();
        Draw.z(Layer.blockOver);
        Draw.alpha(Renderer.bridgeOpacity);
        Tmp.v1.set(x, y).sub(b.x, b.y).setLength(tilesize/2f).scl(-1);

        Lines.stroke(tilesize);
        Lines.line(bridgeRegion, x + Tmp.v1.x, y + Tmp.v1.y, b.x - Tmp.v1.x, b.y - Tmp.v1.y, false);

        Draw.rect(endRegion, x + Tmp.v1.x, y + Tmp.v1.y, Angles.angle(x, y, b.x, b.y));
        Draw.rect(endRegion, b.x - Tmp.v1.x, b.y - Tmp.v1.y, Angles.angle(x, y, b.x, b.y));
      }
    }

    @Override public void drawSelect() {
      Drawf.dashCircle(x, y, range(), Pal.accent);
    }

    @Override
    public void drawConfigure() {
      Drawf.dashCircle(x, y, range(), Pal.accent);
      Drawf.select(x, y, size + 4f, Pal.accent);
      Building other = getLink();
      if (other != null) Drawf.select(other.x, other.y, other.block().size + 4f, Pal.place);
    }

    @Override
    public boolean onConfigureBuildTapped(Building other) {
      if (other instanceof PressureBridgeBuild) {
        if (((PressureBridgeBuild) other).getLink() == this) {
          configure(other.pos());
          other.configure(-1);
          return true;
        }
      }
      if (valid(other)) {
        if (other.pos() == pos()) {
          configure(-1);
        } else {
          configure(other.pos());
        }
        return false;
      }
      return true;
    }

    @Override public void updateTile() {
      updatePressure(self());
    }

    @Override
    public void write(Writes write) {
      super.write(write);
      module.write(write);
      write.i(currentLink);
    }
    @Override
    public void read(Reads read, byte revision) {
      super.read(read, revision);
      module.read(read);
      currentLink = read.i();
    }
  }
}
