package sw.math;

import arc.math.geom.*;
import arc.util.*;
import mindustry.*;

/**
 * Note, an elevation of -1 will make the position infinitely far away, so it'll be at the ref's position at all times
 */
public class Parallax {
	public static float getParallaxFrom(float from, float ref, float elevation) {
		return from + (from - ref) * elevation * Vars.renderer.getDisplayScale()/48f;
	}

	public static Vec2 getParallaxFrom(Vec2 from, Vec2 ref, float elevation) {
		return from.add(Tmp.v1.set(from).sub(ref).scl(elevation * Vars.renderer.getDisplayScale()/48f));
	}
}
