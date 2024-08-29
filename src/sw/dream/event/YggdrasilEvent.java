package sw.dream.event;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import sw.content.*;
import sw.dream.*;

import static mindustry.Vars.*;

public class YggdrasilEvent extends DreamEvent {
	public float minZoom;
	public float maxZoom;
	public boolean canLeave, change;

	public Seq<TriggerBox> trigger = new Seq<>();

	@Override public void draw() {
		trigger.each(trigger -> trigger.draw(player.unit()));
	}

	@Override
	public void end() {
		renderer.minZoom = minZoom;
		renderer.maxZoom = maxZoom;
		enableDarkness = true;
		Sounds.windhowl.stop();
		Sounds.rain.stop();
	}

	@Override
	public void init() {
		minZoom = renderer.minZoom;
		maxZoom = renderer.maxZoom;
		enableDarkness = false;
		state.rules.lighting = true;
		state.rules.ambientLight = Color.valueOf("FFF8E3");

		Sounds.windhowl.loop(0.5f);
		Sounds.rain.loop(0.5f);

		//region portals
		//hallway
		trigger.add(
			new Portal() {{
				area.setSize(8f, 16f);
				x = 32.5f * 8;
				y = 30.5f * 8;
				endX = 32.5f * 8;
				endY = 24.5f * 8;
			}},
			new Portal() {{
				area.setSize( 8f, 16f);
				x = 20.5f * 8;
				y = 24.5f * 8;
				endX = 20.5f * 8;
				endY = 30.5f * 8;
			}}
		);
		//pillars
		trigger.add(
			new Portal() {{
				area.setSize(8f, 16);
				x = 24f * 8;
				y = 14.5f * 8;
				endX = 28f * 8;
				endY = 14.5f * 8;
			}},
			new Portal() {{
				area.setSize(8f, 16f);
				x = 29f * 8;
				y = 14.5f * 8;
				endX = 25f * 8;
				endY = 14.5f * 8;
			}},
			new Portal() {{
				area.setSize(8f, 16f);
				x = 24f * 8;
				y = 10.5f * 8;
				endX = 41f * 8;
				endY = 10.5f * 8;
			}},
			new Portal() {{
				area.setSize(8f, 16f);
				x = 29f * 8;
				y = 10.5f * 8;
				endX = 38f * 8;
				endY = 10.5f * 8;
			}}
		);
		trigger.add(
			new Portal() {{
				area.setSize(8f, 16);
				x = 37f * 8;
				y = 10.5f * 8;
				endX = 41f * 8;
				endY = 10.5f * 8;
			}},
			new Portal() {{
				area.setSize(8f, 16f);
				x = 42f * 8;
				y = 10.5f * 8;
				endX = 38f * 8;
				endY = 10.5f * 8;
			}},
			new Portal() {{
				area.setSize(8f, 16f);
				x = 37f * 8;
				y = 14.5f * 8;
				endX = 28f * 8;
				endY = 14.5f * 8;
			}},
			new Portal() {{
				area.setSize(8f, 16f);
				x = 42f * 8;
				y = 14.5f * 8;
				endX = 25f * 8;
				endY = 14.5f * 8;
			}}
		);
		//corridor
		trigger.add(
			new Portal() {{
				area.setSize(16f, 8f);
				x = 0;
				y = 43f * 8;
				endX = 49.5f * 8;
				endY = 43f * 8;
			}},
			new Portal() {{
				area.setSize(16f, 8f);
				x = 55.5f * 8;
				y = 44f * 8;
				endX = 55.5f * 8;
				endY = 42f * 8;
			}},
			new Portal() {{
				area.setSize(16f, 8f);
				x = 55.5f * 8;
				y = 41f * 8;
				endX = 55.5f * 8;
				endY = 43f * 8;
			}}
		);
		//void
		trigger.add(
			new Portal() {{
				area.setSize(18 * 8f, 8f);
				x = 69.5f * 8;
				y = 19f * 8;
				endX = 69.5f * 8;
				endY = 18f * 8;
			}},
			new Portal() {{
				area.setSize(18 * 8f, 9f);
				x = 69.5f * 8;
				y = 6f * 8;
				endX = 69.5f * 8;
				endY = 7f * 8;
			}},
			new Portal() {{
				area.setSize(8f, 18 * 8f);
				x = 76f * 8;
				y = 12.5f * 8;
				endX = 75f * 8;
				endY = 12.5f * 8;
			}},
			new Portal() {{
				area.setSize(8f, 16f);
				x = 56f * 8;
				y = 12.5f * 8;
				endX = 20.5f * 8;
				endY = 30.5f * 8;
			}}
		);
		trigger.add(new Portal() {{
			area.setSize(8f, 16f);
			x = 0f * 8;
			y = 30.5f * 8;
			endX = 24.5f * 8;
			endY = 30.5f * 8;
		}});
		//endregion
		//region zooms
		trigger.add(
			new Zoom() {{
				area.setCentered(15f * 8, 30.5f * 8, 8f, 16f);
				zoom = 24f;
			}},
			new Zoom() {{
				area.setCentered(39.5f * 8, 71.5f * 8, 34 * 8f);
				zoom = 3f;
			}},
			new Zoom() {{
				area.setCentered(39.5f * 8, 51f * 8, 16f, 8f);
				zoom = 24f;
			}}
		);
		trigger.add(
			new Zoom() {{
				area.setCentered(52.5f * 8, 31.5f * 8, 64f);
				zoom = 8f;
			}},
			new Zoom() {{
				area.setCentered(52.5f * 8, 36f * 8, 64f, 8f);
				zoom = 24f;
			}},
			new Zoom() {{
				area.setCentered(10.5f * 8, 30.5f * 8, 64f);
				zoom = 8f;
			}}
		);
		//endregion
		//region mirror
		trigger.add(new Mirror() {{
			area.setCentered(
				33f * 8,
				12.5f * 8,
				212, 80
			);
			offsets.add(new Vec2(32f, 0), new Vec2(-32f, 0));
		}});
		//endregion
		//region lights
		trigger.add(
			new Light() {{
				area.setCentered(39.5f * 8, 71.5f * 8, 34f * 8, 42f * 8);
				x = 39.5f * 8;
				y = 71.5f * 8;
				radius = 20f * 8;
			}},
			new Light() {{
				area.setCentered(52.5f * 8, 31.5f * 8, 16f * 8);
				x = 52.5f * 8;
				y = 31.5f * 8;
				radius = 64f;
			}}
		);
		//endregion
		//region timer
		trigger.add(
			new Timer() {{
				area.setCentered(10.5f * 8, 30.5f * 8, 64f);
				run = () -> canLeave = true;
			}},
			new Timer() {{
				area.setCentered(55.5f * 8f, 42.5f * 8, 16f, 32f);
				lifetime = 300f;
				run = () -> {
					player.unit().trns(-22f * 8f, 0f);
					Core.camera.position.add(-22f* 8f, 0f);
				};
			}},
			new Timer() {{
				area.setCentered(33.5f * 8f, 43.5f * 8, 16f, 64f);
				lifetime = 600f;
				run = () -> {
					player.unit().set(69.5f * 8f, 12.5f * 8f);
					Core.camera.position.set(69.5f * 8f, 12.5f * 8f);
				};
			}}
		);
		//endregion
	}

