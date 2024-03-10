package sw.world.blocks.defense;

import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.world.blocks.defense.turrets.*;

public class SWConsumeTurret extends Turret {
	public BulletType shootType = Bullets.placeholder;

	public SWConsumeTurret(String name) {
		super(name);
	}

	public class SWConsumeTurretBuild extends TurretBuild {
		@Override public BulletType useAmmo(){
			return shootType;
		}
		@Override public boolean hasAmmo(){
			return canConsume();
		}
		@Override public BulletType peekAmmo(){
			return shootType;
		}

		@Override
		protected void shoot(BulletType type) {
			super.shoot(type);
			consume();
		}
	}
}
