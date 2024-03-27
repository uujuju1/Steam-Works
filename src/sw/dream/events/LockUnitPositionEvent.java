package sw.dream.events;

import arc.util.*;
import mindustry.gen.*;
import sw.dream.*;

public class LockUnitPositionEvent extends DreamEvent {
	public Unit unit;
	public float lockTime;

	public float lastx, lasty, lastRot;
	public float time;

	public LockUnitPositionEvent(Unit unit, float lockTime) {
		this.lockTime = lockTime;
		this.unit = unit;
	}

	@Override
	public void init() {
		lastx = unit.x;
		lasty = unit.y;
		lastRot = unit.rotation;
	}

	@Override
	public void update() {
		if (time <= lockTime) {
			unit.set(lastx, lasty);
			unit.vel.set(0f, 0f);
			unit.rotation = lastRot;
			time += Time.delta;
		} else {
			DreamCore.instance.event(null);
		}
	}
}
