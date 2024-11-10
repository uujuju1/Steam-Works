package sw.world.graph;

import arc.math.*;
import arc.struct.*;
import arc.util.*;
import sw.gen.*;
import sw.world.interfaces.*;

/**
 * Graph containing an isolated group of uildings sharing common stuff.
 */
public class SpinGraph {
	public float rotation;
	public float speed;

	/**
	 * Wether the graph has changed in any way like adding/removing builds.
	 */
	public boolean changed;

	/**
	 * Entity of this graph.
	 */
	public final SpinGraphUpdater updater = SpinGraphUpdater.create().setGraph(this);

	/**
	 * List of buildings of this graph.
	 */
	public final Seq<HasSpin> builds = new Seq<>();

	/**
	 * Temporary seqs for use in flood.
	 */
	public static final Seq<HasSpin> tmp = new Seq<>(), tmp2 = new Seq<>();

	/**
	 * Adds a build to this graph, and removes the build from its older graph.
	 */
	public void addBuild(HasSpin build) {
		builds.add(build);
		checkEntity();
		build.spinGraph().remove(build, false);
		build.spin().graph = this;
		changed = true;
	}

	/**
	 * Disables the updater if there are less than 2 buildings.
	 */
	public void checkEntity() {
		if (builds.size > 1) {
			updater.add();
		} else {
			updater.remove();
		}
	}

	/**
	 * Returns the force that all builds are doing to push the whole system.
	 */
	public float force() {
		return builds.sumf(HasSpin::getForce);
	}

	/**
	 * Merges this graph with another one. if other graph is bigger, merge the other graph with this one.
	 * @param priority If true, the graph will always merge with the other graph.
	 */
	public void merge(SpinGraph other, boolean priority) {
		if (other.builds.size > builds.size && !priority) {
			other.merge(this, false);
		} else {
			other.builds.each(this::addBuild);
		}
		builds.each(HasSpin::onGraphUpdate);
	}
	public void mergeFlood(HasSpin other) {
		tmp.clear().add(other);
		tmp2.clear();

		while(!tmp.isEmpty()) {
			HasSpin next = tmp.pop();
			tmp2.addUnique(next);
			next.nextBuilds().each(b -> {
				if (!tmp2.contains(b)) tmp.add(b);
			});
		}

		tmp2.each(this::addBuild);
	}

	/**
	 * Removes a building from this graph.
	 * @param split if true, makes each connection a separate graph.
	 */
	public void remove(HasSpin build, boolean split) {
		builds.remove(build);
		if (split) build.nextBuilds().each(p -> {
			new SpinGraph().mergeFlood(p);
		});
		checkEntity();
		changed = true;
	}

	/**
	 * Returns the resistance of the whole system from moving.
	 */
	public float resistance() {
		return builds.sumf(HasSpin::getResistance);
	}

	/**
	 * Returns the speed that the system is trying to reach.
	 */
	public float targetSpeed() {
		return builds.max(HasSpin::getTargetSpeed).getTargetSpeed();
	}

	/**
	 * Called every frame by the updater.
	 */
	public void update() {
		if (changed) {
			builds.each(b -> {
				b.spin().section = new SpinSection();
				b.spinSection().addBuild(b);
			});
			builds.each(HasSpin::onGraphUpdate);
			changed = false;
		}

		// use delta?
		speed += force() - resistance();
		speed = Mathf.clamp(speed, 0, targetSpeed());

		rotation += speed * Time.delta;
	}
}
