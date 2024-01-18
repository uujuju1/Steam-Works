package sw.entities.comp;

import arc.math.*;
import arc.util.*;
import ent.anno.Annotations.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.type.*;
import sw.type.*;

@EntityComponent
abstract class CopterComp implements Unitc {
	@Import boolean dead;
	@Import float rotation;
	@Import UnitType type;

	transient float rotorBlur = 1;

	@Override public SWUnitType type() {
		return (SWUnitType) type;
	}

	@Override
	public void update() {
		if (dead) {
			rotorBlur = Mathf.approachDelta(rotorBlur, 0, type().rotorSlowDown);
			if (!Vars.state.isPaused() && type().rotatesDeath) rotation+= Time.delta * type().rotateDeathSpeed;
		}
	}
}
