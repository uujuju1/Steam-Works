package sw.entities.comp;

import ent.anno.Annotations.*;
import mindustry.gen.*;
import sw.gen.*;
import sw.world.graph.*;

@EntityComponent(base = true)
@EntityDef(value = GraphUpdaterc.class, genIO = false, serialize = false)
abstract class GraphUpdaterComp implements Entityc {
	transient GraphDeprecated graphDeprecated;

	@Override
	public void update() {
		if (graphDeprecated != null) graphDeprecated.update();
	}
}
