package sw.world.interfaces;

import arc.struct.*;
import mindustry.world.*;
import sw.world.MultiShape.*;

public interface MultiEnvI {
	/**
	 * qol, just casts this to Block
	 * @see Block
	 */
	default Block asBlock() {
		return (Block) this;
	}

	/**
	 * @return classes that this block can connect to
	 * @apiNote i don't know what'll happen if the group is not mutual
	 */
	default Seq<Block> multiShapeGroup() {
		return Seq.with(asBlock());
	}

	default MultiShapeBuild createMultiShapeBuild() {
		return new MultiShapeBuild();
	}
}
