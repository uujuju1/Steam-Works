package sw.entities.units;

import arc.util.*;
import mindustry.gen.*;
import sw.entities.*;
import sw.type.*;

public class CopterUnit extends UnitEntity {
	public RotorMount[] rotors;

	@Override
	public void add() {
		super.add();
		resetRotors();
	}

	@Override public int classId() {
		return EntityMaps.ids.get(getClass().getName());
	}

	public void resetRotors() {
		rotors = new RotorMount[((SWUnitType) type).rotors.size];
		for (int i = 0; i < ((SWUnitType) type).rotors.size; i++) {
			int finalI = i;
			rotors[i] = new RotorMount() {{
				rotor = ((SWUnitType) type).rotors.get(finalI);
			}};
		}
	}

	@Override
	public void update() {
		super.update();

		for(RotorMount rotor : rotors) rotor.rotor.update(this, rotor);

		if (dead && ((SWUnitType) type).rotateDeathSpeed != 0) rotation += Time.delta * ((SWUnitType) type).rotateDeathSpeed;

	}
}
