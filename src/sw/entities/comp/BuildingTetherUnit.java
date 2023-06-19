package sw.entities.comp;

import arc.util.*;
import mindustry.gen.*;
import sw.entities.*;

public class BuildingTetherUnit extends UnitEntity implements BuildingTetherc {
	public @Nullable Building building;

	@Override public Building building() {
		return building;
	}

	@Override public void building(Building building) {
		this.building = building;
	}

	@Override public String toString() {
		return "BuildingTetherUnit#" + id;
	}
	@Override public int classId() {
		return SWEntityMapping.idMap.get(getClass());
	}

	@Override
	public void update() {
		super.update();
		if (building == null || !building.isValid() || building.team != team) Call.unitDespawn(this);
	}
}
