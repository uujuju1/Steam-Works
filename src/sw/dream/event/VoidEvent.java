package sw.dream.event;

import arc.math.*;
import mindustry.*;
import mindustry.core.*;
import sw.dream.*;
import sw.dream.voidphase.*;

import java.util.*;

public class VoidEvent extends DreamEvent {
	public static boolean isVoid = false;
	public int phase;

	public VoidEvent(int phase) {
		this.phase = phase;
	}

	public static void checkVoid() {
		if (!Objects.equals(Vars.state.map.name(), "void") || Vars.state.isEditor() || Vars.state.getState() == GameState.State.menu) {
			DreamCore.instance.event(null);
			isVoid = false;
		}
	}

	@Override
	public void init() {
		isVoid = true;
	}

	@Override
	public void update() {
		checkVoid();
		Vars.state.rules.waves = false;
		phase = Vars.state.rules.objectiveFlags.size;
		switch (phase) {
			default -> DreamCore.instance.event(new VoidIntro());
			case 1 -> {
//				checkVoid();
				Vars.state.rules.ambientLight.a(Mathf.approachDelta(Vars.state.rules.ambientLight.a, 0, 0.014f));
			}
			case 2 -> DreamCore.instance.event(new VoidPhase1());
			case 3 -> {}
		}
	}
}
