package sw.entities.comp;

import ent.anno.Annotations.*;
import mindustry.entities.EntityCollisions.*;
import mindustry.gen.*;

/**
 * makes solids not solids for this unit
 */
@EntityComponent
abstract class IntangibleComp implements Unitc {
	@Replace(1)
	@Override
	public SolidPred solidity() {
		return null;
	}
}
