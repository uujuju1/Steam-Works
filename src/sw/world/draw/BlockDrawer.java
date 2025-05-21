package sw.world.draw;

import mindustry.world.*;
import mindustry.world.draw.*;
import sw.gen.*;

/**
 * Since all other drawers are custom classes anyways, i can just make this be the base class for them
 */
public class BlockDrawer extends DrawBlock {
	public static Block loadBlock;
	
	@Override public void load(Block block) {
		loadBlock = block;
		SWContentRegionRegistry.load(this);
	}
}
