package sw.entities.bullet;

import arc.struct.*;
import arc.util.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.game.*;
import mindustry.gen.*;

public class MultiBulletType extends BulletType {
	public Seq<BulletType> bullets = new Seq<>();

	public MultiBulletType() {
		super();
		damage = lifetime = splashDamage = 0;
		speed = 10239102;
	}

	@Override
	public Bullet create(Entityc owner, Team team, float x, float y, float angle, float damage, float velocityScl, float lifetimeScl, Object data, Mover mover, float aimX, float aimY) {
		for (BulletType b: bullets) {
			b.create(owner, team, x, y, angle, damage, velocityScl, lifetimeScl, data, mover, aimX, aimY);
		}
		if (lifetime > 0) Log.warn("no, no, no, no, this is not a bullet.", lifetime, true);
		return null;
	}
}
