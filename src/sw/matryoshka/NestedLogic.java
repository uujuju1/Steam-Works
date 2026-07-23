package sw.matryoshka;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import sw.graphics.*;
import sw.matryoshka.world.*;
import sw.matryoshka.world.Nesting.*;

public class NestedLogic implements ApplicationListener {
	public boolean shouldDraw;
	public boolean shouldUpdate;

	public Camera camera = new Camera();

	public FrameBuffer ambientOcclusion;

	public float scale = 10;

	public Seq<Nesting> active = new Seq<>();

	public void draw() {
		Draw.blit(SWShaders.hintBackgroundShader);

		camera.position.set(0, 0);
		camera.width = 1920f / scale;
		camera.height = 1080f / scale;
		camera.update();

		Draw.flush();
//		Camera oldCamera = Core.camera;
		Draw.proj(camera);
//		Core.camera = camera;
		Mat oldTrans = Tmp.m2.set(Draw.trans());

		active.each(nesting -> {
			var context = nesting.getContext();

//			camera.position.set(nesting.x, nesting.y);
//			camera.update();
			Draw.trans(Tmp.m1.idt().translate(nesting.x - nesting.world.unitWidth() / 2f + Vars.tilesize / 2f, nesting.y - nesting.world.unitHeight() / 2f + Vars.tilesize / 2f));

			context.begin();
			Draw.sort(true);
			drawNesting(nesting);
			Draw.flush();
			Draw.sort(false);
			context.end();
		});

		Draw.trans(oldTrans);
//		Draw.proj(oldCamera);
//		Core.camera = oldCamera;
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
	}

	@Override
	public void init() {
		ambientOcclusion = new FrameBuffer();
		Events.on(EventType.DisposeEvent.class, e -> {
			ambientOcclusion.dispose();
		});
	}

	@Override
	public void update() {
		if (shouldUpdate) updateLogic();
		if (shouldDraw) draw();
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

		// TODO run world logic here
		Groups.update();

		context.end();
	}

	public void run(Nesting nesting, Runnable code) {
		NestingContext context = nesting.getContext();

		context.begin();

		code.run();

		context.end();
	}
}
