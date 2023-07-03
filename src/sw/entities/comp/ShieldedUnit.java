package sw.entities.comp;

import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.gen.*;
import sw.entities.*;
import sw.type.*;

// sundown unit weeeeeeeee
public class ShieldedUnit extends LegsUnit{
	public Seq<Unit> units = new Seq<>();
	public IntSeq unitIDs = new IntSeq();
	public float progress = 0;

	@Override public String toString() {
		return "ShieldedUnit#" + id;
	}
	@Override public SWUnitType type() {
		return (SWUnitType) super.type();
	}
	@Override public int classId() {
		return SWEntityMapping.idMap.get(getClass());
	}

	@Override
	public void update() {
		super.update();
		while(!unitIDs.isEmpty()) {
			int id = unitIDs.pop();
			if (Groups.unit.getByID(id) != null && Groups.unit.getByID(id) instanceof UnitTetherUnit unit) units.add(unit);
		}

		if ((canReplace() != null || units.size < type().shields) && type().shieldUnit != null) {
			progress += Time.delta/type().shieldConstructTime;
			if (progress > 1f) {
				progress %= 1f;

				Unit unit = type().shieldUnit.create(team);
				if (units.size >= type().shields) {
					units.replace(canReplace(), unit);
				} else {
					units.add(unit);
				}
				if (unit instanceof UnitTetherUnit u) u.unit(this);
				unit.add();
			}
		}
	}

	@Nullable public Unit canReplace() {
		return units.find(u -> u.dead());
	}

	@Override
	public void read(Reads read) {
		super.read(read);
		short size = read.s();

		for (int i = 0; i < size; i++) {
			unitIDs.add(read.i());
		}
	}
	@Override
	public void write(Writes write) {
		super.write(write);
		write.s(units.size);

		for (int i = 0; i < units.size; i++) {
			write.i(units.get(i).id);
		}
	}
}
