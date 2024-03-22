package sw.world.graph;

import arc.struct.*;
import sw.world.interfaces.*;

public class TensionGraph {
	public final Seq<HasTension> builds = new Seq<>();

	/**
	 * return tension fields
	 */
	public float getPull() {
		return builds.sumf(HasTension::tensionMobile);
	}
	public float getStaticTension() {
		return builds.sumf(HasTension::tensionStatic);
	}
	public float getOverallTension() {
		return builds.sumf(b -> b.tensionStatic() + b.tensionMobile());
	}

	/**
	 * adds a build and it's links into this graph
	 */
	public void addBuild(HasTension build) {
		if (builds.contains(build)) return;
		builds.addUnique(build);
		build.tensionGraph().removeBuild(build, false);
		build.tension().graph = this;
		build.nextBuilds().each(this::addBuild);
	}
	/**
	 * removes a build from this graph
	 * @param propagate creates another graph for the removed build, if false, the build "stays" linked to this graph
	 */
	public void removeBuild(HasTension build, boolean propagate) {
		if (propagate) {
			builds.remove(build);
			new TensionGraph().addBuild(build);
		} else {
			builds.remove(build);
		}
	}
}
