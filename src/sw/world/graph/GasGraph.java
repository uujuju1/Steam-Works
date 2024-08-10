package sw.world.graph;

import arc.struct.*;
import sw.gen.*;
import sw.world.interfaces.*;

/**
 * Graph containing an isolated group of gas buildings.
 */
public class GasGraph {
	/**
	 * Entity associated with this graph.
	 */
	public final GasGraphUpdater updater = GasGraphUpdater.create().setGraph(this);

	/**
	 * List of buildings associated with this graph.
	 */
	public final Seq<HasGas> builds = new Seq<>();

	public static float delta = 1;
	public static int runs = 1;

	/**
	 * Adds a build to this graph, and removes the build from its older graph.
	 */
	public void addBuild(HasGas build) {
		builds.add(build);
		updater.add();
		checkEntity();
		build.gasGraph().remove(build, false);
		build.gas().graph = this;
	}

	/**
	 * Disables the updater if there are less than 2 gas buildings.
	 */
	public void checkEntity() {
		if (builds.size > 1) {
			updater.add();
		} else {
			updater.remove();
		}
	}

	/**
	 * Merges this graph with another one. if other graph is bigger, merge the other graph with this one.
	 * @param priority If true, the graph will always merge with the other graph.
	 */
	public void merge(GasGraph other, boolean priority) {
		if (other.builds.size > builds.size && !priority) {
			other.merge(this, false);
		} else {
			other.builds.each(this::addBuild);
		}
		builds.each(HasGas::onGasGraphUpdate);
	}

	/**
	 * Removes a building from this graph.
	 * @param split if true, makes each connection a separate graph.
	 */
	public void remove(HasGas build, boolean split) {
		builds.remove(build);
		if (split) {
			build.nextBuilds().each(p -> {
				p.gasGraph().merge(new GasGraph(), true);
			});
		}
		checkEntity();
	}

	/**
	 * Called every frame by the updater.
	 */
	public void update() {
		for (int i = 0; i < runs; i++) {
			builds.each(build -> {
				var others = build.nextBuilds().retainAll(b -> b.acceptsGas(build, 0) && build.outputsGas(b, 0));
				others.each(other -> {
					float transferAmount = (build.getGas() - other.getGas())/2f;
					other.handleGas(build, transferAmount, false);
				});
			});
		}
	}
}
