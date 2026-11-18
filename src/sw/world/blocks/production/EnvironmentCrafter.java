package sw.world.blocks.production;

import mindustry.*;
import mindustry.gen.*;
import mindustry.world.*;

public class EnvironmentCrafter extends SWGenericCrafter {
	public EnvironmentCrafter(String name) {
		super(name);
	}

	public boolean accessible() {
		return buildVisibility.visible() || Vars.state.playtestingMap != null;
	}

	@Override public boolean canBreak(Tile tile) {
		return accessible();
	}

	@Override public boolean checkForceDark(Tile tile) {
		return accessible() || forceDark;
	}

	public class EnvironmentCrafterBuild extends SWGenericCrafterBuild {
		@Override public boolean collide(Bullet other) {
			return false;
		}

		@Override public void damage(float damage) {}
	}

}
