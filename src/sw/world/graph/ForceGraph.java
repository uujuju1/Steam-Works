package sw.world.graph;

import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.gen.*;
import sw.entities.comp.*;
import sw.world.interfaces.*;
import sw.world.modules.*;

/**
 * TODO visual speed setting
 */
public class ForceGraph {
	public final Seq<Building> builds = new Seq<>(false, 16, Building.class);
	public final Seq<ForceModule> modules = new Seq<>(false, 16, ForceModule.class);
	public final Seq<Link> links = new Seq<>(false, 16, Link.class);

	public final @Nullable ForceGraphUpdater entity;

	public float rotation = 0;

	private WindowedMean mean;

	public ForceGraph() {
		entity = new ForceGraphUpdater();
		entity.graph = this;
		hasEntity();
	}

	public void hasEntity() {
		if (entity != null) entity.add();
	}

	public Seq<Building> floodFill(Building build) {
		Seq<Building> out = new Seq<>();
		Seq<Building> next = Seq.with(build);
		while (!next.isEmpty()) {
			Building b = next.pop();
			out.addUnique(b);
			if (b instanceof HasForce add) {
				out.addUnique(b);
				add.force().links.each(i -> {
					out.addUnique((Building) i.l1());
					out.addUnique((Building) i.l2());
				});
			}
		}
		return out;
	}

	public void add(Building build) {
		if (build instanceof HasForce add) {
			add.force().graph.removeBuild(build);
			add.force().graph = this;
			builds.addUnique(build);
			modules.addUnique(add.force());
		}
	}
	public void addGraph(ForceGraph graph) {
		if (graph == this) return;
		if (graph.builds.size > builds.size) {
			graph.addGraph(this);
			return;
		}

		graph.entity.remove();

		for (Building build : graph.builds) add(build);
		for (Link link : graph.links) links.addUnique(link);
		hasEntity();
	}
	public void removeBuild(Building build) {
		if (builds.remove(build)) modules.remove(((HasForce) build).force());
	}
	public void remove(Building build) {
		ForceGraph graph = new ForceGraph();
		graph.hasEntity();
		graph.add(build);
		graph.entity.update();
	}

	public void update() {
		rotation += Mathf.maxZero(Math.abs(getSpeed()) - getResistance()) * Time.delta * (getSpeed() >= 0 ? 1 : -1);
//		for (Link link : links) {
//			if (
//				link.l1() != null && link.l2() != null &&
//					builds.contains((Building) link.l1()) && builds.contains((Building) link.l2())
//			) {
//				float spd = (link.l1().force().speed + link.l2().force().speed)/2f;
//				link.l1().force().speed = spd;
//				link.l2().force().speed = spd;
//			}
//		}
		if (mean == null || mean.getWindowSize() != builds.size) mean = new WindowedMean(builds.size);
		for (Building b : builds) {
			HasForce build = (HasForce) b;
			mean.add(build.force().speed);
			if (mean.hasEnoughData()) build.force().speed = mean.mean();
			build.force().speed = Math.min(Math.abs(build.force().speed), build.forceConfig().maxForce) * (build.speed() > 0 ? 1 : -1);
//			build.force().speed = Mathf.approachDelta(build.force().speed, 0, build.forceConfig().baseResistance);
		}
	}

	public float getResistance() {
		float resistance = 0.0001f;
		if (modules.isEmpty()) return resistance;
    for (HasForce b : builds.map(b -> (HasForce) b)) if (b != null) resistance += b.forceConfig().baseResistance;
    return resistance/modules.size;
	}
	public float getSpeed() {
		float speed = 0.0001f;
		if (modules.isEmpty()) return speed;
		for (ForceModule b : modules) speed += b.speed;
		return speed/modules.size;
	}
}
