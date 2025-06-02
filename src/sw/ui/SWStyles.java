package sw.ui;

import arc.scene.ui.Button.*;
import mindustry.gen.*;

public class SWStyles {
	public static ButtonStyle settingButton;

	public static void load() {
		settingButton = new ButtonStyle() {{
			up = Tex.buttonSideRight;
			down = Tex.buttonSideRightDown;
			over = Tex.buttonSideRightOver;
		}};
	}
}
