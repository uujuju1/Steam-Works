package sw.entities.units;

import mindustry.entities.*;
import mindustry.gen.*;

public class CollisionlessLegsUnit extends LegsUnit {
	@Override public int classId() {
		return EntityMaps.ids.get(getClass().getName());
	}

	@Override public EntityCollisions.SolidPred solidity() {
		return null;
	}
}
