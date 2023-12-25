package sw.world.graph;

import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import sw.gen.*;
import sw.world.interfaces.*;

public class ForceGraph {
	public final @Nullable ForceGraphEntity entity;
	private final int id;

	public final Seq<HasForce> builds = new Seq<>();
	public final Seq<ForceLink> links = new Seq<>();

	public float rotation;

	public ForceGraph() {
		this(ForceGraphEntity.create());
	}
	public ForceGraph(ForceGraphEntity entity) {
		this.entity = entity;
		entity.graph(this);
		entity.add();
		id = entity.id();
	}

/**
 * adds a build with flood
 */
	public void addBuild(HasForce build) {
		if (!builds.contains(build)) {
			build.fGraph().removeBuild(build, false);
			build.force().graph = this;
			builds.addUnique(build);
			for (ForceLink forceLink : build.force().links) {
				addLink(forceLink);
			}
		}
	}
/**
 * removes the build from this graph and adds it (or not) to another graph
 */
	public void removeBuild(HasForce build) {
		removeBuild(build, true);
	}
	public void removeBuild(HasForce build, boolean replace) {
		builds.remove(build);
		if (replace) new ForceGraph().addBuild(build);
		if (builds.isEmpty()) remove();
	}
/**
 * adds a link and it's builds into the graph
 */
	public void addLink(ForceLink link) {
		if (!link.valid()) return;
		links.addUnique(link);
		if (link.startBuild() != null) addBuild(link.startBuild());
		if (link.endBuild() != null) addBuild(link.endBuild());
	}
/**
 * removes a link and it's builds from the graph
 */
	public void removeLink(ForceLink link) {
		links.remove(link);
		removeBuild(link.startBuild(), link.startBuild() != null);
		removeBuild(link.endBuild(), link.endBuild() != null);
	}

/**
 * gets rotation parameters from the builds
 */
	public float getSpeed() {
		return builds.sumf(b -> b.force().speed);
	}
	public float getFriction() {
		return builds.sumf(b -> b.fConfig().friction);
	}

/**
 * method that constantly updates the graph and it's funcions
 */
	public void update() {
		float speed = Math.max(0, Math.abs(getSpeed()/builds.size) - getFriction()) * (getSpeed() > 0f ? 1f : -1f);
		builds.each(b -> {
			b.force().speed = speed;
		});

		rotation += getSpeed();
	}

/**
 * add or remove the graph (entity related)
 */
	public void add() {
		entity.add();
	}
	public void remove() {
		entity.remove();
	}

/**
 * io
 */
	public void read(Reads read) {
		short size = read.s();
		for (int i = 0; i < size; i++) {
			ForceLink forceLink = new ForceLink(-1, -1);
			forceLink.read(read);
			links.add(forceLink);
		}
	}
	public void afterRead() {
		links.each(this::addLink);
	}
	public void write(Writes write) {
		write.s(links.size);
		for (ForceLink forceLink : links) {
			forceLink.write(write);
		}
	}

	public static class ForceLink {
		public int start, end;

		public ForceLink(HasForce start, HasForce end) {
			this.start = start.pos();
			this.end = end.pos();
		}
		public ForceLink(int start, int end) {
			this.start = start;
			this.end = end;
		}

/**
 * cast links as buildings
 */
		public HasForce startBuild() {
			return Vars.world.build(start) instanceof HasForce hasForce ? hasForce : null;
		}
		public HasForce endBuild() {
			return Vars.world.build(end) instanceof HasForce hasForce ? hasForce : null;
		}

/**
 * returns true if both builds are something
 */
		public boolean valid() {
			return startBuild() != null && endBuild() != null;
		}
/**
 * ratio between 2 links
 */
		public float ratio(boolean reversed) {
			if (!valid()) return 1;
			return reversed ? (startBuild().beltSize()/endBuild().beltSize()) : (endBuild().beltSize()/startBuild().beltSize());
		}

		@Override public boolean equals(Object obj) {
			return obj instanceof ForceLink forceLink &&
				       (forceLink.start == start || forceLink.start == end) &&
				       (forceLink.end == end || forceLink.end == start);
		}
		@Override public String toString() {
			return "(link1: " + Point2.unpack(start) + "; link2: " + Point2.unpack(end) + ")";
		}

/**
 * link related io
 */
		public void write(Writes write) {
			write.i(start);
			write.i(end);
		}
		public void read(Reads read) {
			start = read.i();
			end = read.i();
		}
	}
	public enum ForceRatio {
		extreme("ratio.extreme"),
		normal("ratio.normal"),
		equal("ratio.equal");

		final String name;
		ForceRatio(String name) {
			this.name = name;
		}
	}
}
