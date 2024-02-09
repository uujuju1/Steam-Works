package sw.entities.comp;

import mindustry.gen.*;
import sw.gen.*;
import sw.world.graph.*;

import static ent.anno.Annotations.*;

@EntityComponent(base = true)
@EntityDef(value = {VibrationGraphEntityc.class}, genIO = false)
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
}
