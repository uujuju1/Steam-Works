package sw.matryoshka;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.math.*;
import arc.scene.event.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import sw.graphics.*;
import sw.matryoshka.ui.*;
import sw.matryoshka.world.*;
import sw.matryoshka.world.Nesting.*;

/**
 * Handler for the ponder system. Updates and draws Nestings and the ponder ui.
 */
public class NestedLogic implements ApplicationListener {
	private boolean enabled;

	public boolean shouldDraw = true;
	public boolean shouldUpdate = true;

	public Camera camera = new Camera();
	public FrameBuffer ambientOcclusion;
	public float scale = 10;

	public NestedUI ui;

	public Seq<Nesting> active = new Seq<>();

	/**
	 * Properly disables the listener and enables the base ui.
	 */
	public void disable() {
		enabled = false;
		Vars.ui.hudGroup.touchable = Vars.ui.menuGroup.touchable = Touchable.childrenOnly;
		ui.updateVisibility();
	}

	public void draw() {
		Draw.blit(SWShaders.hintBackgroundShader);
		camera.width = 1920f / scale;
		camera.height = 1080f / scale;

		Draw.flush();
		Mat oldTrans = Tmp.m2.set(Draw.trans());
		Camera oldCamera = Core.camera;

		active.each(nesting -> {
			var context = nesting.getContext();

			camera.position.set(-nesting.x + nesting.world.unitWidth() / 2f - Vars.tilesize / 2f, -nesting.y + nesting.world.unitHeight() / 2f - Vars.tilesize / 2f);
			Core.camera = camera;
			camera.update();
			Draw.proj(camera);
//			Draw.trans(Tmp.m1.idt().translate(nesting.x - nesting.world.unitWidth() / 2f + Vars.tilesize / 2f, nesting.y - nesting.world.unitHeight() / 2f + Vars.tilesize / 2f));

			context.begin();
			Draw.sort(true);
			drawNesting(nesting);
			Draw.flush();
			Draw.sort(false);
			context.end();
			Draw.flush();
		});

		Draw.trans(oldTrans);
		Core.camera = oldCamera;

		Draw.proj(Core.scene.getCamera());
		ui.draw();
		Draw.flush();
	}
	public void drawNesting(Nesting nesting) {
		// border
		Draw.color(Pal.darkerMetal);
		Draw.z(Layer.min);
		Fill.crect(-Vars.tilesize / 2f - 2f, -Vars.tilesize / 2f - 2f, Vars.world.unitWidth() + 4f, Vars.world.unitHeight() + 4f);
		Draw.color();

		// floors
		Draw.draw(Layer.floor, () -> Vars.world.tiles.eachTile(tile -> {
			if (tile.floor() == Blocks.air) {
				Draw.color((tile.x + tile.y) % 2 == 0 ? Color.white : Color.lightGray);
				Fill.square(tile.drawx(), tile.drawy(), 4);
			} else tile.floor().drawBase(tile);
		}));

		// darkness
		Draw.proj(0, 0, Vars.world.width(), Vars.world.height());
		Tmp.m1.set(Draw.trans());
		Draw.trans(Draw.trans().idt());
		ambientOcclusion.resize(Vars.world.width(), Vars.world.height());
		ambientOcclusion.begin(Color.clear);
		Vars.world.tiles.eachTile(tile -> {
			if (tile.block().displayShadow(tile)) {
				Fill.rect(tile.x + 1, tile.y + 1, 1f, 1f);
			}
		});
		ambientOcclusion.end();
		Draw.proj(camera);
		Draw.trans(Tmp.m1);

		Draw.z(Layer.blockUnder - 1);
		Draw.color(Color.black, 0.3f);
		Draw.rect(Draw.wrap(ambientOcclusion.getTexture()),
			Vars.world.unitWidth() / 2f - Vars.tilesize / 2f,
			Vars.world.unitHeight() / 2f - Vars.tilesize / 2f,
			Vars.world.unitWidth(),
			-Vars.world.unitHeight()
		);
		Draw.color();
		Draw.flush();

		// blocks
		Draw.z(Layer.block);
		Vars.world.tiles.eachTile(tile -> {
			if (tile.build != null) {
				if (tile.build.block.drawCached) tile.build.drawCached();
				if (tile.build.block.drawDynamic) tile.build.draw();
			}
		});
		Draw.flush();

		// Drawc
		Groups.draw.each(Drawc::draw);

		// bloomed (assumes bloom settings are updated in Renderer)
		if (Vars.renderer.bloom != null) {
			Draw.draw(Layer.bullet - 0.02f, Vars.renderer.bloom::capture);
			Draw.draw(Layer.effect + 0.02f, Vars.renderer.bloom::render);
		}
	}

	/**
	 * Properly enables the listener and disables the base ui.
	 */
	public void enable() {
		enabled = true;
		Vars.ui.hudGroup.touchable = Vars.ui.menuGroup.touchable = Touchable.disabled;
		ui.updateVisibility();
	}

	@Override
	public void init() {
		if (!Vars.headless) {
			ambientOcclusion = new FrameBuffer();
			Events.on(EventType.DisposeEvent.class, e -> {
				ambientOcclusion.dispose();
			});
		}

		ui = new NestedUI();
		ui.touchablility = () -> enabled ? Touchable.childrenOnly : Touchable.disabled;
		ui.visibility = () -> enabled;
		ui.build(this);
	}

	/**
	 * Enables/disables this listener.
	 */
	public void toggle() {
		if (enabled) {
			disable();
		} else {
			enable();
		}
	}

	@Override
	public void update() {
		if (enabled) {
			if (shouldUpdate) updateLogic();
			if (shouldDraw) draw();
		}
	}

	public void updateLogic() {
		active.each(this::updateNesting);
	}

	/**
	 * Standard vanilla like updating loop for a Nesting.
	 */
	public void updateNesting(Nesting nesting) {
		NestingContext context = nesting.getContext();

		context.begin();

		Camera oldCamera = Core.camera;
		Core.camera = camera;
		Groups.update();
		Core.camera = oldCamera;

		context.end();
	}

	/**
	 * Runs code within the context of a Nesting.
	 */
	public void run(Nesting nesting, Runnable code) {
		NestingContext context = nesting.getContext();

		context.begin();

		code.run();

		context.end();
	}
}
