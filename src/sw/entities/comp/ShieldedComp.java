package sw.entities.comp;

import arc.math.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import ent.anno.Annotations.*;
import mindustry.entities.part.*;
import mindustry.entities.units.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.type.*;
import sw.gen.*;
import sw.type.*;

@EntityComponent
abstract class ShieldedComp implements Unitc{
	@Import UnitType type;
	@Import Team team;
	@Import WeaponMount[] mounts;
	@Import float x, y, rotation;
	transient IntSeq unitIDs = new IntSeq();
	transient Seq<UnitTetherUnit> units = new Seq<>();
	transient float progress;

	@Nullable public UnitTetherUnit canReplace() {
		return units.find(u -> !u.isValid());
	}

	public float getRotation(UnitTetherUnit unit) {
		if (mounts.length > 0) {
			WeaponMount first = mounts[0];
			DrawPart.params.set(first.warmup, first.reload / first.weapon.reload, first.smoothReload, first.heat, first.recoil, first.charge, x, y, rotation);
		} else {
			DrawPart.params.set(0, 0, 0, 0, 0, 0, x, y, rotation);
		}
		float place = units.indexOf(unit)/Math.max(1, units.size - 1f);
		return Mathf.lerp(
			rotation + Mathf.lerp(type().shieldStartAng, type().shieldEndAng, place),
			rotation + Mathf.lerp(type().shieldShootingStartAng, type().shieldShootingEndAng, place),
			type().shieldProgress.get(DrawPart.params)
		);
	}

	@Override public SWUnitType type() {
		return (SWUnitType) type;
	}

	@Override
	public void update() {
		while(!unitIDs.isEmpty()) {
			int id = unitIDs.pop();
			if (Groups.unit.getByID(id) != null && Groups.unit.getByID(id) instanceof UnitTetherUnit unit) units.add(unit);
		}

		if ((canReplace() != null || units.size < type().shields) && type().shieldUnit != null) {
			progress += Time.delta/type().shieldConstructTime;
			if (progress > 1f) {
				progress %= 1f;

				UnitTetherUnit unit = (type().shieldUnit.create(team) instanceof UnitTetherUnit u) ? u : null;
				if (unit != null) {
					unit.set(x, y);
					unit.tetherUnit(self());
					unit.add();
					if (units.size >= type().shields) {
						units.replace(canReplace(), unit);
					} else {
						units.add(unit);
					}
				}
			}
		}
		for (UnitTetherUnit unit : units) {
			unit.set(Tmp.v1.trns(getRotation(unit), type().shieldSeparateRadius).add(self()));
			unit.rotation(angleTo(unit));
		}
	}

	@Override
	public void read(Reads read) {
		byte size = read.b();
		for (int i = 0; i < size; i++) {
			unitIDs.add(read.i());
		}
	}

	@Override
	public void write(Writes write) {
		write.b(units.size);
		units.each(unit -> {
			write.i(unit.id);
		});
	}
}
