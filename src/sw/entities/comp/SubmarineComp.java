package sw.entities.comp;

import arc.util.*;
import ent.anno.Annotations.*;
import mindustry.gen.*;
import mindustry.type.*;
import sw.type.*;

@EntityComponent
abstract class SubmarineComp implements Unitc {
	@Import UnitType type;
	transient float submergeTime;

	public boolean submerged() {
		return submergeTime <= 0;
	}

	@Override public SWUnitType type() {
		return (SWUnitType) type;
	}

	@Override
	public void update() {
		if (!submerged()) submergeTime -= Time.delta;
		if (isShooting()) submergeTime = type().vulnerabilityTime;
	}
}
