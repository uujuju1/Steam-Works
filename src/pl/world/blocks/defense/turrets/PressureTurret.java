package pl.world.blocks.defense.turrets;

import arc.Core;
import mindustry.content.Bullets;
import mindustry.entities.bullet.BulletType;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.blocks.defense.turrets.Turret;
import pl.world.blocks.pressure.*;

public class PressureTurret extends Turret implements PressureBlock {
  public BulletType shootType = Bullets.placeholder;
  public Pressure pressureC = new Pressure();

  public PressureTurret(String name) {
    super(name);
  }

  @Override public Pressure getPressure() {
    return pressureC;
  }

  @Override
  public void setBars() {
    super.setBars();
    addBar("pressure", (PressureTurretBuild entity) -> new Bar(Core.bundle.get("bar.pressure"), Pal.accent, entity::pressureMap));
  }
  @Override
  public void setStats() {
    super.setStats();
    addPressureStats(stats);
  }

  public class PressureTurretBuild extends TurretBuild implements PressureBuild<PressureTurret> {
    public PressureModule module = new PressureModule();

    @Override public PressureTurret getBlock() {
      return (PressureTurret) block;
    }
    @Override public PressureModule getModule() {
      return module;
    }

    @Override
    public void updateTile() {
      unit.ammo(getPressure() * unit.type().ammoCapacity);
      super.updateTile();
      updatePressure(self());
    }

    @Override
    public BulletType useAmmo() {
      consume();
      return shootType;
    }
    @Override public BulletType peekAmmo() {
      return shootType;
    }

    @Override public boolean hasAmmo() {
      return true;
    }
  }
}
