package sw.world.interfaces;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.struct.*;
import mindustry.gen.*;
import sw.world.graph.*;
import sw.world.modules.*;

public interface HasSound extends Buildingc {
  /**
   * misc
   */
	SoundModule sound();
	default SoundGraph soundGraph() {
		return sound().graph;
	}

  /**
   * get sound values
   */
	default float soundIntensity() {
		return sound().amount;
	}

  /**
   * set sound values
   */
	default void addSound(float amount) {
		sound().amount += amount;
	}
	default void setSound(float amount) {
		sound().amount = amount;
	}

  /**
   * other builds
   */
	default Seq<HasSound> soundProximity() {
		return proximity().copy().removeAll(b -> !(b instanceof HasSound)).as();
	}

	default boolean connectsTo(HasSound to) {
		return true;
	}

	/**
	 * draw sound
	 */
	default void drawSound(TextureRegion region, Color color) {
		Draw.mixcol(color, soundIntensity() / 100f);
		Draw.rect(region, x(), y(), block().rotate ? 90 - rotdeg() % 180 - 90 : 0f);
		Draw.reset();
	}
}
