package sw.world.interfaces;

import arc.math.geom.*;
import sw.entities.*;

public interface HasArm {
	Vec2 unset = new Vec2();

	default Arm arm() {
		throw new RuntimeException("Missing arm object. Either override the arm() method or use a different drawer that doesn't use it");
	}

	default float getArmTime() {
		return 0;
	}

	default Vec2 getArmPos() {
		return unset;
	}
}
