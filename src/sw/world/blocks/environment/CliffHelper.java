package sw.world.blocks.environment;

import mindustry.graphics.*;
import mindustry.world.*;

public class CliffHelper extends Block {
	public CliffHelper(String name) {
		super(name);
		breakable = alwaysReplace = false;
		solid = true;
		cacheLayer = CacheLayer.walls;
		fillsTile = false;
		hasShadow = false;
	}
}
