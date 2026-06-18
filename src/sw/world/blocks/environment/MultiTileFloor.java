package sw.world.blocks.environment;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import mindustry.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;

public class MultiTileFloor extends Floor {
	public Floor base;

	public int extension = 3;

	public Point2[] offsets;

	public MultiTileFloor(String name, Floor base) {
		super(name);

		this.base = base;
		blendGroup = base;
	}

	public boolean checkAdjacent(Tile tile) {
		for (Point2 offset : offsets) {
			Tile next = tile.nearby(offset.x, offset.y);

			if (next == null || next.floor() != this) return false;
		}
		return true;
	}

	@Override
	public void drawBase(Tile tile) {
		base.drawMain(tile);

		drawMain(tile);


	}

	@Override
	public void drawMain(Tile tile) {
		if(checkAdjacent(tile)){
			Draw.rect(variantRegions[Mathf.randomSeed(tile.pos(), 0, Math.max(0, variantRegions.length - 1))], tile.worldx() - extension * Vars.tilesize / 2f + Vars.tilesize / 2f, tile.worldy() - extension * Vars.tilesize / 2f + Vars.tilesize / 2f);
		}
	}

	@Override
	public void init() {
		super.init();

		offsets = new Point2[extension * extension];
		for (int i = 0; i < extension; i++) {
			for (int j = 0; j < extension; j++) {
				offsets[i * extension + j] = new Point2(-j, -i);
			}
		}
	}
}
