package sw.matryoshka.ui;

import arc.*;
import arc.scene.ui.layout.*;
import mindustry.gen.*;
import sw.matryoshka.*;

public class NestedUI extends WidgetGroup {
	public Runnable closeListener;

	public void build(NestedLogic logic) {
		Core.scene.add(this);
		visible = false;
		closeListener = logic::disable;

		fill(t -> {
			t.bottom().right();
			t.button(Icon.cancel, closeListener);
		});
	}
}
