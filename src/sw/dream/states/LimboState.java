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
	public int looped;

	public float[] zooms = new float[4];

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

	private void moveUnit(Unit unit) {
		Tmp.v1.set(playerUnit).sub(160, 160f);

		Tmp.v1.x = Mathf.mod(Tmp.v1.x, Tmp.r1.width);
		Tmp.v1.y = Mathf.mod(Tmp.v1.y, Tmp.r1.height);

		Tmp.v1.add(160f, 160);

		Vec2 offset = Tmp.v2.set(Tmp.v1).sub(playerUnit);

		if (unit instanceof LegsUnit legsUnit) {
			Leg[] legs = legsUnit.legs;

			for (Leg leg : legs) {
				leg.base.add(offset);
				leg.joint.add(offset);
			}
		}
		playerUnit.set(Tmp.v1);
	}

	@Override
	public void update() {
		boolean hasMap = Vars.state.isMenu();

		if (hasMap && !once) {
			loadDarkMap();
			zooms[0] = Vars.renderer.minZoomInGame;
			zooms[1] = Vars.renderer.maxZoomInGame;
			zooms[2] = Vars.renderer.minZoom;
			zooms[3] = Vars.renderer.maxZoom;
			once = true;
		}

		if (!Tmp.r1.setCentered(Vars.world.unitWidth()/2f, Vars.world.unitHeight()/2f, Vars.world.unitWidth() - 320f, Vars.world.unitHeight() - 320f).contains(playerUnit.x, playerUnit.y)) {
//			Tmp.v1.set(playerUnit).sub(160, 160f);
//
//			Tmp.v1.x = Mathf.mod(Tmp.v1.x, Tmp.r1.width);
//			Tmp.v1.y = Mathf.mod(Tmp.v1.y, Tmp.r1.height);
//
//			Tmp.v1.add(160f, 160);
//
//			Vec2 offset = Tmp.v2.set(Tmp.v1).sub(playerUnit);
//			playerUnit.set(Tmp.v1);
//
//			Leg[] legs = ((LegsUnit) playerUnit).legs;
//
//			for (Leg leg : legs) {
//				leg.base.add(offset);
//				leg.joint.add(offset);
//			}

			moveUnit(playerUnit);
			Vec2 offset = Tmp.v2;

			Tmp.r2.setCentered(Core.camera.position.x, Core.camera.position.y, Core.camera.width, Core.camera.height).grow((SWUnitTypes.lambda.hitSize + SWUnitTypes.lambda.legLength) * 3);
//			if (dummy != null && Tmp.r2.contains(dummy.x, dummy.y)) moveUnit(dummy);
			if (dummy != null && Tmp.r2.contains(dummy.x, dummy.y)) {
				dummy.move(offset);
				for (Leg leg : ((DummyLambda) dummy).legs) {
					leg.base.add(offset);
					leg.joint.add(offset);
				}
			}

			if (looped == 3) {
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

			looped++;
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
				dummy.vel.set(playerUnit).sub(dummy).setLength(speed);
				speed += 1 / 1200f * Time.delta;

				if (playerUnit.dst(dummy) < dummy.hitSize * 2f) {
					Core.settings.put("sw-fool-state", 2);
					Core.settings.saveValues();
					Core.app.exit();
				}
			}
		}

		// TODO enable this after debugging
		Vars.disableUI = true;
		Vars.renderer.minZoomInGame = Vars.renderer.maxZoomInGame = 8;
		Vars.renderer.minZoom = Vars.renderer.maxZoom = 8;
	}
}
