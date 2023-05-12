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
import sw.world.meta.*;
import sw.world.modules.*;

public interface HasForce {
	ForceModule force();
	ForceConfig forceConfig();
	default @Nullable Building getLink() {
		return Vars.world.build(force().link);
	}
	default ForceGraph graph() {
		return force().graph;
	}

	default float spin() {
		return graph().rotation;
	}
	default float speed() {
		return force().speed * force().torque;
	}
	default float torque() {return force().speed / force().torque;}
	default void ratio() {
		float mean = 0;
		if (force().links.copy().removeAll(l -> l.l1() == this).isEmpty()) {
			force().torque = 1f;
			return;
		}
		for(Link link : force().links.copy().removeAll(l -> l.l1() == this)) {
			if (link.l1() == null || link.l2() == null) continue;
			mean += link.ratio(this, true);
		}
		force().torque = mean / force().links.copy().removeAll(l -> l.l1() == this).size;
	}

	default void drawBelt() {
		Building b = (Building) this;
		if (getLink() != null) {
			Draw.z(Layer.block - 1f);
			for (int i : Mathf.signs) {
				Vec2 p1 = new Vec2(b.x, b.y), p2 = new Vec2(getLink().x, getLink().y);
				int serrations = Mathf.ceil(p1.dst(p2) / 8f);
				float time = serrations * 20f;
				float rot = spin() * i;
				rot += rot > 0 ? time : -time;

				float angle = Tmp.v1.set(p1).sub(p2).angle();
				p1.add(Tmp.v1.trns(angle + 90 + (i > 0 ? 0 : 180), forceConfig().beltSizeOut));
				p2.add(Tmp.v1.trns(angle + 90 + (i > 0 ? 0 : 180), ((HasForce) getLink()).forceConfig().beltSizeIn));
				angle = Tmp.v1.set(p1).sub(p2).angle();

				SWDraw.linePoint(Color.valueOf("A6918A"), Color.valueOf("6B5A55"), p1.x, p1.y, p2.x, p2.y);
				Draw.color(Color.valueOf("BEADA7"));

				for (int j = 0; j < serrations; j++) {
					float progress = ((rot + time/serrations * j) %time / time) + (rot > 0 ? 0f : 1f);

					Tmp.v1.set(p1).lerp(p2, progress);
					Fill.rect(Tmp.v1.x, Tmp.v1.y, 2, 1, angle);
				}
			}
			Draw.reset();
		}
	}

	default void link(Building build) {
		HasForce b = (HasForce) build;
		force().link = build.pos();
		Link link = new Link(this, b);

		graph().addGraph(b.graph());
		graph().links.addUnique(link);

		force().links.addUnique(link);
		b.force().links.addUnique(link);
		graph().entity.remove();
		graph().entity.add();
		b.ratio();
	}
	default void unLink() {
		if (getLink() instanceof HasForce b) {
			Link link = new Link(this, b);
			force().link = -1;

			force().links.remove(link);
			b.force().links.remove(link);
			graph().links.remove(link);
			unLinkGraph();
			graph().floodFill((Building) this).each(build -> graph().add(build));
		}
	}
	default void unLinkGraph() {
		graph().remove((Building) this);
		graph().removeBuild((Building) this);
	}

	default boolean configureBuildTap(Building other) {
		Building from = (Building) this;
		if (other instanceof HasForce next && from.tile().dst(other) < forceConfig().range) {
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
