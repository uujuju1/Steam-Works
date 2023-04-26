package sw.world.blocks.defense;

import arc.*;
import arc.struct.*;
import mindustry.entities.bullet.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.meta.*;
import sw.*;
import sw.world.consumers.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class HeatTurret extends Turret implements HeatBlockI {
	public HeatConfig heatConfig = SWVars.baseConfig.copy();
	public ConsumeHeatTrigger consumer = new ConsumeHeatTrigger(0, 0, true);
	public BulletType shootType;

	public HeatTurret(String name) {
		super(name);
	}

	@Override public HeatConfig heatConfig() {
		return heatConfig;
	}

	@Override
	public void setStats() {
		super.setStats();
		heatStats(stats);
		consumer.display(stats);
		stats.add(Stat.ammo, StatValues.ammo(ObjectMap.of(this, shootType)));
	}
	@Override
	public void setBars() {
		super.setBars();
		addBar("heat", (HeatTurretBuild entity) -> new Bar(Core.bundle.get("bar.heat"), Pal.accent, entity::fraction));
	}

	public class HeatTurretBuild extends TurretBuild implements HasHeat {
		HeatModule module = new HeatModule();

		@Override public HeatModule module() {
			return module;
		}
		@Override public HeatBlockI type() {
			return (HeatBlockI) block;
		}

		@Override
		public void updateTile() {
			unit.ammo(heat() - consumer.min - consumer.amount);
			super.updateTile();
			updateHeat(this);
		}

		@Override
		protected void shoot(BulletType type) {
			super.shoot(type);
			consumer.trigger(this);
		}

		@Override public BulletType useAmmo() {
			return shootType;
		}
		@Override public BulletType peekAmmo() {
			return shootType;
		}
		@Override public boolean hasAmmo() {
			return unit.ammo() > 0;
		}
	}
}
