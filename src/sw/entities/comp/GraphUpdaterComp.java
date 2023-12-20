package sw.entities.comp;

import ent.anno.Annotations.*;
import mindustry.gen.*;
import sw.gen.*;
import sw.world.graph.*;

@EntityComponent(base = true)
@EntityDef(GraphUpdaterc.class)
abstract class GraphUpdaterComp implements Entityc {
	transient Graph graph;

	@Override
	public void update() {
		graph.update();
	}
}
