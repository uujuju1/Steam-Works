package sw.world;

import arc.struct.*;
import mindustry.world.*;

/**
 * a class containing a custom shape in the map and the tiles on where this shape is
 */
public class MultiShape {
	public int layer;
	public MultiShapeBuild build = new MultiShapeBuild();
	public Seq<Tile> tiles = new Seq<>();

	/**
	 * @return true whenever the multi shape is present in one of the env layers
	 */
	public boolean isFloor() {
		return layer == 0;
	}
	public boolean isOverlay() {
		return layer == 1;
	}
	public boolean isBlock() {
		return layer == 2;
	}

	public float centerX() {
		return (tiles.min(Tile::worldx).worldx() + tiles.max(Tile::worldx).worldx())/2f;
	}
	public float centerY() {
		return (tiles.min(Tile::worldy).worldy() + tiles.max(Tile::worldy).worldy())/2f;
	}

	public static class MultiShapeBuild {
		public void update(MultiShape shape) {}

		public void draw(MultiShape shape) {}
	}
}
