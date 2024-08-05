package sw.entities.comp;

import arc.util.*;
import ent.anno.Annotations.*;
import mindustry.gen.*;
import sw.gen.*;
import sw.world.graph.*;

@EntityComponent(base = true)
@EntityDef(value = {GasGraphUpdaterc.class}, serialize = false)
abstract class GasGraphUpdaterComp implements Entityc {
	public transient GasGraph graph;

	public GasGraphUpdater setGraph(GasGraph newGraph) {
		graph = newGraph;
		return self();
	}

	@Override
	public void update() {
		if (graph != null) {
			graph.update();
		} else {
			Log.err("Graph is null. Removing.");
			remove();
		}
	}
}
