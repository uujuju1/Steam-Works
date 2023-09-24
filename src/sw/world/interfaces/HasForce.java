package sw.world.interfaces;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import sw.util.*;
import sw.world.graph.*;
import sw.world.graph.ForceGraph.*;
import sw.world.meta.*;
import sw.world.modules.*;

public interface HasForce extends Buildingc, Posc{
	ForceModule force();
	ForceConfig forceConfig();
	default @Nullable HasForce getLink() {
		return (HasForce) Vars.world.build(force().link);
	}
	default ForceGraph graph() {
		return force().graph;
	}

	default float beltSize() {
		return forceConfig().beltSize;
	}

	default float spin() {
		return graph().rotation;
	}
	default float speed() {
		return force().speed * ratioScl();
	}
	default float ratioScl() {
		switch (getRatio()) {
			case equal : return 1f;
			case normal : return 2f;
			case extreme : return 3f;
		}
		return 1f;
	}
	default float ratioNumber() {
		if (force().links.copy().removeAll(l -> l.l1() == this).isEmpty()) return 1f;
		return beltSize() / force().links.copy().removeAll(
			l -> l.l1() == this || l.l1() == null
		).max(l -> l.l1().beltSize()).l1().beltSize();
	}
	default ForceRatio getRatio() {
		float ratio = ratioNumber();
		if (Math.abs(ratio - 1/ratio) > 1.5) {
			return ForceRatio.extreme;
		} else if (Math.abs(ratio - 1/ratio) > 1) {
			return ForceRatio.normal;
		} else return ForceRatio.equal;
	}

	default void drawBelt() {
		if (getLink() != null) {
			Draw.z(Layer.block - 1f);
			for (int i : Mathf.signs) {
				Vec2 p1 = new Vec2(x(), y()), p2 = new Vec2(getLink().x(), getLink().y());
				int serrations = Mathf.ceil(p1.dst(p2) / 8f);
				float time = serrations * 20f;
				float rot = spin() * i;
				rot += rot > 0 ? time : -time;

				float angle = Tmp.v1.set(p1).sub(p2).angle();
				p1.add(Tmp.v1.trns(angle + 90 + (i > 0 ? 0 : 180), beltSize()));
				p2.add(Tmp.v1.trns(angle + 90 + (i > 0 ? 0 : 180), ((HasForce) getLink()).beltSize()));

				SWDraw.beltLine(Color.valueOf("A6918A"), Color.valueOf("6B5A55"), Color.valueOf("BEADA7"), p1.x, p1.y, p2.x, p2.y, rot);
			}
			Draw.reset();
		}
	}

	default void forceLink(HasForce b) {
		force().link = b.pos();

		graph().merge(b.graph());
		graph().links.addUnique(new ForceLink(this, b));
		force().links.addUnique(new ForceLink(this, b));
		b.force().links.addUnique(new ForceLink(this, b));
		graph().updateGraph();
	}
	default void forceUnLink() {
		if (getLink() != null) {
			graph().links.remove(new ForceLink(this, getLink()));
			force().links.remove(new ForceLink(this, getLink()));
			getLink().force().links.remove(new ForceLink(this, getLink()));
			force().link = -1;
			graph().updateGraph();
		}
	}

	default boolean configureForceLink(Building other) {
		if (other instanceof HasForce next && tile().dst(other) < forceConfig().range && next.forceConfig().acceptsForce && forceConfig().outputsForce) {
			if ((getLink() != null && getLink() == other) || other == this) {
				forceUnLink();
				return false;
			}

			if (next.getLink() == this) next.forceUnLink();
			if (getLink() != null) forceUnLink();

			forceLink(next);
			graph().rotation = 0;
			return false;
		}
		return true;
	}
}
