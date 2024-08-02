package sw.ui;

import arc.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import sw.world.blocks.sandbox.ResourceSource.*;

public class SWTables {
	static ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle() {{
		checked = down = Styles.flatOver;
	}};
	static TextureRegionDrawable whiteui = (TextureRegionDrawable) Tex.whiteui;

	public static void resourceSourceSelector(Table cont, Config def, Runnable run) {
		cont.table(content -> {
			content.add(Core.bundle.get("category.items")).color(Pal.accent).padBottom(10f);
			content.add();
			content.add(Core.bundle.get("category.liquids")).color(Pal.accent).padBottom(10f).row();

			content.pane(Styles.smallPane, items -> Vars.content.items().each(item -> {
				Button button = items.button(new TextureRegionDrawable(item.uiIcon), style, () -> {
					def.itemBits.flip(Vars.content.items().indexOf(item));
					run.run();
				}).tooltip(item.localizedName).size(32f).pad(2.5f).get();
				button.setChecked(def.itemBits.get(Vars.content.items().indexOf(item)));
				if ((Vars.content.items().indexOf(item) + 1) % 4 == 0) items.row();
      })).maxHeight(148f);
			content.image(whiteui).growY().padLeft(10f).padRight(10f);
			content.pane(Styles.smallPane, liquids -> Vars.content.liquids().each(liquid -> {
				Button button = liquids.button(new TextureRegionDrawable(liquid.uiIcon), style, () -> {
					def.liquidBits.flip(Vars.content.liquids().indexOf(liquid));
					run.run();
				}).tooltip(liquid.localizedName).size(32f).pad(2.5f).get();
				button.setChecked(def.liquidBits.get(Vars.content.liquids().indexOf(liquid)));
				if ((Vars.content.liquids().indexOf(liquid) + 1) % 4 == 0) liquids.row();
      })).maxHeight(148f);
		}).row();
		cont.image(whiteui).growX().padTop(10f).padBottom(10f).row();
		cont.table(tension -> {
			tension.add(Core.bundle.get("category.power") + ": ");
			tension.field(def.power + "", TextField.TextFieldFilter.floatsOnly, s -> {
				def.power = Strings.parseFloat(s, 0f);
				run.run();
			});
		}).row();
		cont.table(tension -> {
			tension.add(Core.bundle.get("category.heat") + ": ");
			tension.field(def.heat + "", TextField.TextFieldFilter.floatsOnly, s -> {
				def.heat = Strings.parseFloat(s, 0f);
				run.run();
			});
		});
		cont.margin(10f);
	}
}
