package sw.ai;

import arc.math.*;
import arc.util.*;
import mindustry.entities.part.*;
import mindustry.entities.units.*;
import sw.entities.comp.*;

public class ShieldAI extends AIController {
	public float getRotation() {
		if (!(unit instanceof UnitTetherUnit u) || !(u.unit() instanceof ShieldedUnit b)) return 0f;
		if (b.mounts.length > 0) {
			WeaponMount first = b.mounts[0];
			DrawPart.params.set(first.warmup, first.reload / b.type().weapons.first().reload, first.smoothReload, first.heat, first.recoil, first.charge, u.x, u.y, u.rotation);
		} else {
			DrawPart.params.set(0, 0, 0, 0, 0, 0, u.x, u.y, u.rotation);
		}
		return Mathf.lerp(
			b.type().shieldStartAng + (b.rotation() - 90 + b.type().shieldEndAng /(b.type().shields - 1f) * b.units.indexOf(u)),
			b.type().shieldShootingStartAng + (b.rotation() - 90 + b.type().shieldShootingEndAng /(b.type().shields - 1f) * b.units.indexOf(u)),
			b.type().shieldProgress.get(DrawPart.params)
		);
	}

	@Override
	public void updateMovement() {
		if (unit instanceof UnitTetherUnit u && u.unit() instanceof ShieldedUnit b) {
			Tmp.v1.trns(getRotation(), b.type().shieldSeparateRadius);
			u.set(Tmp.v1.add(b));
			u.rotation(u.angleTo(b) + 180f);
		}
	}
}
