package sw.entities.comp;

import arc.util.io.*;
import mindustry.gen.*;
import sw.entities.*;
import sw.type.*;

import static mindustry.Vars.*;

public class UnitTetherUnit extends UnitEntity {
	public Unit unit;
	public int unitID = -1;

	@Override public String toString() {
		return "UnitTetherUnit#" + id;
	}
	@Override public SWUnitType type() {
		return (SWUnitType) super.type();
	}
	@Override public int classId() {
		return SWEntityMapping.idMap.get(getClass());
	}

	public Unit unit() {
		return unit;
	}
	public void unit(Unit unit) {
     this.unit = unit;
	}

	@Override
	public void update() {
		super.update();

		if(unitID != -1) {
			unit(Groups.unit.getByID(unitID));
			if(unit == null || !net.client()) unitID = -1;
		}

		if (unit != null && unit.team != team) team(unit.team);
		if (unit() == null || !unit().isValid() || unit().dead()) Call.unitDespawn(this);
	}

	@Override public void wobble() {}

	@Override
	public void read(Reads read) {
		super.read(read);
		unitID = read.i();
	}
	@Override
	public void write(Writes write) {
		super.write(write);
		write.i(unit.id);
	}
}
