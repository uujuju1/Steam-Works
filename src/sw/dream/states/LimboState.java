package sw.dream.states;

import arc.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.core.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.world.*;
import sw.content.*;
import sw.dream.Dream.*;
import sw.dream.*;
import sw.dream.dummy.*;

public class LimboState extends DreamState {
	public boolean once = false;
	public boolean looped;

	public float speed;

	public Unit playerUnit, dummy;

	private void loadDarkMap() {
		Tiles tiles = Vars.world.resize(100, 100);
		Vars.world.beginMapLoad();
		tiles.fill();

		tiles.eachTile(tile -> {
			tile.setFloor(DreamContent.voidTile.asFloor());
			tile.setBlock(Mathf.mod(tile.x, 10) == 0 && Mathf.mod(tile.y, 10) == 0 ? DreamContent.spikes : Blocks.air);
		});
		Vars.world.endMapLoad();

		Vars.state.rules = new Rules();
		Core.audio.stop(Core.audio.musicBus);
		Vars.state.rules.canGameOver = false;

		Vars.state.set(GameState.State.playing);

		DummyLambda entity = (DummyLambda) (playerUnit = new DummyLambda());

		entity.setType(SWUnitTypes.lambda);
		entity.set(Vars.world.unitWidth() / 2f, Vars.world.unitHeight() / 2f);
		entity.team = Team.sharded;
		entity.controller(Vars.player);
		entity.rotation = 0;
		entity.elevation = 1f;
		entity.heal();
		entity.add();
	}

	@Override
	public void update() {
		boolean hasMap = Vars.state.isMenu();

		if (hasMap && !once) {
			loadDarkMap();
			once = true;
		}

		if (!Tmp.r1.setCentered(Vars.world.unitWidth()/2f, Vars.world.unitHeight()/2f, Vars.world.unitWidth() - 320f, Vars.world.unitHeight() - 320f).contains(playerUnit.x, playerUnit.y)) {
			Tmp.v1.set(playerUnit).sub(160, 160f);

			Tmp.v1.x = Mathf.mod(Tmp.v1.x, Tmp.r1.width);
			Tmp.v1.y = Mathf.mod(Tmp.v1.y, Tmp.r1.height);

			Tmp.v1.add(160f, 160);

			Vec2 offset = Tmp.v2.set(Tmp.v1).sub(playerUnit);
			playerUnit.set(Tmp.v1);

			Leg[] legs = ((LegsUnit) playerUnit).legs;

			for (Leg leg : legs) {
				leg.base.add(offset);
				leg.joint.add(offset);
			}

			if (!looped) {
				DummyLambda entity = (DummyLambda) (dummy = new DummyLambda());

				entity.setType(SWUnitTypes.lambda);
				entity.set(Vars.world.unitWidth() / 2f, Vars.world.unitHeight() / 2f);
				entity.team = Team.sharded;
				entity.controller(new AIController());
				entity.rotation = 0;
				entity.elevation = 1f;
				entity.heal();
				entity.add();
			}

			looped = true;
		}

		Core.camera.position.set(playerUnit);
		if (playerUnit.controller() != Vars.player) playerUnit.controller(Vars.player);
		if (!playerUnit.isValid()) {
			playerUnit.dead = false;
			playerUnit.add();
			playerUnit.heal();
			playerUnit.controller(Vars.player);
		}

		if (dummy != null) {
			dummy.rotation = dummy.angleTo(playerUnit);
			if (dummy.team == Team.sharded) {
				dummy.set(Tmp.v1.set(dummy).sub(playerUnit).clampLength(80f, Float.POSITIVE_INFINITY).add(playerUnit));
				if (!Tmp.r1.setCentered(Vars.world.unitWidth()/2f, Vars.world.unitHeight()/2f, Vars.world.unitWidth() - 320f, Vars.world.unitHeight() - 320f).contains(dummy.x, dummy.y)) {
					dummy.team = Team.crux;
				}
			} else {
				dummy.move(Tmp.v1.set(playerUnit).sub(dummy).setLength(speed));
				speed += 1 / 600f * Time.delta;

				if (playerUnit.dst(dummy) < dummy.hitSize * 2f) {
					Core.settings.put("sw-fool-state", 2);
					Dream.self.currentState = 2;
					Vars.logic.reset();
				}
			}
		}

		// TODO enable this after debugging
		Vars.disableUI = true;
		Vars.renderer.minZoomInGame = Vars.renderer.maxZoomInGame = 8;
		Vars.renderer.minZoom = Vars.renderer.maxZoom = 8;
	}
}
