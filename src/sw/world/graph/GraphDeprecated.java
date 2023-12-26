package sw.world.graph;

import arc.util.*;
import sw.gen.*;

public abstract class GraphDeprecated {
	public final @Nullable GraphUpdater entity;

	public GraphDeprecated() {
		entity = GraphUpdater.create();
		entity.graphDeprecated = this;
	}

	public void addGraph() {
		entity.add();
	}
	public void removeGraph() {
		entity.remove();
	}

	public abstract void update();
}
