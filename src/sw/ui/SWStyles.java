package sw.ui;

import arc.scene.style.*;
import arc.scene.ui.Button.*;
import mindustry.gen.*;
import mindustry.graphics.*;

public class SWStyles {
	public static ButtonStyle
		sector,
		settingButton;

	public static void load() {
		sector = new ButtonStyle() {{
			down = checked = ((ScaledNinePatchDrawable) Tex.whitePane).tint(Pal.accent);
		}};
		
		settingButton = new ButtonStyle() {{
			up = Tex.buttonSideRight;
			down = Tex.buttonSideRightDown;
			over = Tex.buttonSideRightOver;
		}};
	}
}
