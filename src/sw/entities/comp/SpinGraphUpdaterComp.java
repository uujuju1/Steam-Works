package sw.entities.comp;

import arc.util.*;
import ent.anno.Annotations.*;
import mindustry.gen.*;
//import sw.gen.*;
import sw.world.graph.*;

@EntityComponent(base = true)
//@EntityDef(value = {SpinGraphUpdaterc.class}, serialize = false)
abstract class SpinGraphUpdaterComp implements Entityc {
	public transient SpinGraph graph;

//	public SpinGraphUpdater setGraph(SpinGraph newGraph) {
//		graph = newGraph;
//		return self();
//	}

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
