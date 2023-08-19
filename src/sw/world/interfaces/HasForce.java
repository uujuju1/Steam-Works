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
	default @Nullable Building getLink() {
		return Vars.world.build(force().link);
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
	default float torque() {return force().speed / ratioScl();}
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
				Vec2 p1 = new Vec2(x(), y()), p2 = new Vec2(getLink().x, getLink().y);
				int serrations = Mathf.ceil(p1.dst(p2) / 8f);
				float time = serrations * 20f;
				float rot = spin() * i;
				rot += rot > 0 ? time : -time;

				float angle = Tmp.v1.set(p1).sub(p2).angle();
				p1.add(Tmp.v1.trns(angle + 90 + (i > 0 ? 0 : 180), beltSize()));
				p2.add(Tmp.v1.trns(angle + 90 + (i > 0 ? 0 : 180), ((HasForce) getLink()).beltSize()));
//				angle = Tmp.v1.set(p1).sub(p2).angle();

				SWDraw.beltLine(Color.valueOf("A6918A"), Color.valueOf("6B5A55"), Color.valueOf("BEADA7"), p1.x, p1.y, p2.x, p2.y, rot);
//				SWDraw.linePoint(Color.valueOf("A6918A"), Color.valueOf("6B5A55"), p1.x, p1.y, p2.x, p2.y);
//				Draw.color(Color.valueOf("BEADA7"));
//
//				for (int j = 0; j < serrations; j++) {
//					float progress = ((rot + time/serrations * j) %time / time) + (rot > 0 ? 0f : 1f);
//
//					Tmp.v1.set(p1).lerp(p2, progress);
//					Fill.rect(Tmp.v1.x, Tmp.v1.y, 2, 1, angle);
//				}
			}
			Draw.reset();
		}
	}

	default void link(Building build) {
		HasForce b = (HasForce) build;
		force().link = build.pos();
		ForceLink forceLink = new ForceLink(this, b);

		graph().addGraph(b.graph());
		graph().links.addUnique(forceLink);

		force().links.addUnique(forceLink);
		b.force().links.addUnique(forceLink);
		b.graph().addGraph();
	}
	default void unLink() {
		if (getLink() instanceof HasForce b) {
			ForceLink forceLink = new ForceLink(this, b);
			force().link = -1;

			force().links.remove(forceLink);
			b.force().links.remove(forceLink);
			graph().links.remove(forceLink);
			unLinkGraph();
			graph().floodFill((Building) this).each(build -> graph().add(build));
		}
	}
	default void unLinkGraph() {
		graph().remove((Building) this);
		graph().removeBuild((Building) this);
	}

	default boolean configureBuildTap(Building other) {
		if (other instanceof HasForce next && tile().dst(other) < forceConfig().range && next.forceConfig().acceptsForce) {
			if ((getLink() != null && getLink() == other) || other == this) {
				unLink();
				return false;
			}

			if (next.getLink() == this) next.unLink();
			if (getLink() != null) unLink();

			link(other);
			graph().rotation = 0;
			return false;
		}
		return true;
	}
}
