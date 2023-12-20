package sw.world.graph;

import arc.util.*;
import sw.gen.*;

public abstract class Graph {
	public final @Nullable GraphUpdater entity;

	public Graph() {
		entity = GraphUpdater.create();
		entity.graph = this;
	}

	public void addGraph() {
		entity.add();
	}
	public void removeGraph() {
		entity.remove();
	}

	public abstract void update();
}
