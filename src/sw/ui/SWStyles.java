package sw.ui;

import arc.*;
import arc.scene.style.*;
import arc.scene.ui.Button.*;
import mindustry.gen.*;

public class SWStyles {
	public static ButtonStyle settingButton;

	public static Drawable inventoryClear, treeBorder;

	public static void load() {
		settingButton = new ButtonStyle() {{
			up = Tex.buttonSideRight;
			down = Tex.buttonSideRightDown;
			over = Tex.buttonSideRightOver;
		}};

		inventoryClear = Core.atlas.drawable("sw-inventory-clear");
		treeBorder = Core.atlas.drawable("sw-tree-border");
	}
}
