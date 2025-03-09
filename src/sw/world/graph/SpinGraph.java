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
	 * Used when updating inertia.
	 */
	public float lastSpeed;

	/**
	 * Entity of this graph.
	 */
	public final SpinGraphUpdater updater = SpinGraphUpdater.create().setGraph(this);

	/**
	 * List of buildings of this graph.
	 */
	public final Seq<HasSpin> builds = new Seq<>();
	public final Seq<HasSpin> producers = new Seq<>();

	/**
	 * Temporary seqs for use in flood.
	 */
	public static final Seq<HasSpin> tmp = new Seq<>(), tmp2 = new Seq<>();

	/**
	 * Adds a build to this graph, and removes the build from its older graph.
	 */
	public void addBuild(HasSpin build) {
		builds.add(build);
		if (build.spinConfig().outputsSpin) producers.add(build);
		checkEntity();
		build.spinGraph().remove(build, false);
		build.spin().graph = this;
		changed = true;
	}

	/**
	 * Disables the updater if there are less than a single building.
	 */
	public void checkEntity() {
		if (builds.size > 0) {
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
		producers.remove(build);
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
		if (force() < resistance()) return 0;
		if (force() == resistance()) return speed;
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
			updateInertia();
			changed = false;
		}

		// use delta?
//		speed += (force() - resistance()) * Time.delta;
//		speed = Mathf.clamp(speed, 0, targetSpeed());

		speed = Mathf.approachDelta(speed, targetSpeed(), Math.abs(force() - resistance()));

		if (lastSpeed != speed) {
			lastSpeed = speed;
			builds.each(b -> b.spin().inertia = speed);
		}

		rotation += speed * Time.delta;
	}

	public void updateInertia() {
		speed = builds.sumf(b -> b.spin().inertia)/builds.size;
	}

	public void updateRatios() {
		/*
		let b = Vars.world.build(59, 59)
Draw.z(Layer.blockOver)

let transmission = SWPower.shaftTransmission
let highHalf = transmission.scaledEdges
let lowHalf = [0, 3]
let mul = transmission.multiplier

let tmp = Seq.with(b)
let builds = Seq.with(b)
let layerBuilds = new ObjectFloatMap()
let froms = ObjectMap.of(b, b)
layerBuilds.put(b, 1)

let last

while (!tmp.isEmpty()) {
  let build = tmp.pop()

  let edges = Edges.getEdges(build.block.size)

  build.nextBuilds().each(other => {
    let edgesOther = Edges.getEdges(build.block.size)
    if (!builds.contains(other)) {
      tmp.add(other)
      builds.add(other)
      froms.put(other, build)
      last = froms.get(build)

      if (build.block == transmission) {
        let isHigh = false
        for (let i = 0; i < highHalf.length; i++) {
          if (build.nearby(
            edges[build.spinConfig().allowedEdges[build.rotation][highHalf[i]]].x,
            edges[build.spinConfig().allowedEdges[build.rotation][highHalf[i]]].y
          ) == other) isHigh = true
        }
        let isLow = false
        for (let i = 0; i < lowHalf.length; i++) {
          if (build.nearby(
            edges[build.spinConfig().allowedEdges[build.rotation][lowHalf[i]]].x,
            edges[build.spinConfig().allowedEdges[build.rotation][lowHalf[i]]].y
          ) == other) isLow = true
        }
        let sameLast = false
        for (let i = 0; i < highHalf.length; i++) {
          if (build.nearby(
            edges[build.spinConfig().allowedEdges[build.rotation][highHalf[i]]].x,
            edges[build.spinConfig().allowedEdges[build.rotation][highHalf[i]]].y
          ) == last) sameLast = true
        }
        let h = layerBuilds.get(build, 1) * (isHigh ? mul : 1/mul)
        if (isHigh ^ sameLast && !(isHigh && isLow) && !(layerBuilds.get(build, 1) == 1 && layerBuilds.get(last, 1) != 1)) {
          layerBuilds.put(other, h)
        } else {
          layerBuilds.put(other, layerBuilds.get(!(isHigh && isLow) && !(layerBuilds.get(build, 1) == 1 && layerBuilds.get(last, 1) != 1) ? last : build, 1))
          if ((isHigh && isLow) && (other.block != transmission || other.rotation != build.rotation)) {
            Fill.square(other.x, other.y, 2)
          }
        }
      } else layerBuilds.put(other, layerBuilds.get(build, 1))
    } else {
      last = froms.get(build)
      let h = 1;
      if (build.block == transmission) {
        let isHigh = false
        for (let i = 0; i < highHalf.length; i++) {
          if (build.nearby(
            edges[build.spinConfig().allowedEdges[build.rotation][highHalf[i]]].x,
            edges[build.spinConfig().allowedEdges[build.rotation][highHalf[i]]].y
          ) == other) isHigh = true
        }
        let isLow = false
        for (let i = 0; i < lowHalf.length; i++) {
          if (build.nearby(
            edges[build.spinConfig().allowedEdges[build.rotation][lowHalf[i]]].x,
            edges[build.spinConfig().allowedEdges[build.rotation][lowHalf[i]]].y
          ) == other) isLow = true
        }
        let sameLast = false
        for (let i = 0; i < highHalf.length; i++) {
          if (build.nearby(
            edges[build.spinConfig().allowedEdges[build.rotation][highHalf[i]]].x,
            edges[build.spinConfig().allowedEdges[build.rotation][highHalf[i]]].y
          ) == last) sameLast = true
        }

        if (isHigh ^ sameLast && !(isHigh && isLow) && !(layerBuilds.get(last, 1) == mul && h == 1/mul)) {
          h = layerBuilds.get(build, 1) * (isHigh ? mul : 1/mul)
        } else {
          h = layerBuilds.get(!(isHigh && isLow) && !(layerBuilds.get(last, 1) == mul && h == 1/mul) ? last : build, 1)
        }
      } else h = layerBuilds.get(build, 1)

      if (
        other != last &&
        (
          other.block == transmission ?
          (layerBuilds.get(other, 1) * mul < h || h < layerBuilds.get(other, 1) / mul) :
          (h != layerBuilds.get(other, 1))
        )
      ) {
        Fill.square(other.x, other.y, 2)
        Lines.line(other.x, other.y, build.x, build.y)
      }

    }
  })

  last = build
}

builds.each(build => {
  build.block.drawPlaceText(
    "" + (layerBuilds.get(build, 0)),
    build.tileX(), build.tileY(),
    true
  )
})
		*/
	}
}
