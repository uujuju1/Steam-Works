package sw.ai;

import arc.math.*;
import arc.util.*;
import mindustry.*;
import mindustry.entities.units.*;
import mindustry.gen.*;

public class RandomAI extends AIController {
	public float turnSpeed = 10f;
	public boolean homeToTarget;
	private float randomDir = Mathf.random(360f);

	@Override
	public void updateMovement() {
		randomDir = Mathf.mod(randomDir + Mathf.range(turnSpeed), 360f);

		Tmp.v1.trns(randomDir, 80f).scl(
			Mathf.sign(Tmp.v1.x + unit.x >= 0 && Tmp.v1.x + unit.x <= Vars.world.unitWidth()),
			Mathf.sign(Tmp.v1.y + unit.y >= 0 && Tmp.v1.y + unit.y <= Vars.world.unitHeight())
		);

		randomDir = Tmp.v1.angle();

		Tmp.v1.add(unit);


		Teamc homingTarget = findTarget(unit.x, unit.y, unit.type.maxRange, true, true);
		if (homingTarget != null && homeToTarget) {
			moveTo(homingTarget, 0);
		} else {
			moveTo(Tmp.v1, 0);
		}
	}
}
