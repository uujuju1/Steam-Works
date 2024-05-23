package sw.ui;

import arc.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.*;
import mindustry.ui.*;
import sw.world.blocks.sandbox.ResourceSource.*;

import static arc.scene.ui.TextField.TextFieldFilter.*;

public class SWTables {
	static ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle() {{
		checked = down = Styles.flatOver;
	}};

	public static void buildSandboxSelector(Table cont, Config con, Runnable run) {
		cont.table(Styles.black6, menu -> {
			menu.table(Styles.flatDown, resources -> {
				resources.pane(Styles.smallPane, items -> {
					for (int i = 0; i < Vars.content.items().size; i++) {
						final int id = i;
						Button button = items.button(new TextureRegionDrawable(Vars.content.items().get(i).uiIcon), style, () -> {
							con.itemBits.flip(id);
							run.run();
						}).size(36f).margin(2f).grow().tooltip(Vars.content.items().get(i).localizedName).get();
						button.setChecked(con.itemBits.get(i));
						if ((i + 1) % 4 == 0) items.row();
					}
        }).height(144f).padRight(10f);
				resources.pane(Styles.smallPane, liquids -> {
					for (int i = 0; i < Vars.content.liquids().size; i++) {
						final int id = i;
						Button button = liquids.button(new TextureRegionDrawable(Vars.content.liquids().get(i).uiIcon), style, () -> {
							con.liquidBits.flip(id);
							run.run();
						}).size(36).margin(2).grow().tooltip(Vars.content.liquids().get(i).localizedName).get();
						button.setChecked(con.liquidBits.get(i));
						if ((i + 1) % 4 == 0) liquids.row();
					}
        }).height(144f);
      }).margin(20f).row();
			menu.table(heat -> {
				heat.add(Core.bundle.get("sw-heat") + ": ");
				heat.field("" + con.heatValue, floatsOnly, s -> {
					con.heatValue = Strings.parseFloat(s, 0);
					run.run();
				}).growX();
      }).growX().row();
			menu.table(staticTension -> {
				staticTension.add(Core.bundle.get("sw-static-tension") + ": ");
				staticTension.field("" + con.sTension, floatsOnly, s -> {
					con.sTension = Strings.parseFloat(s, 0);
					run.run();
				}).growX();
      }).growX().row();
			menu.table(mobileTension -> {
				mobileTension.add(Core.bundle.get("sw-mobile-tension") + ": ");
				mobileTension.field("" + con.mTension, floatsOnly, s -> {
					con.mTension = Strings.parseFloat(s, 0);
					run.run();
				}).growX();
      }).growX();
		}).margin(20f);
	}
}
