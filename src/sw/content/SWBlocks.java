package sw.content;

import mindustry.world.*;
import sw.content.blocks.*;
import sw.world.blocks.sandbox.*;

public class SWBlocks {
	public static Block
		allSource;

	public static void load() {
		SWCrafting.load();
		SWDefense.load();
		SWDistribution.load();
		SWEnvironment.load();
		SWProduction.load();
		SWPower.load();
		SWStorage.load();
		SWTurrets.load();
		SWUnits.load();

		// region sandbox
		allSource = new ResourceSource("all-source") {{
			health = 2147483647;
		}};
		// endregion
	}
}
