package sw.world.graph;

import arc.func.*;
import arc.struct.*;
import mindustry.*;
import mindustry.gen.*;
import sw.util.*;
import sw.world.interfaces.*;

public class HeatGraph extends Graph {
	public Seq<HasHeat> builds = new Seq<>(false, 16, HasHeat.class);
	public Seq<HeatLink> links = new Seq<>(false, 16, HeatLink.class);

	public HeatGraph() {
		super();
		checkEntity();
	}

	@Override
	public void update() {
		links.each(l -> {
			if (l.valid()) {
				HasHeat from = l.get((l1, l2) -> l2.temperature() > l1.temperature());
				HasHeat to = l.other(from);
				float amount = SWMath.heatTransferDelta(from.heatC().heatEmissivity, from.temperature(), to.temperature(), true);
				if (from.outputsHeat(to, amount) && to.acceptsHeat(from, amount)) from.transferHeat(to, amount);
			}
		});
		builds.each(b -> {
			if (b.overflows()) {
				builds.remove(b);
				b.asBuild().kill();
			}
			if (b.temperature() < b.heatC().minHeat) b.setHeat(b.heatC().minHeat);
			b.addHeat(-b.temperature() * b.heatC().heatLoss);
		});
	}

	public void reloadConnections() {
		for (HasHeat b: builds) b.heatProximity().each(b2 -> {
			links.addUnique(new HeatLink(b, (HasHeat) b2));
			builds.addUnique((HasHeat) b2);
		});
	}

	public void merge(HeatGraph graph) {
		if (graph == this) return;
		if (graph.builds.size > builds.size) {
			graph.merge(this);
			return;
		}

		graph.entity.remove();

		for (HasHeat build : graph.builds) builds.addUnique(build);
		for (HeatLink link : graph.links) links.addUnique(link);
	}

	public void add(HasHeat build) {
		build.heat().graph = this;
		builds.addUnique(build);
	}

	public static class HeatLink {
		public int l1, l2;

		public HeatLink(HasHeat l1, HasHeat l2) {
			this(((Building) l1).pos(), ((Building) l2).pos());
		}
		public HeatLink(int l1, int l2) {
			this.l1 = l1;
			this.l2 = l2;
		}

		public HasHeat l1() {
			return (HasHeat) Vars.world.build(l1);
		}
		public HasHeat l2() {
			return (HasHeat) Vars.world.build(l2);
		}

		public HasHeat other(HasHeat build) {
			return build == l1() ? l2() : l1();
		}
		public boolean has(HasHeat build) {
			return build == l1() || build == l2();
		}
		public boolean valid() {
			return l1() != l2() && l1() != null && l2() != null;
		}
		public HasHeat get(Boolf2<HasHeat, HasHeat> boolf) {
			return boolf.get(l1(), l2()) ? l2() : l1();
		}

		@Override public boolean equals(Object obj) {
			return obj instanceof HeatLink link && (link.l1 == l1 || link.l1 == l2) && (link.l2 == l2 || link.l2 == l1);
		}
		@Override public String toString() {
			return "link1: " + l1 + "; link2: " + l2;
		}

	}
}
