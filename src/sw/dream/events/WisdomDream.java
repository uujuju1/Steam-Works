package sw.dream.events;

import arc.*;
import arc.audio.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import arc.util.pooling.*;
import mindustry.*;
import mindustry.audio.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.*;
import sw.audio.*;
import sw.content.*;
import sw.dream.*;

public class WisdomDream extends DreamEvent {
	public Block unlockBlock = SWBlocks.rebuilder;
	public Vec2 pos = new Vec2();
	public Sound loop = ModSounds.awareness;
	public SoundLoop soundLoop = new SoundLoop(ModSounds.awareness, 1f);
	public String text = "follow";
	public Font font = Fonts.tech;

	boolean sounds = true;

	@Override
	public void draw() {
		Draw.blend(Blending.additive);
		Draw.z(Layer.blockOver);
		for(Color color : new Color[]{Color.red, Color.green, Color.blue}){
			Draw.color(color);
			Tmp.v1.trns(Mathf.random(360f), Mathf.random(8f)).add(pos);
			Draw.rect(unlockBlock.uiIcon, Tmp.v1.x, Tmp.v1.y, Mathf.random(45f) - 45f / 2f);
		}
		Draw.blend();
		Draw.z(Layer.max);
		Draw.color(Color.white, Mathf.clamp(Mathf.map(
			Vars.player.unit().dst(pos),
			40, 200f,
			1, 0
		)));
		Draw.rect();
		drawText();
	}

	public void drawText() {
		if (Mathf.chance(0.1f)) {
			Vec2
				textPos = new Vec2().set(Core.camera.position),
				offset = new Vec2(Mathf.range(Core.camera.width/2f), Mathf.range(Core.camera.height/2f));

			GlyphLayout layout = Pools.obtain(GlyphLayout.class, GlyphLayout::new);
			boolean ints = font.usesIntegerPositions();
			font.setUseIntegerPositions(false);
			font.getData().setScale(5f / Vars.renderer.getDisplayScale());
			layout.setText(font, text);

			float dx = textPos.x + offset.x, dy = textPos.y + offset.y/ 2f + 3f;
			Draw.blend(Blending.additive);
			Draw.z(Layer.fogOfWar);
			font.setColor(Color.red);
			font.draw(text, dx + Mathf.range(8f), dy + Mathf.range(8f) + layout.height + 1f, Align.center);

			font.setColor(Color.green);
			font.draw(text, dx + Mathf.range(8f), dy + Mathf.range(8f) + layout.height + 1f, Align.center);

			font.setColor(Color.blue);
			font.draw(text, dx + Mathf.range(8f), dy + Mathf.range(8f) + layout.height + 1f, Align.center);
			Draw.blend();

			font.setUseIntegerPositions(ints);
			font.setColor(Color.white);
			font.getData().setScale(1f);
			Draw.reset();
			Pools.free(layout);
		}
	}

	@Override
	public void update() {
		Vars.control.sound.stop();
		soundLoop.update(pos.x, pos.y, sounds, Mathf.clamp(Mathf.map(
			Vars.player.unit().dst(pos),
			40, 200f,
			1, 0
		)));
		if (Vars.state.isMenu()) {
			unlockBlock.unlock();
			soundLoop.stop();
			DreamCore.instance.event(null);
		}
		if (Vars.state.isPaused()) return;
		Groups.unit.each(Unitc::isPlayer, u -> {
			if (u.dst(pos) > 40f) {
				u.impulse(Tmp.v1.trns(u.angleTo(pos), 50f));
			} else {
				unlockBlock.unlock();
				soundLoop.stop();
				DreamCore.instance.event(null);
			}
		});
	}
}
