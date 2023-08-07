package sw.entities.bullet;

import arc.struct.*;
import arc.util.*;
import mindustry.ai.types.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.world.blocks.*;

import static mindustry.Vars.*;

public class MultiBulletType extends BulletType {
	public Seq<BulletType> bullets = new Seq<>();

	public MultiBulletType() {
		super();
		damage = lifetime = splashDamage = 0;
		speed = 10239102;
	}

	@Override
	public @Nullable Bullet create(@Nullable Entityc owner, @Nullable Entityc shooter, Team team, float x, float y, float angle, float damage, float velocityScl, float lifetimeScl, Object data, @Nullable Mover mover, float aimX, float aimY){
		for (BulletType b : bullets) {
			if (spawnUnit != null) {
				//don't spawn units clientside!
				if (!net.client()) {
					Unit spawned = spawnUnit.create(team);
					spawned.set(x, y);
					spawned.rotation = angle;
					//immediately spawn at top speed, since it was launched
					if (spawnUnit.missileAccelTime <= 0f) {
						spawned.vel.trns(angle, spawnUnit.speed);
					}
					//assign unit owner
					if (spawned.controller() instanceof MissileAI ai) {
						if (shooter instanceof Unit unit) {
							ai.shooter = unit;
						}

						if (shooter instanceof ControlBlock control) {
							ai.shooter = control.unit();
						}

					}
					spawned.add();
				}
				//Since bullet init is never called, handle killing shooter here
				if (killShooter && owner instanceof Healthc h && !h.dead()) h.kill();

				//no bullet returned
				return null;
			}

			Bullet bullet = Bullet.create();
			bullet.type = b;
			bullet.owner = owner;
			bullet.team = team;
			bullet.time = 0f;
			bullet.originX = x;
			bullet.originY = y;
			bullet.aimTile = world.tileWorld(aimX, aimY);
			bullet.aimX = aimX;
			bullet.aimY = aimY;
			bullet.initVel(angle, b.speed * velocityScl);
			if (backMove) {
				bullet.set(x - bullet.vel.x * Time.delta, y - bullet.vel.y * Time.delta);
			} else {
				bullet.set(x, y);
			}
			bullet.lifetime = b.lifetime * lifetimeScl;
			bullet.data = data;
			bullet.drag = b.drag;
			bullet.hitSize = b.hitSize;
			bullet.mover = mover;
			bullet.damage = (damage < 0 ? b.damage : damage) * bullet.damageMultiplier();
			//reset trail
			if (bullet.trail != null) {
				bullet.trail.clear();
			}
			bullet.add();

			if (b.keepVelocity && owner instanceof Velc v) bullet.vel.add(v.vel());
		}
		return null;
	}
}
