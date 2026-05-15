package sw.type;

import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.type.*;

public class TeleportWeapon extends Weapon {
	public TeleportWeapon(float reload) {
		display = false;
		mirror = false;
		x = y = 0f;
		this.reload = reload;
		rotate = true;
		rotateSpeed = 361f;
	}

	@Override
	protected void shoot(Unit unit, WeaponMount mount, float shootX, float shootY, float rotation) {
		unit.set(mount.aimX, mount.aimY);
		shootSound.at(mount.aimX, mount.aimY);
	}
}
