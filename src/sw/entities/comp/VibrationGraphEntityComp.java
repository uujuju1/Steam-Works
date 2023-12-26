package sw.entities.comp;

import arc.util.io.*;
import mindustry.gen.*;
import sw.gen.*;
import sw.world.graph.*;

import static ent.anno.Annotations.*;

@EntityComponent(base = true)
@EntityDef(VibrationGraphEntityc.class)
abstract class VibrationGraphEntityComp implements Entityc{
	transient VibrationGraph graph;

	@Override
	public void update() {
		if (graph == null || graph.entity != self()) {
			remove();
		} else {
			graph.update();
		}
	}

	@Override public void read(Reads read) {
		graph = new VibrationGraph(self());
		graph.read(read);
	}
	@Override public void afterRead() {
		graph.afterRead();
	}
	@Override public void write(Writes write) {
		graph.write(write);
	}
}
