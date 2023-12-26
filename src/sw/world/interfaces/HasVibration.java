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
import sw.world.graph.VibrationGraph.*;
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
		if (getVibrationLink() == null) return;
		Draw.z(Layer.block - 0.01f);

		Vec2 pos1 = new Vec2(x(), y());
		Vec2 pos2 = new Vec2(getVibrationLink().x(), getVibrationLink().y());
		SWDraw.linePoint(
			SWDraw.denseAlloyMiddle,
			SWDraw.denseAlloyBase,
			pos1.x, pos1.y,
			pos2.x, pos2.y
		);

		int serrations = Mathf.floor(pos1.dst(pos2)/4);
		float angle = Tmp.v1.set(pos1).sub(pos2).angle();
		Draw.color(SWDraw.denseAlloySerration);
		for (int i = 0; i < serrations + 1; i++) {
			Tmp.v1.set(pos1).lerp(pos2, i/(float)serrations);

			Tmp.v2.set(0, 0);
			for(int j = 0; j < 5; j++) {
				Tmp.v2.add((float) Math.sin((Time.time + i) * (j + 1f))/10f, 0f);
			}
			Tmp.v2.rotate(angle + 90).limit(1);
			Tmp.v1.add(Tmp.v2);

			Fill.rect(Tmp.v1.x, Tmp.v1.y, 1, 4, angle);
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
		VibrationLink link = new VibrationLink(this.pos(), other.pos());
		vibration().links.addUnique(link);
		other.vibration().links.addUnique(link);
		vGraph().addLink(link);
	}
	default void removeVibrationLink(HasVibration other) {
		if (other != null) {
			VibrationLink link = new VibrationLink(this.pos(), other.pos());
			vibration().links.remove(link);
			other.vibration().links.remove(link);
			vGraph().removeLink(link);
		}
	}
}
