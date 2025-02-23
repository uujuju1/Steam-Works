package sw.content;

import sw.content.blocks.*;

public class SWBlocks {
	public static void load() {
		SWCrafting.load();
		SWDefense.load();
		SWDistribution.load();
		SWEnvironment.load();
		SWProduction.load();
		SWPower.load();
		SWSandbox.load();
		SWStorage.load();
		SWTurrets.load();
		SWUnits.load();
	}
}
