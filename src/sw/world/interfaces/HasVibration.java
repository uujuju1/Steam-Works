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

public interface HasVibration extends Buildingc {
	VibrationConfig vConfig();
	VibrationModule vibration();
	default VibrationGraph vGraph() {
		return vibration().graph;
	}
	default @Nullable HasVibration getVibrationLink() {
		return Vars.world.build(vibration().link) instanceof HasVibration b ? b : null;
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

	default void vibrationLink(HasVibration build) {
		vibration().link = build.pos();
		vibration().links.addUnique(new VibrationGraph.VibrationLink(pos(), build.pos()));
		build.vibration().links.addUnique(new VibrationGraph.VibrationLink(pos(), build.pos()));
		vGraph().links.addUnique(new VibrationGraph.VibrationLink(pos(), build.pos()));
		vGraph().updateBuilds();
	}
	default void vibrationUnlink() {
		vibration().links.remove(new VibrationGraph.VibrationLink(pos(), getVibrationLink().pos()));
		getVibrationLink().vibration().links.remove(new VibrationGraph.VibrationLink(pos(), getVibrationLink().pos()));
		vGraph().links.remove(new VibrationGraph.VibrationLink(pos(), getVibrationLink().pos()));
		vibration().link = -1;
		vGraph().updateBuilds();
	}

	default boolean configVibrationLink(Building other) {
		if (vConfig().outputsVibration) {
			if (getVibrationLink() != null) if (other == getVibrationLink() || other == this) {
				vibrationUnlink();
				return false;
			}
			if (other instanceof HasVibration b && other.dst(this) <= vConfig().range && b.vConfig().acceptsVibration) {
				if (getVibrationLink() != null) vibrationUnlink();
				if (b.getVibrationLink() == this) b.vibrationUnlink();
				vibrationLink(b);
				return false;
			}
		}
		return true;
	}
}
