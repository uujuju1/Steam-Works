package sw.entities.units;

import arc.util.*;
import arc.util.io.*;
import mindustry.gen.*;
import sw.world.graph.*;

@SuppressWarnings("unchecked")
public class GraphUpdater implements Entityc {
	public boolean added;
	public int id;
	public Graph graph;

	@Override
	public int classId() {
		return 0;
	}

	public GraphUpdater setGraph(Graph newGraph) {
		graph = newGraph;
		add();
		return self();
	}

	@Override
	public int id() {
		return id;
	}

	@Override
	public void add() {
		if (!added) {
			id = Groups.all.addIndex(this);
			added = true;
		}
	}

	@Override
	public void remove() {
		if (added) {
			Groups.all.remove(this);
			added = false;
		}
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


	@Override public void id(int i) {

	}

	@Override public <T extends Entityc> T self() {
		return (T) this;
	}
	@Override public <T> T as() {
		return (T) this;
	}

	@Override public boolean isAdded() {
		return false;
	}
	@Override public boolean isLocal() {
		return false;
	}
	@Override public boolean isRemote() {
		return false;
	}

	@Override public boolean serialize() {
		return false;
	}

	@Override public void afterRead() {

	}
	@Override public void afterReadAll() {

	}
	@Override public void beforeWrite() {

	}
	@Override public void read(Reads reads) {

	}
	@Override public void write(Writes writes) {

	}
}
