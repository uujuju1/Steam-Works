package sw.world.interfaces;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import sw.util.*;
import sw.world.graph.*;
import sw.world.meta.*;
import sw.world.modules.*;

public interface HasVibration extends Buildingc {
	VibrationConfig vConfig();
	VibrationModule vibration();
	default VibrationGraph vGraph() {
		return vibration().graph;
	}
	default @Nullable HasVibration getVibrationLink() {
		return Vars.world.build(vibration().link) instanceof HasVibration b ? b : null;
	}
	default Seq<HasVibration> getVibrationLinks() {
		return vibration().links.map(link -> link.other(this));
	}

	default void drawLink() {
		Draw.z(Layer.block - 0.01f);
		if (getVibrationLink() == null) return;
		Vec2
			x = new Vec2(x(), y()),
			y = new Vec2(getVibrationLink().x(), getVibrationLink().y());

		SWDraw.linePoint(SWDraw.denseAlloyMiddle, SWDraw.denseAlloyBase, x.x, x.y, y.x, y.y);

		Draw.color(SWDraw.denseAlloySerration);
		int serrations = Mathf.floor((x.dst(y)/4f));
		for (int i = 0; i < serrations; i++) {
			float angle = Tmp.v1.set(x).sub(y).angle();
			float intensity = 0;

			for(float f : vGraph().frequencies.toArray()) intensity += Math.sin(Time.time + i * f) * f / 4000;

			Tmp.v2.set(x).lerp(y, i/((float) serrations)).add(Tmp.v1.trns(angle + 90, intensity));

			Fill.rect(Tmp.v2.x, Tmp.v2.y + Tmp.v1.y, 1f, 5f, angle);
			Fill.rect(Tmp.v2.x, Tmp.v2.y + Tmp.v1.y, 1f, 5f, angle);
		}

		Draw.reset();
		Draw.z(Layer.block);
	}

	default boolean configVibrationLink(Building other) {
		if (vConfig().outputsVibration) {
			if (getVibrationLink() != null) if (other == getVibrationLink() || other == this) {
				removeVibrationLink(getVibrationLink());
				vibration().link = -1;
				return false;
			}
			if (other instanceof HasVibration build && other.dst(this) <= vConfig().range && build.vConfig().acceptsVibration) {
				if (getVibrationLink() != null) removeVibrationLink(getVibrationLink());
				if (build.getVibrationLink() == this) {
					build.removeVibrationLink(this);
					build.vibration().link = -1;
				}
				createVibrationLink(build);
				vibration().link = build.pos();
				return false;
			}
		}
		return true;
	}

	default void createVibrationLink(HasVibration other) {
		VibrationGraph.VibrationLink link = new VibrationGraph.VibrationLink(this.pos(), other.pos());
		vibration().links.addUnique(link);
		other.vibration().links.addUnique(link);
		vGraph().addLink(link);
	}
	default void removeVibrationLink(HasVibration other) {
		if (other != null) {
			VibrationGraph.VibrationLink link = new VibrationGraph.VibrationLink(this.pos(), other.pos());
			vibration().links.remove(link);
			other.vibration().links.remove(link);
			vGraph().removeLink(link);
		}
	}
}
