package sw.ui;

import sw.ui.dialog.*;

public class SWUI {
	public static TechTreeDialog techtreeDialog;
	public static SectorLaunchDialog sectorLaunchDialog;

	public static void load() {
		techtreeDialog = new TechTreeDialog();
		sectorLaunchDialog = new SectorLaunchDialog();
	}
}
