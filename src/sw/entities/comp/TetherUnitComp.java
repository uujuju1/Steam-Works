package sw.entities.comp;

import arc.util.*;
import arc.util.io.*;
import ent.anno.Annotations.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.type.*;
import sw.type.*;

@EntityComponent
abstract class TetherUnitComp implements Unitc {
	@Import UnitType type;
	@Import Team team;

	//spawner unit cannot be read directly for technical reasons.
	public transient @Nullable Unit tetherUnit;
	public int tetherUnitID = -1;

	@Override public SWUnitType type() {
		return (SWUnitType) type;
	}

	@Override
	public void update(){
		if (tetherUnitID != -1) {
			tetherUnit = Groups.unit.getByID(tetherUnitID);
			tetherUnitID = -1;
		}
		if(tetherUnit == null || !tetherUnit.isValid() || tetherUnit.team != team){
			Call.unitDespawn(self());
		}
	}

	@Override
	public void read(Reads read) {
		tetherUnitID  = read.i();
	}

	@Override
	public void write(Writes write) {
		write.i(tetherUnit.id);
	}
}
