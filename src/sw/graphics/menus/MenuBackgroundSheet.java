package sw.graphics.menus;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.ui.layout.*;
import mindustry.content.*;
import mindustry.world.*;

import static mindustry.Vars.*;

public class MenuBackgroundSheet extends MenuBackground {
	private int cacheFloor;
	private final int width = !mobile ? 100 : 60, height = !mobile ? 50 : 40;
	private CacheBatch batch;
	private final Camera camera = new Camera();
	private final Mat mat = new Mat();

	public MenuBackgroundSheet() {
		init = menu -> {
			generate();
			cache();
		};
	}

	public void generate() {
		world.setGenerating(true);
		Tiles tiles = world.resize(width, height);
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				Block floor = Blocks.metalFloor;
				Block ore = Blocks.air;
				Block wall = Blocks.air;

				Tile tile;
				tiles.set(x, y, (tile = new CachedTile()));
				tile.x = (short)x;
				tile.y = (short)y;
				tile.setFloor(floor.asFloor());
				tile.setBlock(wall);
				tile.setOverlay(ore);
			}
		}
		world.setGenerating(false);
	}
	public void cache() {
		Batch prev = Core.batch;
		Core.batch = batch = new CacheBatch(new SpriteCache(width * height * 6, false));
		batch.beginCache();
		for (Tile tile : world.tiles) tile.floor().drawBase(tile);
		cacheFloor = batch.endCache();
		Core.batch = prev;
	}

	@Override
	public void render() {
		float scaling = Math.max(
			Scl.scl(4f),
			Math.max(
				Core.graphics.getWidth() / ((width - 1f) * tilesize),
				Core.graphics.getHeight() / ((height - 1f) * tilesize)
			)
		);
		camera.position.set(width * tilesize / 2f, height * tilesize / 2f);
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
		Draw.flush();

		Draw.proj(mat);
	}
}
