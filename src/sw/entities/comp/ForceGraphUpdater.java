package sw.entities.comp;

import arc.util.io.*;
import mindustry.*;
import mindustry.entities.*;
import mindustry.gen.*;
import sw.entities.*;
import sw.world.graph.*;

public class ForceGraphUpdater implements Entityc {
	public transient boolean added;
	public transient int id = EntityGroup.nextId();
	public transient ForceGraph graph;
	public transient int id_all;

	@Override public <T extends Entityc> T self() {
		return (T)this;
	}
	@Override public <T> T as() {
		return (T)this;
	}

	@Override public boolean isAdded() {
		return added;
	}
	@Override public boolean isLocal() {
		return true;
	}
	@Override public boolean isNull() {
		return false;
	}
	@Override public boolean isRemote() {
		return false;
	}
	@Override public boolean serialize() {
		return false;
	}

	@Override
	public void add() {
		if (!added) {
			id_all = Groups.all.addIndex(this);
			added = true;
		}
	}
	@Override
	public void remove() {
		if (added) {
			Groups.all.removeIndex(this, id_all);
			id_all = -1;
			added = false;
		}
	}

	@Override public void afterRead() {}
	@Override public void read(Reads read) {
		graph.rotation = read.f() % 100;
		short s = read.s();
		for (int i = 0; i < s; i++) graph.links.addUnique(new Link(Vars.world.build(read.i()), Vars.world.build(read.i())).addS());
//		links.remove(link -> link.l1 == null || link.l2 == null);
	}
	@Override public void write(Writes write) {
		write.f(graph.rotation);
		write.s(graph.links.size);
		for (Link link : graph.links) {
			write.i(link.l1.pos());
			write.i(link.l2.pos());
		}
	}

	@Override
	public void update() {
		if (graph != null) {
			graph.update();
		}
	}

	@Override public int id() {
		return id;
	}
	@Override public void id(int i) {
		id = i;
	}
	@Override public int classId() {
		return SWEntityMapping.idMap.get(ForceGraphUpdater.class);
	}

	@Override
	public String toString() {
		return "ForceGraphUpdater#"+id;
	}
}
