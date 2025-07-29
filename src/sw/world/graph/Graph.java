package sw.world.graph;

import arc.func.*;
import arc.struct.*;
import sw.entities.units.*;

public abstract class Graph<T> {
	public final Seq<T> builds = new Seq<>(false);
	
	public GraphUpdater updater = new GraphUpdater().setGraph(this);
	
	public boolean changed = true;
	
	/**
	 * Adds a build for this graph, no questions asked.
	 */
	public void addBuild(T build) {
		builds.addUnique(build);
		checkEntity();
		changed = true;
	}
	
	/**
	 * Checks if the graph is valid, if it's not, it removes the entity.
	 */
	public void checkEntity() {
		if (validGraph()) {
			updater.add();
		} else {
			updater.remove();
		}
	}
	
	/**
	 * Called whenever the graph is changed and is still valid.
	 */
	public void graphChanged() {
	}
	
	/**
	 * Adds new builds from a starting point and a finder function, is only finished when
	 */
	public Seq<T> floodFill(T start, Func<T, Seq<T>> finder) {
		Seq<T>
			tmp = Seq.with(start),
			tmp2 = Seq.with();
		
		while (!tmp.isEmpty()) {
			T current = tmp.pop();
			tmp2.addUnique(current);
			
			finder.get(current).each(build -> !tmp2.contains(build), tmp::add);
		}
		
		return tmp2;
	}
	
	/**
	 * Removes a build from this graph, no questions asked.
	 */
	public void removeBuild(T build) {
		builds.remove(build);
		changed = true;
	}
	
	public void update() {
		if (changed) {
			checkEntity();
			changed = false;
			if (updater.isAdded()) graphChanged();
		}
	}
	
	/**
	 * Returns true whenever the graph is valid. When {@link #checkEntity} is called. this method must return true for the updater to not remove itself.
	 */
	public boolean validGraph() {
		return builds.size > 0;
	}
}
