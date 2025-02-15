package sw.entities;

import arc.math.geom.*;

public class Arm {
	public Vec2 targetPos = new Vec2();
	public Vec2 startPos = new Vec2();
	public float time = 0;

	public void changePos(Vec2 vec) {
		time = 0f;
		startPos.set(targetPos);
		targetPos.set(vec);
	}

	public Arm reset(float rotation, float armLength) {
		startPos.trns(rotation, armLength/2f - 0.001f);
		targetPos.trns(rotation, armLength/2f);
		return this;
	}
}
