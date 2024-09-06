package sw.ui;

import sw.ui.dialog.*;

public class SWUI {
	public static TechtreeDialog techtreeDialog;
	public static SectorLaunchDialog sectorLaunchDialog;

	public static void load() {
		techtreeDialog = new TechtreeDialog();
		sectorLaunchDialog = new SectorLaunchDialog();
	}
}
