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

//	public static void buildSandboxSelector(Table cont, Config con, Runnable run) {
//		cont.table(Styles.black6, menu -> {
//			menu.table(Styles.flatDown, resources -> {
//				resources.pane(Styles.smallPane, items -> {
//					for (int i = 0; i < Vars.content.items().size; i++) {
//						final int id = i;
//						Button button = items.button(new TextureRegionDrawable(Vars.content.items().get(i).uiIcon), style, () -> {
//							con.itemBits.flip(id);
//							run.run();
//						}).size(36f).margin(2f).grow().tooltip(Vars.content.items().get(i).localizedName).get();
//						button.setChecked(con.itemBits.get(i));
//						if ((i + 1) % 4 == 0) items.row();
//					}
//        }).height(144f).padRight(10f);
//				resources.pane(Styles.smallPane, liquids -> {
//					for (int i = 0; i < Vars.content.liquids().size; i++) {
//						final int id = i;
//						Button button = liquids.button(new TextureRegionDrawable(Vars.content.liquids().get(i).uiIcon), style, () -> {
//							con.liquidBits.flip(id);
//							run.run();
//						}).size(36).margin(2).grow().tooltip(Vars.content.items().get(i).localizedName).get();
//						button.setChecked(con.liquidBits.get(i));
//						if ((i + 1) % 4 == 0) liquids.row();
//					}
//        }).height(144f);
//      }).margin(20f).row();
//			menu.table(heat -> {
//				heat.add("Heat: ");
//				heat.field("" + con.heatValue, floatsOnly, s -> {
//					con.heatValue = Strings.parseFloat(s, 0);
//					run.run();
//				}).growX();
//      }).growX().row();
//			menu.table(staticTension -> {
//				staticTension.add("Static Tension: ");
//				staticTension.field("" + con.sTension, floatsOnly, s -> {
//					con.sTension = Strings.parseFloat(s, 0);
//					run.run();
//				}).growX();
//      }).growX().row();
//			menu.table(mobileTension -> {
//				mobileTension.add("Mobile Tension: ");
//				mobileTension.field("" + con.mTension, floatsOnly, s -> {
//					con.mTension = Strings.parseFloat(s, 0);
//					run.run();
//				}).growX();
//      }).growX();
//		}).margin(20f);
//	}
	public static void resourceSourceSelector(Table cont, Config def, Runnable run) {
		cont.table(content -> {
			content.add(Core.bundle.get("ui.sw-allsource.items")).color(Pal.accent).padBottom(10f);
			content.add();
			content.add(Core.bundle.get("ui.sw-allsource.liquids")).color(Pal.accent).padBottom(10f).row();

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
					def.itemBits.flip(Vars.content.liquids().indexOf(liquid));
					run.run();
				}).tooltip(liquid.localizedName).size(32f).pad(2.5f).get();
				button.setChecked(def.itemBits.get(Vars.content.liquids().indexOf(liquid)));
				if ((Vars.content.liquids().indexOf(liquid) + 1) % 4 == 0) liquids.row();
      })).maxHeight(148f);
		}).row();
		cont.image(whiteui).growX().padTop(10f).padBottom(10f).row();
		cont.table(tension -> {
			tension.add(Core.bundle.get("ui.sw-allsource.s-tension") + ": ");
			tension.field("0.0", TextField.TextFieldFilter.floatsOnly, s -> {
				def.sTension = Strings.parseFloat(s, 0f);
				run.run();
			});
		}).row();
		cont.table(tension -> {
			tension.add(Core.bundle.get("ui.sw-allsource.m-tension") + ": ");
			tension.field("0.0", TextField.TextFieldFilter.floatsOnly, s -> {
				def.mTension = Strings.parseFloat(s, 0f);
				run.run();
			});
		});
		cont.margin(10f);
	}
}
