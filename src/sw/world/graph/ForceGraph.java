package sw.world.graph;

import arc.struct.*;
import mindustry.*;
import sw.world.interfaces.*;

/**
 * TODO visual speed setting
 */
public class ForceGraph extends Graph {
	public final Seq<HasForce> builds = new Seq<>(false, 16, HasForce.class);
	public final Seq<ForceLink> links = new Seq<>(false, 16, ForceLink.class);

	public float rotation = 0;

	@Override
	public void update() {
		float speed = Math.max(0, Math.abs(getSpeed()/builds.size) - getFriction()) * (getSpeed() > 0f ? 1f : -1f);
		builds.each(b -> {
			b.force().speed = speed;
		});

		rotation += getSpeed();
	}

	public void add(HasForce build) {
		builds.addUnique(build);
		build.force().graph = this;
		addGraph();
	}
	public void remove(HasForce build) {
		softRemove(build);
		build.force().graph = new ForceGraph();
		build.graph().add(build);
	}
	public void softRemove(HasForce build) {
		builds.remove(build);
		if (builds.isEmpty()) removeGraph();
	}
	public void merge(ForceGraph graph) {
		if (graph.builds.size > builds.size) {
			graph.merge(this);
			return;
		}
		graph.removeGraph();
		graph.builds.each(b -> {
			graph.softRemove(b);
			add(b);
		});
		graph.links.each(links::addUnique);
	}
	public void updateGraph() {
		Seq<HasForce> build = builds.copy();
		Seq<ForceLink> link = links.copy();

		build.each(this::remove);
		links.each(l -> {
			if (l.l1() != null && l.l2() != null) {
				l.l1().graph().merge(l.l2().graph());
				l.l1().graph().links.addUnique(l);
			}
		});
	}

	public Seq<HasForce> flood(HasForce start) {
		Seq<HasForce> out = Seq.with(start);
		Seq<HasForce> temp = Seq.with(start);

		while (!temp.isEmpty()) {
			HasForce init = temp.pop();
			init.force().links.each(c -> {
				if (c.other(init) != null && !out.contains(c.other(init))) {
					out.add(c.other(init));
					temp.add(c.other(init));
				}
			});
		}

		return out;
	}

	public float getSpeed() {
		return builds.sumf(b -> b.force().speed);
	}
	public float getFriction() {
		return builds.sumf(b -> b.forceConfig().friction);
	}

	public static class ForceLink {
		public int l1, l2;

		public ForceLink(HasForce l1, HasForce l2) {
			this(l1.pos(), l2.pos());
		}
		public ForceLink(int l1, int l2) {
			this.l1 = l1;
			this.l2 = l2;
		}

		public HasForce l1() {
			return (HasForce) Vars.world.build(l1);
		}
		public HasForce l2() {
			return (HasForce) Vars.world.build(l2);
		}

		public HasForce other(HasForce build) {
			return build == l1() ? l2() : l1();
		}
		public boolean has(HasForce build) {
			return build == l1() || build == l2();
		}

		@Override public boolean equals(Object obj) {
			return obj instanceof sw.world.graph.ForceGraph.ForceLink forceLink &&
				       (forceLink.l1 == l1 || forceLink.l1 == l2) &&
				       (forceLink.l2 == l2 || forceLink.l2 == l1);
		}
		@Override public String toString() {
			return "link1: " + l1 + "; link2: " + l2;
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