package sw.entities.comp;

import arc.util.io.*;
import ent.anno.Annotations.*;
import mindustry.gen.*;
import sw.gen.*;
import sw.world.graph.*;

@EntityComponent(base = true)
@EntityDef(ForceGraphEntityc.class)
abstract class ForceGraphEntityComp implements Entityc{
	transient ForceGraph graph;

	@Override
	public void update() {
		if (graph == null || graph.entity != self()) {
			remove();
		} else {
			graph.update();
		}
	}

	@Override public void read(Reads read) {
		graph = new ForceGraph(self());
		graph.read(read);
	}
	@Override public void afterRead() {
		graph.afterRead();
	}
	@Override public void write(Writes write) {
		graph.write(write);
	}
}
