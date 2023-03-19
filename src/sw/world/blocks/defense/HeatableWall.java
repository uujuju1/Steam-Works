package sw.world.blocks.defense;

import arc.Core;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.gen.Bullet;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.blocks.defense.Wall;
import sw.SWVars;
import sw.world.heat.HasHeat;
import sw.world.heat.HeatBlockI;
import sw.world.heat.HeatConfig;
import sw.world.modules.HeatModule;

import static sw.util.SWMath.heatMap;

public class HeatableWall extends Wall implements HeatBlockI {
  HeatConfig heatConfig = SWVars.baseConfig.copy();

  public HeatableWall(String name) {
    super(name);
    update = sync = true;
  }

  @Override public HeatConfig heatConfig() {
    return heatConfig;
  }

  @Override
  public void setBars() {
    super.setBars();
    addBar("heat", (HeatableWallBuild entity) -> new Bar(Core.bundle.get("bar.heat"), Pal.accent, () -> heatMap(entity.module().heat, 0f, heatConfig().maxHeat)));
  }
  @Override
  public void setStats() {
    super.setStats();
    heatStats(stats);
  }

  public class HeatableWallBuild extends WallBuild implements HasHeat {
    HeatModule module = new HeatModule();

    @Override public HeatModule module() {
      return module;
    }
    @Override public HeatBlockI type() {
      return (HeatBlockI) block;
    }

    @Override
    public boolean collision(Bullet bullet) {
      if (bullet.type instanceof BasicBulletType) {
        bullet.damage /= Math.abs(2 * fractionNeg());
      } else {
        addHeat(bullet.damage * (1 - fraction()));
      }
      return super.collision(bullet);
    }

    @Override
    public void updateTile() {
      updateHeat(this);
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
