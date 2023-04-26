package sw.world.interfaces;

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
		return force().speed;
	}
	default float strength() {return force().strength;}

	default void drawBelt(Building b) {
		if (b instanceof HasForce a && a.getLink() != null) {
			Draw.z(Layer.block - 1f);
			for (int i : Mathf.signs) {
				Vec2 p1 = new Vec2(b.x, b.y), p2 = new Vec2(a.getLink().x, a.getLink().y);
				int serrations = Mathf.ceil(p1.dst(p2) / 8f);
				float shift = spin() * i;

				float angle = Tmp.v1.set(p1).sub(p2).angle();
				p1.add(Tmp.v1.trns(angle + 90 + 180 * Mathf.maxZero(i), forceConfig().beltSizeOut));
				p2.add(Tmp.v1.trns(angle + 90 + 180 * Mathf.maxZero(i), ((HasForce) a.getLink()).forceConfig().beltSizeIn));
				angle = Tmp.v1.set(p1).sub(p2).angle();

				SWDraw.linePoint(Pal.gray.cpy().mul(1.5f), p1.x, p1.y, p2.x, p2.y);

				Draw.color(Pal.gray.cpy().mul(2f));

				for (int j = 0; j < serrations; j++) {
					Tmp.v1.trns(angle, (shift % 8f) - Mathf.maxZero(8f * i));
					Tmp.v2.set(p1).lerp(p2, j / (float) serrations).add(Tmp.v1);
					Fill.rect(Tmp.v2.x, Tmp.v2.y, 2, 1, angle);
				}
			}
			Draw.reset();
		}
	}
}
