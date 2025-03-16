package sw.graphics;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.math.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import arc.util.noise.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.world.*;
import sw.content.blocks.*;

public class SWMenuRenderer extends MenuRenderer {
	private final int width = !Vars.mobile ? 100 : 60, height = !Vars.mobile ? 50 : 40;
	private final float darkness = 0.3f;
	private final Camera cam = new Camera();
	private final Mat mat = new Mat();

	private int floorBatch, blockBatch;
	private CacheBatch tileBatch;
	private FrameBuffer shadowBatch;

	private static Block floor, overlay, wall;

	public SWMenuRenderer() {
		long millis = Time.millis();
		genEnv();
		createBatch();
		Log.debug("SW Menu Renderer Generated in @ ms", Time.timeSinceMillis(millis));
	}

	private void createBatch() {
		tileBatch = new CacheBatch(new SpriteCache(width * height * 6, false));
		shadowBatch = new FrameBuffer(width, height);

		Draw.proj().setOrtho(0, 0, width, height);

		shadowBatch.begin(Color.clear);
		Draw.color(Color.black, darkness);
		for (Tile tile : Vars.world.tiles) {
			if (tile.block() != Blocks.air) Fill.square(tile.x + 0.5f, tile.y + 0.5f, 1);
		}
		Draw.color();
		shadowBatch.end();

		Batch other = Core.batch;
		Core.batch = tileBatch;

		tileBatch.beginCache();
		for (Tile tile : Vars.world.tiles) {
			tile.floor().drawBase(tile);
		}
		for (Tile tile : Vars.world.tiles) {
			tile.overlay().drawBase(tile);
		}
		floorBatch = tileBatch.endCache();

		tileBatch.beginCache();
		for (Tile tile : Vars.world.tiles) {
			tile.block().drawBase(tile);
		}
		blockBatch = tileBatch.endCache();

		Core.batch = other;
	}

	@Override
	public void dispose() {
		tileBatch.dispose();
		shadowBatch.dispose();
	}

	private void genEnv() {
		Vars.world.setGenerating(true);
		Vars.world.resize(width, height);

		Mathf.rand.setSeed((long) (Math.random() * 100000));

		int offsetX = Mathf.random(0, 10);
		int offsetY = Mathf.random(0, 10);
		int seed = Mathf.random(0, 100);

		pass((x, y) -> {
			float noise = Simplex.noise2d(seed, 3, 0.6, 0.05, x, y);
			float noise2 = Simplex.noise2d(seed * 2, 7, 0.9, 0.05, x, y);
			floor = noise2 < 0.4f ? SWEnvironment.fissuredSouesite : SWEnvironment.souesite;
			overlay = Blocks.air;
			wall = noise > 0.6f ? SWEnvironment.souesiteWall : Blocks.air;

			if (((x + offsetX) % 10 < 2 || (y + offsetY) % 10 < 2) && Mathf.chance(0.5)) {
				floor = SWEnvironment.plate;
				if (wall != Blocks.air) wall = SWEnvironment.plateWall;
			}
		});
		Vars.world.setGenerating(false);
	}

	private void pass(Intc2 pos) {
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				Tile tile = Vars.world.tile(x, y);

				floor = tile.floor();
				overlay = tile.overlay();
				wall = tile.block();

				pos.get(x, y);

				tile.setFloor(floor.asFloor());
				tile.setOverlay(overlay);
				tile.setBlock(wall);
			}
		}
	}

	@Override
	public void render() {
		cam.position.set(width * Vars.tilesize / 2f, height * Vars.tilesize / 2f);
		float scaling = Math.max(Scl.scl(4f), Math.max(Core.graphics.getWidth() / ((width - 1f) * Vars.tilesize), Core.graphics.getHeight() / ((height - 1f) * Vars.tilesize)));
		cam.resize(
			Core.graphics.getWidth() / scaling,
			Core.graphics.getHeight() / scaling
		);

		mat.set(Draw.proj());
		Draw.flush();
		Draw.proj(cam);
		tileBatch.setProjection(cam.mat);

		tileBatch.beginDraw();
		tileBatch.drawCache(floorBatch);
		tileBatch.endDraw();

		Draw.color();
		Draw.rect(
			Draw.wrap(shadowBatch.getTexture()),
			width * Vars.tilesize/2f,
			height * Vars.tilesize/2f,
			width * Vars.tilesize,
			-height * Vars.tilesize
		);
		Draw.flush();

		tileBatch.beginDraw();
		tileBatch.drawCache(blockBatch);
		tileBatch.endDraw();

		Draw.proj(mat);

		Draw.proj(mat);
		Draw.color(0f, 0f, 0f,  darkness);
		Fill.crect(0, 0, Core.graphics.getWidth(), Core.graphics.getHeight());
		Draw.color();
	}
}
