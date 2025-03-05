package sw.world.blocks.defense;

import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;

public class SWContinuousTurret extends ConsumeTurret {
	public SWContinuousTurret(String name) {
		super(name);
	}

	public class SWContinuousTurretBuild extends ConsumeTurretBuild {
		public Seq<BulletEntry> bullets = new Seq<>();
		public float lastLength = size * 4f;

		@Override public float activeSoundVolume() {
			return 1f;
		}

		@Override
		protected void handleBullet(@Nullable Bullet bullet, float offsetX, float offsetY, float angleOffset) {
			if(bullet != null) {
				bullets.add(new BulletEntry(bullet, offsetX, offsetY, angleOffset, 0f));

				//make sure the length updates to the last set value
				Tmp.v1.trns(rotation, shootY + lastLength).add(x, y);
				bullet.aimX = Tmp.v1.x;
				bullet.aimY = Tmp.v1.y;
			}
		}

		@Override public boolean shouldActiveSound() {
			return bullets.any();
		}

		protected void updateBullet(BulletEntry entry) {
			float
				bulletX = x + Angles.trnsx(rotation - 90, shootX + entry.x, shootY + entry.y),
				bulletY = y + Angles.trnsy(rotation - 90, shootX + entry.x, shootY + entry.y),
				angle = rotation + entry.rotation;

			entry.bullet.rotation(angle);
			entry.bullet.set(bulletX, bulletY);

			if(isShooting() && hasAmmo()) {
				entry.bullet.time = entry.bullet.lifetime * entry.bullet.type.optimalLifeFract * shootWarmup;
				entry.bullet.keepAlive = true;
			}
		}

		@Override
		protected void updateReload() {
			float multiplier = hasAmmo() ? peekAmmo().reloadMultiplier : 1f;
			reloadCounter += delta() * multiplier;

			if (reloadCounter > reload && bullets.any()) {
				BulletEntry entry = bullets.first();
				float
					bulletX = x + Angles.trnsx(rotation - 90, shootX + entry.x, shootY + entry.y),
					bulletY = y + Angles.trnsy(rotation - 90, shootX + entry.x, shootY + entry.y),
					angle = rotation + entry.rotation;

				(shootEffect == null ? entry.bullet.type.shootEffect : shootEffect).at(bulletX, bulletY, angle, entry.bullet.type.hitColor);
				(smokeEffect == null ? entry.bullet.type.smokeEffect : smokeEffect).at(bulletX, bulletY, angle, entry.bullet.type.hitColor);
				reloadCounter = 0;
				consume();
			}

			reloadCounter = Math.min(reloadCounter, reload);
		}

		@Override
		protected void updateShooting() {
			if(bullets.any()) {
				return;
			}

			if(canConsume() && !charging() && shootWarmup >= minWarmup) {
				shoot(peekAmmo());
			}
		}

		@Override
		public void updateTile() {
			bullets.removeAll(b -> !b.bullet.isAdded() || b.bullet.type == null || b.bullet.owner != this);

			super.updateTile();

			unit.ammo(canConsume() ? unit.type().ammoCapacity : 0);

			if (bullets.any()) {
				bullets.each(this::updateBullet);

				wasShooting = true;
				heat = 1f;
				curRecoil = recoil;
			}
		}

		@Override public BulletType useAmmo() {
			return shootType;
		}
	}
}
