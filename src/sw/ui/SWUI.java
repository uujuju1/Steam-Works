package sw.ui;

import mindustry.*;
import sw.ui.dialog.*;
import sw.ui.fragment.*;

public class SWUI {
	public static TechtreeDialog techtreeDialog;
	public static SectorLaunchDialog sectorLaunchDialog;

	public static CliffPlacerFragment cliffPlacerFragment;

	public static void load() {
		techtreeDialog = new TechtreeDialog();
		sectorLaunchDialog = new SectorLaunchDialog();
		cliffPlacerFragment = new CliffPlacerFragment().build(Vars.ui.hudGroup);
	}
}
