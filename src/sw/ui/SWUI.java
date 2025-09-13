package sw.ui;

import mindustry.*;
import sw.ui.dialog.*;
import sw.ui.fragment.*;

public class SWUI {
	public static SectorLaunchDialog sectorLaunchDialog;
	
	public static SpinFragment spinFragment;

	public static void load() {
		sectorLaunchDialog = new SectorLaunchDialog();
		
		spinFragment = new SpinFragment().build(Vars.ui.hudGroup);
	}
}
