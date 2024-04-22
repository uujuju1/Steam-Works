package sw.entities.comp;

import arc.math.*;
import ent.anno.Annotations.*;
import mindustry.gen.*;
import mindustry.type.*;
import sw.type.*;

@EntityComponent
abstract class RevealComp implements Unitc {
	@Import UnitType type;

	transient float revealTime;

	/**
	 * reveals the unit, must be called every frame to slowly reveal the unit
	 */
	public void reveal() {
		revealTime = Mathf.approachDelta(revealTime, 1, type().revealSpeed * 2f);
	}

	@Override public SWUnitType type() {
		return (SWUnitType) type;
	}

	@Override public void update() {
		revealTime = Mathf.approachDelta(revealTime, 0, type().revealSpeed);
	}
}
