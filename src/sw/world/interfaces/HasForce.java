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
import sw.world.graph.ForceGraph.*;
import sw.world.meta.*;
import sw.world.modules.*;

public interface HasForce extends Buildingc, Posc{
	ForceModule force();
	ForceConfig fConfig();
	default @Nullable HasForce getForceLink() {
		return (HasForce) Vars.world.build(force().link);
	}
	default ForceGraph fGraph() {
		return force().graph;
	}

	default float beltSize() {
		return fConfig().beltSize;
	}

	default float spin() {
		return fGraph().rotation;
	}
	default float speed() {
		return force().speed * ratioScl();
	}
	default float ratioScl() {
		return switch (getRatio()) {
			case equal -> 1f;
			case normal -> 2f;
			case extreme -> 3f;
		};
	}
	default float ratioNumber() {
		return force().links.sumf(link -> link.ratio(link.startBuild() == this))/force().links.size;
	}
	default ForceRatio getRatio() {
		int ratio = Mathf.round(Math.max(ratioNumber(), 1/ratioNumber()));
		return switch (ratio) {
			case 2 -> ForceRatio.normal;
			case 3 -> ForceRatio.extreme;
			default -> ForceRatio.equal;
		};
	}

	default void drawBelt() {
		if (getForceLink() != null) {
			Draw.z(Layer.block - 1f);
			Vec2 start = new Vec2(x(), y()), end = new Vec2(getForceLink().x(), getForceLink().y());
			float angle = Tmp.v1.set(start).sub(end).angle() + 90;
			for (int i = 0; i < 2; i++) {
				Tmp.v1.trns(angle + i * 180, beltSize());
				Vec2 p1 = new Vec2(start).add(Tmp.v1);

				Tmp.v1.trns(angle + i * 180, getForceLink().beltSize());
				Vec2 p2 = new Vec2(end).add(Tmp.v1);

				if (i == 1) {
					Tmp.v1.set(p1);
					p1.set(p2);
					p2.set(Tmp.v1);
				}
				SWDraw.beltLine(SWDraw.compoundSerration, SWDraw.compoundBase, SWDraw.compoundMiddle, p1.x, p1.y, p2.x, p2.y, spin());
			}
			Draw.reset();
		}
	}

	default void createForceLink(HasForce other) {
		ForceLink forceLink = new ForceLink(this, other);
		force().links.add(forceLink);
		other.force().links.add(forceLink);
		fGraph().addLink(forceLink);
	}
	default void removeForceLink(HasForce other) {
		if (other != null) {
			ForceLink forceLink = new ForceLink(this, other);
			force().links.remove(forceLink);
			other.force().links.remove(forceLink);
			fGraph().removeLink(forceLink);
		}
	}

	default boolean configureForceLink(Building other) {
		if (fConfig().outputsForce) {
			if (getForceLink() != null) if (other == getForceLink() || other == this) {
				removeForceLink(getForceLink());
				force().link = -1;
				return false;
			}
			if (other instanceof HasForce build && other.dst(this) <= fConfig().range && build.fConfig().acceptsForce) {
				if (getForceLink() != null) removeForceLink(getForceLink());
				if (build.getForceLink() == this) {
					build.removeForceLink(this);
					build.force().link = -1;
				}
				createForceLink(build);
				force().link = build.pos();
				return false;
			}
		}
		return true;
	}
}
