package sw.world.interfaces;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.graphics.*;
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
		Lines.stroke(3f, Color.grays(0.3f));
		Lines.line(x(), y(), getVibrationLink().x(), getVibrationLink().y());
		Lines.stroke(1f, Color.grays(0.5f));
		Lines.line(x(), y(), getVibrationLink().x(), getVibrationLink().y());
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
		if (getVibrationLink() != null) if (other == getVibrationLink() || other == this) {
			vibrationUnlink();
			return false;
		}
		if (other instanceof HasVibration b && other.dst(this) <= vConfig().range) {
			if (getVibrationLink() != null) vibrationUnlink();
			if (b.getVibrationLink() == this) b.vibrationUnlink();
			vibrationLink(b);
			return false;
		}
		return true;
	}
}
