package sw.content;

import mindustry.*;
import mindustry.type.*;
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

		Vars.content.blocks().each(b -> b.minfo != null && b.minfo.mod != null && b.minfo.mod.name.equals("sw") && b.category == Category.power, b -> b.databaseTag = "sw-torque");
	}
}