	@Override
	public void update() {
		trigger.each(trigger -> trigger.act(player.unit()));
		if (player.unit().stack.amount > 0 && !change) {
			((Portal) trigger.get(10)).x = 39.5f * 8f;
			((Portal) trigger.get(17)).x = 25.5f * 8f;
			change = true;
		}
		if (change) {
			player.unit().stack.amount = 1;
			player.unit().stack.item = SWItems.nickel;
			player.unit().mineTile = null;
		}
		if (state.rules.defaultTeam.items().get(SWItems.nickel) > 0) {
			state.rules.defaultTeam.core().kill();
			player.unit().kill();
			DreamCore.instance.event(null);
			Sounds.unlock.at(player.unit());
			Core.settings.put("sw-dream-lore", 1);
		}
		Vars.control.sound.stop();
	}

	abstract static class TriggerBox {
		public void act(Posc pos) {}

		public void draw(Unit unit) {

		}
	}

	public static class Mirror extends TriggerBox {
		public Rect area = new Rect();
		public Seq<Vec2> offsets = new Seq<>();

		@Override
		public void draw(Unit unit) {
			if (area.contains(unit.x, unit.y)) {
				offsets.each(offset -> {
					Draw.rect(unit.type.fullIcon, unit.x + offset.x, unit.y + offset.y, unit.rotation - 90f);
					Drawf.light(unit.x + offset.x, unit.y + offset.y, unit.type.lightRadius, unit.type.lightColor, unit.type.lightOpacity);
				});
			}
		}
	}
	public static class Portal extends TriggerBox {
		public Rect area = new Rect();
		public float x, y, endX, endY;
		public Vec2 offset = new Vec2();

		@Override
		public void act(Posc unit) {
			area.setCenter(x, y);
			if (area.contains(unit.x(), unit.y())) {
				unit.trns(endX - x, endY - y);
				Core.camera.position.add(endX - x, endY - y);
			}
		}
	}
	public static class Zoom extends TriggerBox {
		public Rect area = new Rect();
		public float zoom = 0.667f;

		@Override public void act(Posc pos) {
			if (area.contains(pos.x(), pos.y())) renderer.maxZoom = renderer.minZoom = zoom;
		}
	}
	public static class Light extends TriggerBox {
		public Rect area = new Rect();
		public float x, y, radius;

		@Override public void draw(Unit unit) {
			if (area.contains(unit.x, unit.y)) Drawf.light(x, y, radius, unit.type.lightColor, unit.type.lightOpacity);
		}
	}
	public static class Timer extends TriggerBox {
		public Rect area = new Rect();
		public float time, lifetime;
		public Runnable run;

		@Override
		public void act(Posc pos) {
			if (area.contains(pos.x(), pos.y())) {
				time += Time.delta;
				if (time >= lifetime) run.run();
			}
		}
	}
}
