package sw.ui;

import arc.graphics.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.Button.*;
import arc.scene.ui.Slider.*;
import arc.scene.ui.TextField.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;

public class SWStyles {
	public static TextureRegionDrawable whiteui = (TextureRegionDrawable) Tex.whiteui;

	public static ButtonStyle
		sector,
		settingButton;

	public static TextFieldStyle invisibleField;

	public static SliderStyle smallSlider;

	public static void load() {
		sector = new ButtonStyle() {{
			down = checked = ((ScaledNinePatchDrawable) Tex.whitePane).tint(Pal.accent);
		}};
		
		settingButton = new ButtonStyle() {{
			up = Tex.buttonSideRight;
			down = Tex.buttonSideRightDown;
			over = Tex.buttonSideRightOver;
		}};

		invisibleField = new TextField.TextFieldStyle() {{
			font = Fonts.def;
			fontColor = Color.white;

			cursor = Tex.cursor;
		}};

		smallSlider = new Slider.SliderStyle() {{
			background = Tex.sliderBack;
			knob = Tex.buttonSelect;
		}};
	}
}
