package sw.dream.states;

import arc.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.*;
import mindustry.entities.units.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.world.*;
import sw.content.*;
import sw.dream.Dream.*;
import sw.dream.dummy.*;
import sw.world.blocks.environment.*;

public class AttractState extends DreamState {
	public float time;

	public boolean reached;

//	@Override
//	public void draw() {
//		Draw.flush();
//		Draw.proj(Core.camera);
//		Draw.draw(Layer.blockOver, () -> {
//			Draw.alpha(Mathf.clamp(time / 600f));
//			Fill.circle(Core.camera.position.x, Core.camera.position.y, 5f);
//		});
//	}

	@Override
	public void update() {
		if (!Vars.net.active() && !Vars.state.rules.editor && Vars.state.map != null && Vars.state.map.name().equals(SWSectorPresets.abandonedMaze.generator.map.name())) {
			Vars.control.sound.loop(Sounds.rain, Mathf.clamp(time / 600f));

			if (reached) {
				Vars.control.sound.loop(Sounds.mechStepSmall, 1);

				if (Vars.renderer.getScale() != Mathf.round(Scl.scl(Vars.renderer.maxZoomInGame))) {
					Groups.unit.each(u -> u != Vars.player.unit(), u -> {
						u.move(Tmp.v1.set(Vars.player.unit()).sub(u).clampLength(0f, 8f));

						if (u.dst(Vars.player.unit()) < u.hitSize * 2f) {
							// TODO phase 2
							Core.app.exit();
						}
					});
				}
			} else pushPlayer();
		}
	}

	private void pushPlayer() {
		Tile camTile = Vars.world.tileWorld(Core.camera.position.x, Core.camera.position.y);

		if (camTile != null && Vars.renderer.getScale() == Mathf.round(Scl.scl(Vars.renderer.maxZoomInGame))) {
			boolean hasPitfall = camTile.floor() instanceof Pitfall;
			for (Point2 offset : Geometry.d8) {
				if (camTile.nearby(offset.x, offset.y) == null) hasPitfall &= camTile.nearby(offset.x, offset.y).floor() instanceof Pitfall;
			}

			if (hasPitfall) {
				time = Mathf.approachDelta(time, 600f, 1f);
			} else time = Mathf.approachDelta(time, 0f, 5f);
		} else {
			time = Mathf.approachDelta(time, 0f, 5f);

			Unit playerUnit = Vars.player.unit();
			if (playerUnit != null) playerUnit.move(Tmp.v1.set(50.5f, 161.5f).scl(8f).sub(playerUnit).clampLength(0f, 0.1f));
		}

		if (time == 600f) {
			reached = true;
			// TODO Setting for phase two, so that if the game closes it already goes there.
			spawnUnits();
		}
	}

	private void spawnUnits() {
		for (int i = 0; i < 10; i++) {
			DummyLambda entity = new DummyLambda();

			entity.setType(SWUnitTypes.lambda);
			entity.set(Tmp.v1.trns(Mathf.random(360f), 160f).add(50.5f * 8f, 161.5f * 8f));
			entity.team = Team.crux;
			entity.controller(new AIController());
			entity.rotation = entity.angleTo(50.5f * 8f, 161.5f * 8f);
			entity.health = 1000000;
			entity.add();
		}
	}
}
