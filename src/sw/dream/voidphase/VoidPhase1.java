package sw.dream.voidphase;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import sw.audio.*;
import sw.content.*;
import sw.dream.*;
import sw.dream.event.*;

public class VoidPhase1 extends DreamEvent {
	public float warmup = 0f;

	public float spawnRadius = 40;
	public Vec2 spawnPos = new Vec2(310, 277).scl(8f);

	@Override
	public void draw() {
		super.draw();

		Lines.stroke(10f, Pal.accent);
		Lines.circle(2000f, 2000f, 40f * Interp.circle.apply(warmup));
		Tmp.v1.trns(spawnPos.angleTo(2000f, 2000) + 180f, 40f * Interp.circle.apply(warmup)).add(2000f, 2000f);
		Drawf.tri(
			Tmp.v1.x, Tmp.v1.y,
			10f * Interp.circle.apply(warmup),
			40f * Interp.circle.apply(warmup),
			spawnPos.angleTo(2000f, 2000) + 180f
		);
	}

	@Override
	public void update() {
		VoidEvent.checkVoid();
		warmup = Mathf.approachDelta(warmup, 1f, 0.014f);

		if (Vars.state.rules.objectiveFlags.contains("enemies")) {
			Fx.dynamicWave.at(2000f, 2000f, 80f, Pal.accent);
			Fx.shieldBreak.at(2000f, 2000f, 80f);
			Vars.state.rules.spawns.select(spawn -> spawn.getSpawned(0) > 0).each(spawnGroup -> {
				for (int i = 0; i < spawnGroup.getSpawned(0); i++) {
					Tmp.v1.setToRandomDirection().scl(Mathf.random(spawnRadius)).add(spawnPos);
					Unit unit = spawnGroup.createUnit(Team.crux, 0);
					unit.set(Tmp.v1);
					unit.controller(unit.type.aiController.get());
					unit.add();
				}
			});
			SWFx.realityTear.at(spawnPos, spawnRadius * 1.5f);
			ModSounds.sonarShoot.at(spawnPos.x, spawnPos.y, 1f, 3f);
			DreamCore.instance.event(new VoidEvent(3));
		}
	}
}
