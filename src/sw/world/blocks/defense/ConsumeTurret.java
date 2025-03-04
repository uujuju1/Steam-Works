package sw.world.blocks.defense;

import mindustry.content.*;
import mindustry.entities.bullet.*;

public class ConsumeTurret extends SWTurret {
	public BulletType shootType = Bullets.placeholder;

	public ConsumeTurret(String name) {
		super(name);
	}

	public class ConsumeTurretBuild extends SWTurretBuild {
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

		@Override public BulletType useAmmo(){
			return shootType;
		}
	}
}
