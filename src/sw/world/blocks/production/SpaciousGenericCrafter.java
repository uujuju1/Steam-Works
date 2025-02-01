package sw.world.blocks.production;

import mindustry.*;
import mindustry.game.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;

public class SpaciousGenericCrafter extends GenericCrafter {
	public int radius = 5;

	public SpaciousGenericCrafter(String name) {
		super(name);
	}

	@Override
	public void drawOverlay(float x, float y, int rotation) {
		super.drawOverlay(x, y, rotation);
		Drawf.dashSquare(
			Pal.accent,
			x - offset,
			y - offset,
			radius * Vars.tilesize * 2f
		);
	}

	@Override
	public boolean canPlaceOn(Tile tile, Team team, int rotation) {
		for(int x = -radius; x <= radius; x++) {
			for(int y = -radius; y <= radius; y++) {
				if (tile.nearby(x, y) != null && tile.nearby(x, y).block() == this) return false;
			}
		}
		return super.canPlaceOn(tile, team, rotation);
	}
}
