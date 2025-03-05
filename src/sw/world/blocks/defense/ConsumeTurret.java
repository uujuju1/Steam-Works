package sw.world.blocks.defense;

import arc.struct.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.world.meta.*;

public class ConsumeTurret extends SWTurret {
	public BulletType shootType = Bullets.placeholder;

	public ConsumeTurret(String name) {
		super(name);
	}

	@Override
	public void setStats() {
		super.setStats();
		stats.add(Stat.ammo, StatValues.ammo(ObjectMap.of(this, shootType)));
	}

	public class ConsumeTurretBuild extends SWTurretBuild {
		@Override public boolean hasAmmo() {
			return canConsume();
		}

		@Override public BulletType peekAmmo() {
			return shootType;
		}

		@Override public BulletType useAmmo() {
			consume();
			return shootType;
		}
	}
}
