package sw.dream;

import arc.math.geom.*;
import arc.struct.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import sw.world.blocks.environment.*;

public class DreamContent {
	public static Block voidTile, voidTileWall, spikes;

	public static void load() {
		new Dream();
		voidTile = new Floor("void-tile", 0);
		voidTileWall =  new StaticWall("void-tile-wall") {{
			variants = 0;
		}};
		spikes = new Spike("spike") {{
			variants = 0;
			variantShapes = new ObjectMap[]{
				ObjectMap.of(
//					new Polygon(new float[] {-3.5f, 2f, 3.5f, -1f, -4f, -4f}), Pal.shadow,
					new Polygon(new float[] {-4f, -4f, 4f, -4f, 0f, 0f}), Pal.gray.cpy().mul(0.75f),
					new Polygon(new float[] {4f, -4f, 4f, 4f, 0f, 0f}), Pal.gray,
					new Polygon(new float[] {4f, 4f, -4f, 4f, 0f, 0f}), Pal.gray.cpy().mul(1.25f),
					new Polygon(new float[] {-4f, 4f, -4f, -4f, 0f, 0f, }), Pal.gray
//					new Polygon(new float[] {-1f, -3.5f, 3.5f, -1f, 3f, 9f}), Color.gray
				)
			};
		}};
	}
}
