package sw.entities.units;

import arc.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.input.*;

public class LambdaUnit extends LegsUnit {
	@Override public int classId() {
		return EntityMaps.ids.get(getClass().getName());
	}

	@Override public EntityCollisions.SolidPred solidity() {
		return null;
	}

	@Override
	public float speed() {
		return super.speed() * (Core.input.keyDown(Binding.boost) || Core.camera.position.dst(this) > type.buildRange ? type.boostMultiplier : 1f);
	}
}
