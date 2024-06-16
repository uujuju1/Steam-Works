package sw.dream.event;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import sw.dream.*;

import static mindustry.Vars.*;

public class YggdrasilEvent extends DreamEvent {
	public Seq<TriggerBox> trigger = new Seq<>();

	@Override
	public void draw() {
		trigger.each(trigger -> trigger.draw(player.unit()));
	}

	@Override
	public void update() {
		trigger.each(trigger -> trigger.act(player.unit()));
	}

	abstract static class TriggerBox {
		public void act(Posc pos) {}

		public void draw(Unit unit) {

		}
	}

	public static class Portal extends TriggerBox {
		public Rect area = new Rect();
		public Vec2 offset = new Vec2();
		public float clipsize = 8;
		public boolean drawCopy = true;

		@Override
		public void act(Posc unit) {
			if (area.contains(unit.x(), unit.y())) {
				unit.trns(offset);
				Core.camera.position.add(offset);
			}
		}

		@Override
		public void draw(Unit unit) {
			if (visibleFrom(unit) && drawCopy) {
				Draw.rect(unit.type.fullIcon, unit.x, unit.y, unit.rotation - 90f);
				Tmp.v2.set(offset).scl(visibleFromStart(unit) ? 1f : -1f);
				Draw.rect(unit.type.fullIcon, unit.x + Tmp.v2.x, unit.y + Tmp.v2.y, unit.rotation - 90f);
				Drawf.light(unit.x + Tmp.v2.x, unit.y + Tmp.v2.y, unit.type.lightRadius, unit.type.lightColor, unit.type.lightOpacity);
			}
		}

		public boolean visibleFrom(Posc pos) {
			return visibleFromStart(pos) || visibleFromEnd(pos);
		}
		public boolean visibleFromStart(Posc pos) {
			return pos.dst(area.getCenter(Tmp.v1)) <= clipsize;
		}
		public boolean visibleFromEnd(Posc pos) {
			return pos.dst(area.getCenter(Tmp.v1).add(offset)) <= clipsize;
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
}
