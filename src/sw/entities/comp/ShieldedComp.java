package sw.entities.comp;

import arc.struct.*;
import arc.util.*;
import ent.anno.Annotations.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.type.*;
import sw.gen.*;
import sw.type.*;

@EntityComponent
abstract class ShieldedComp implements Unitc{
	@Import UnitType type;
	@Import Team team;
	@Import float x, y;
	transient IntSeq unitIDs = new IntSeq();
	transient Seq<Unit> units = new Seq<>();
	transient float progress;

	@Nullable public Unit canReplace() {
		return units.find(u -> u.dead());
	}

	@Override public SWUnitType type() {
		return (SWUnitType) type;
	}

	@Override
	public void update() {
		while(!unitIDs.isEmpty()) {
			int id = unitIDs.pop();
			if (Groups.unit.getByID(id) != null && Groups.unit.getByID(id) instanceof UnitTetherc unit) units.add(Groups.unit.getByID(id));
		}

		if ((canReplace() != null || units.size < type().shields) && type().shieldUnit != null) {
			progress += Time.delta/type().shieldConstructTime;
			if (progress > 1f) {
				progress %= 1f;

				Unit unit = type().shieldUnit.spawn(team, x, y);
				if (units.size >= type().shields) {
					units.replace(canReplace(), unit);
				} else {
					units.add(unit);
				}
				if (unit instanceof TetherUnitc u) u.tetherUnit(self());
			}
		}
	}
}
