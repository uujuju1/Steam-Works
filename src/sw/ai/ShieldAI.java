package sw.ai;

import arc.math.*;
import arc.util.*;
import mindustry.entities.part.*;
import mindustry.entities.units.*;
import sw.gen.*;
import sw.type.*;

public class ShieldAI extends AIController {
	public float getRotation() {
		if (!(unit instanceof TetherUnitc u) || !(u.tetherUnit() instanceof Shieldedc b) || !(b.type() instanceof SWUnitType type)) return 0f;
		if (b.mounts().length > 0) {
			WeaponMount first = b.mounts()[0];
			DrawPart.params.set(first.warmup, first.reload / b.type().weapons.first().reload, first.smoothReload, first.heat, first.recoil, first.charge, u.x(), u.y(), u.rotation());
		} else {
			DrawPart.params.set(0, 0, 0, 0, 0, 0, u.x(), u.y(), u.rotation());
		}
		return Mathf.lerp(
			type.shieldStartAng + (b.rotation() - 90 + type.shieldEndAng /(type.shields - 1f) * b.units().indexOf(unit)),
			type.shieldShootingStartAng + (b.rotation() - 90 + type.shieldShootingEndAng /(type.shields - 1f) * b.units().indexOf(unit)),
			type.shieldProgress.get(DrawPart.params)
		);
	}

	@Override
	public void updateMovement() {
		if (unit instanceof TetherUnitc u && u.tetherUnit().type() instanceof SWUnitType type) {
			Tmp.v1.trns(getRotation(), type.shieldSeparateRadius);
			u.set(Tmp.v1.add(u.tetherUnit()));
			u.rotation(u.angleTo(u.tetherUnit()) + 180f);
		}
	}
}
