package sw.dream.voidphase;

import arc.graphics.*;
import arc.math.*;
import arc.util.*;
import mindustry.*;
import mindustry.gen.*;
import sw.audio.*;
import sw.content.*;
import sw.dream.*;
import sw.dream.event.*;

public class VoidIntro extends DreamEvent {
	public float timer;

	@Override
	public void init() {
		Vars.state.rules.ambientLight = Color.valueOf("FFF8E7");
		Vars.state.rules.lighting = true;
	}

	@Override
	public void update() {
		VoidEvent.checkVoid();
		if (!(Vars.player.unit() instanceof Legsc)) return;
		if (timer < 600f) {
			Vars.player.unit().set(2000f, 2000);
			Vars.player.unit().vel.set(0f, 0f);
			Vars.renderer.setScale(1f);
			((Legsc) Vars.player.unit()).baseRotation(Time.time % 360f);
			timer += Time.delta;
		} else {
			Vars.state.rules.lighting = false;
			SWFx.realityTear.at(235f * 8f, 262f * 8f, 140f);
			SWFx.realityTear.at(241.5f * 8f, 228f * 8f, 80f);
			ModSounds.sonarShoot.at(235f * 8f, 262f * 8f, Mathf.random(0.5f, 1f), 2.8f);
			ModSounds.sonarShoot.at(241.5f * 8f, 228f * 8f, Mathf.random(0.5f, 1f), 1.6f);
			Vars.state.rules.objectiveFlags.add("introfinish");
			DreamCore.instance.event(new VoidEvent(1));
		}
	}
}
