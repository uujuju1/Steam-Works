package sw.core;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import arc.util.noise.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.graphics.*;
import mindustry.world.*;
import sw.content.*;

import static mindustry.Vars.*;

public class SWMenuRenderer extends MenuRenderer {
	private int cacheFloor, cacheWall;
	private final int w = !mobile ? 100 : 60, h = !mobile ? 50 : 40;
	private CacheBatch batch;
	private FrameBuffer shadowMask;
	private Camera camera = new Camera();
	private Mat mat = new Mat();

	public SWMenuRenderer() {
		Time.mark();
		swGenerate();
		cache();
		Log.info("Menu generated in @", Time.elapsed());
	}

	private void swGenerate() {
		world.setGenerating(true);
		Tiles tiles = world.resize(w, h);
		shadowMask = new FrameBuffer(w, h);
		for(int x = 0; x < w; x++){
			for(int y = 0; y < h; y++){
				Block floor = Blocks.metalFloor;
				Block ore = Blocks.air;
				Block wall = Blocks.air;

				int s = Mathf.random(100000);
				if (x%4 == 0 || (y - 1)%4 == 0 && y - 1 > 0) floor = Blocks.metalFloor2;
				if (x%4 == 0 && (y - 1)%4 == 0 && y - 1 > 0) floor = Blocks.metalFloor3;
				if (Simplex.noise2d(
					s,
					3,
					0.5,
					1/20.0,
					x, y
				) > 0.5 && floor == Blocks.metalFloor) floor = Blocks.metalFloorDamaged;
				if (Simplex.noise2d(
					s,
					3,
					0.5,
					1/10.0,
					x, y
				) < 0.4 && (x%4 == 0 || y%4 == 0)) wall = Blocks.darkMetal;

				Tile tile;
				tiles.set(x, y, (tile = new CachedTile()));
				tile.x = (short)x;
				tile.y = (short)y;
				tile.setFloor(floor.asFloor());
				tile.setBlock(wall);
				tile.setOverlay(ore);
			}
		}

		world.tile(w/2, h/2).setBlock(SWBlocks.omniBelt, Team.sharded);

		world.setGenerating(false);
	}
	private void cache() {
		Draw.proj().setOrtho(0, 0, shadowMask.getWidth(), shadowMask.getHeight());
		shadowMask.begin(Color.clear);
		Draw.color(Color.black);

		for(Tile tile : world.tiles){
			if(tile.block() != Blocks.air){
				Fill.rect(tile.x + 0.5f, tile.y + 0.5f, 1, 1);
			}
		}

		Draw.color();
		shadowMask.end();

		Batch prev = Core.batch;
		Core.batch = batch = new CacheBatch(new SpriteCache(w * h * 6, false));
		batch.beginCache();
		for (Tile tile : world.tiles) tile.floor().drawBase(tile);
		for (Tile tile : world.tiles) tile.overlay().drawBase(tile);
		cacheFloor = batch.endCache();
		batch.beginCache();
		for (Tile tile : world.tiles) {
			if (tile.build == null) tile.block().drawBase(tile);
		}
		cacheWall = batch.endCache();
		Core.batch = prev;
	}

	@Override
	public void render() {
		float scaling = Math.max(
			Scl.scl(4f),
			Math.max(
				Core.graphics.getWidth() / ((w - 1f) * tilesize),
				Core.graphics.getHeight() / ((h - 1f) * tilesize)
			)
		);
		camera.position.set(w * tilesize / 2f, h * tilesize / 2f);
		camera.resize(
			Core.graphics.getWidth() / scaling,
			Core.graphics.getHeight() / scaling
		);

		mat.set(Draw.proj());
		Draw.flush();
		Draw.proj(camera);
		batch.setProjection(camera.mat);
		batch.beginDraw();
		batch.drawCache(cacheFloor);
		batch.endDraw();
		Draw.color();
		Draw.rect(Draw.wrap(shadowMask.getTexture()),
			w * tilesize / 2f - 4f, h * tilesize / 2f - 4f,
			w * tilesize, -h * tilesize
		);
		Draw.flush();

		batch.beginDraw();
		batch.drawCache(cacheWall);
		batch.endDraw();

		float x = w/2 * 8f, y = h/2 * 8f;

		Draw.rect(SWBlocks.omniBelt.region, x, y);

		Lines.stroke(6, Color.valueOf("6B5A55"));
		Lines.poly(x, y, 32, 48f);
		Lines.stroke(Lines.getStroke() / 3, Color.valueOf("BEADA7"));
		Lines.poly(x, y, 32, 48f);
		Draw.color(Color.valueOf("A6918A"));


		Vec2 vector = Tmp.v1.set(0, 0);

		for(int i = 0; i < 40; i += 2){
			vector.set(48f, 0f).rotate(360f / 40f * i + 90f - Time.time);
			float x1 = vector.x;
			float y1 = vector.y;

			vector.set(48f, 0f).rotate(360f / 40f * (i + 1) + 90f - Time.time);

			Lines.line(x1 + x, y1 + y, vector.x + x, vector.y + y);
		}

		Draw.proj(mat);
	}
}
