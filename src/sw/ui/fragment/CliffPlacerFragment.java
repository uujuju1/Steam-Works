package sw.ui.fragment;

import arc.scene.*;
import arc.scene.event.*;
import arc.scene.ui.layout.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import sw.world.blocks.environment.*;

import static mindustry.Vars.*;

public class CliffPlacerFragment extends Table {
	Table layout;

	public CliffPlacerFragment() {
		setFillParent(true);
		visible(() -> Vars.ui.hudfrag.shown && Vars.state.isEditor() && Vars.state.isPlaying() && Vars.control.input.commandMode);
		touchable(() -> visible ? Touchable.enabled : Touchable.disabled);
		right();

		add(layout = new Table(Tex.buttonSideLeft, t -> {
			t.table(Tex.underlineOver, title -> {
				title.label(() -> "@ui.sw-cliff-placer").color(Pal.accent);
			}).growX().padBottom(10f).row();
			t.table(Styles.black3, buttons -> {
				buttons.button("@ui.sw-process-cliffs", Icon.play, Styles.nonet, iconSmall, SWCliff::processCliffs).growX().height(35f).pad(5f).row();
				buttons.button("@ui.sw-un-process-cliffs", Icon.undo, Styles.nonet, iconSmall, SWCliff::unProcessCliffs).growX().height(35f).pad(5f);
			}).margin(10f).growX();
		})).margin(10f);
	}

	public CliffPlacerFragment build(Group parent) {
		parent.addChildAt(0, this);
		return this;
	}
}
