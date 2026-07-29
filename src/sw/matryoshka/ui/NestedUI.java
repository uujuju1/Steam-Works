package sw.matryoshka.ui;

import arc.*;
import arc.scene.ui.ImageButton.*;
import arc.scene.ui.layout.*;
import mindustry.gen.*;
import sw.matryoshka.*;

public class NestedUI extends WidgetGroup {
	public Runnable closeListener;

	public void build(NestedLogic logic) {
		Core.scene.add(this);
		setFillParent(true);
		visible = false;
		closeListener = logic::disable;

		// close button
		fill(t -> {
			t.bottom().right();
			t.button(Icon.cancel, new ImageButtonStyle() {{
				up = Tex.buttonSideLeft;
				down = Tex.buttonSideLeftDown;
				over = Tex.buttonSideLeftOver;
			}}, closeListener).width(300f);
		});
	}
}
