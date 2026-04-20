package sw.ui;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.math.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import sw.ui.elements.*;
import sw.world.blocks.sandbox.ResourceSource.*;

public class SWTables {
	static ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle() {{
		checked = down = Styles.flatOver;
	}};

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
			content.image(Tex.whiteui).growY().padLeft(10f).padRight(10f);
			content.pane(Styles.smallPane, liquids -> Vars.content.liquids().each(liquid -> {
				Button button = liquids.button(new TextureRegionDrawable(liquid.uiIcon), style, () -> {
					def.liquidBits.flip(Vars.content.liquids().indexOf(liquid));
					run.run();
				}).tooltip(liquid.localizedName).size(32f).pad(2.5f).get();
				button.setChecked(def.liquidBits.get(Vars.content.liquids().indexOf(liquid)));
				if ((Vars.content.liquids().indexOf(liquid) + 1) % 4 == 0) liquids.row();
      })).maxHeight(148f);
		}).row();
		cont.image(Tex.whiteui).growX().padTop(10f).padBottom(10f).row();
		cont.table(t -> {
			t.add(Core.bundle.get("category.power") + ": ");
			t.field(def.power + "", TextField.TextFieldFilter.floatsOnly, s -> {
				def.power = Strings.parseFloat(s, 0f);
				run.run();
			});
		}).row();
		cont.table(t -> {
			t.add(Core.bundle.get("bar.heat") + ": ");
			t.field(def.heat + "", TextField.TextFieldFilter.floatsOnly, s -> {
				def.heat = Strings.parseFloat(s, 0f);
				run.run();
			});
		});
		cont.margin(10f);
	}

	public static void buildFloatSlider(Table table, String name, Floatc setter, Floatp getter) {
		TextField field = new TextField(Strings.fixed(getter.get(), 0));
		field.setFilter(TextField.TextFieldFilter.floatsOnly);
		field.changed(() -> setter.get(field.getText().isEmpty() ? 0f : Float.parseFloat(field.getText())));
		field.setStyle(SWStyles.invisibleField);

		CenterSlider slider = table.add(new CenterSlider(8f, 1f, value -> {
			float delta = value == 0 ? 0f : Mathf.pow(2f, Math.abs(value) - 1f) * Mathf.sign(value);
			setter.get(Mathf.maxZero(getter.get() + delta));
		})).minWidth(300f).growX().get();

		slider.update(() -> {
			if (slider.isDragging()) {
				field.setProgrammaticChangeEvents(false);
				field.setText(Strings.fixed(Mathf.maxZero(getter.get() + (slider.getValue() == 0 ? 0f : Mathf.pow(2f, Math.abs(slider.getValue()) - 1f)) * Mathf.sign(slider.getValue())), 0));
				field.setProgrammaticChangeEvents(true);
			} else {
				slider.setValue(slider.getValue() * 0.49f);
			}
		});

		slider.setStyle(SWStyles.smallSlider);

		table.row();
		table.table(below -> {
			below.add(name).padRight(10f).color(Color.lightGray);

			below.add(field);
		}).left().padTop(5f).row();
	}
}
