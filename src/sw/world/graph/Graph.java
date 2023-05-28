package sw.world.graph;

import arc.util.*;
import sw.entities.comp.*;

public abstract class Graph {
	public final @Nullable GraphUpdater entity;

	public Graph() {
		entity = new GraphUpdater();
		entity.graph = this;
	}

	public void checkEntity() {
		if (entity != null) entity.add();
	}

	public abstract void update();
}
