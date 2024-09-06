package sw.world.blocks.environment;

import mindustry.world.*;
import mindustry.world.blocks.environment.*;

public class Limiter extends StaticWall {
	public Limiter(String name) {
		super(name);
		variants = 0;
		hasShadow = false;
	}

	@Override public void drawBase(Tile tile) {
		tile.floor().drawBase(tile);
	}

	@Override
	public int minimapColor(Tile tile) {
		return tile.floor().minimapColor(tile);
	}
}
