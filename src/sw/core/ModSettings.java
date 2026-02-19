package sw.core;

import arc.*;
import arc.graphics.*;
import arc.scene.style.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;
import mindustry.ui.dialogs.SettingsMenuDialog.*;
import sw.*;
import sw.content.*;
import sw.gen.*;
import sw.ui.*;

import static mindustry.Vars.*;

public class ModSettings {
  public static BaseDialog game, gamedata, graphics;

  private static Table settings;
  public static Seq<SettingsTable> categories = new Seq<>();

  public static void buildSettings() {
    // game
    categories.add(new SettingsTable() {{
      checkPref("sw-hide-maps", false);
      checkPref("sw-replace-music", true);
    }});
    // graphics
    categories.add(new SettingsTable() {{
      checkPref("sw-menu-enabled", true);
      checkPref("sw-menu-override", true);
      checkPref("sw-ui-filter", true);
    }});
  }

  public static void load() {
    game = new BaseDialog("@settings.game") {{
//      cont.remove();
//      buttons.remove();
//      titleTable.remove();

//      row();
      cont.pane(settings = new Table()).grow().top();
//      row();
//      add(buttons).fillX();

      addCloseButton();
    }};
    gamedata = new BaseDialog("@settings.sw-moddata") {{
      addCloseButton();

      cont.table(Tex.button, cat -> {
        cat.button(
          "@settings.clearresearch",
          Icon.trash,
          Styles.flatt,
          Vars.iconMed,
          () -> Vars.ui.showConfirm("@settings.sw-clearresearch.confirm", SWVars::clearUnlockModContent)
        ).growX().marginLeft(8).height(50).row();
        cat.button(
          "@settings.clearcampaignsaves",
          Icon.trash,
          Styles.flatt,
          Vars.iconMed,
          () -> Vars.ui.showConfirm("@settings.sw-clearcampaignsaves.confirm", () -> {
            SWPlanets.wendi.sectors.each(sector -> {
              if (sector.hasSave()) {
                sector.save.delete();
                sector.save = null;
              }
            });
          })
        ).growX().marginLeft(8).height(50).row();
        cat.button(
          "@settings.sw-resethints",
          Icon.trash,
          Styles.flatt,
          Vars.iconMed,
          () -> Vars.ui.showConfirm("@settings.sw-resethints.confirm", EventHints::resetHints)
        ).growX().marginLeft(8).height(50).row();
      }).width(400f).row();
    }};
//    graphics = new BaseDialog("@settings.graphics") {{
//      addCloseButton();
//
//      cont.table(settings -> {
//        settings.left();
//        settings.check(
//          "@settings.sw-menu-enabled",
//          Core.settings.getBool("sw-menu-enabled", true),
//          check -> Core.settings.put("sw-menu-enabled", check)
//        )
//          .left().pad(1f)
//          .update(check -> check.setChecked(Core.settings.getBool("sw-menu-enabled", true)))
//          .tooltip("@settings.sw-menu-enabled-description");
//
//        settings.row();
//
//        settings.check(
//          "@settings.sw-menu-override",
//          Core.settings.getBool("sw-menu-override", true),
//          check -> Core.settings.put("sw-menu-override", check)
//        )
//          .left().pad(1f)
//          .update(check -> check.setChecked(Core.settings.getBool("sw-menu-override", true)))
//          .tooltip("@settings.sw-menu-override-description");
//
//        settings.row();
//
//        settings.check(
//          "@settings.sw-hide-maps",
//          Core.settings.getBool("sw-hide-maps", true),
//          check -> Core.settings.put("sw-hide-maps", check)
//        )
//          .left().pad(1f)
//          .update(check -> check.setChecked(Core.settings.getBool("sw-hide-maps", true)))
//          .tooltip("@settings.sw-hide-maps-description");
//        settings.row();
//
//        settings.check(
//          "@settings.sw-ui-filter",
//          Core.settings.getBool("sw-ui-filter", true),
//          check -> {
//            Core.settings.put("sw-ui-filter", check);
//            ((TextureRegionDrawable) Tex.whiteui).getRegion().texture.setFilter(check ? Texture.TextureFilter.linear : Texture.TextureFilter.nearest);
//          }
//        )
//          .left().pad(1f)
//          .update(check -> check.setChecked(Core.settings.getBool("sw-ui-filter", true)))
//          .tooltip("@settings.sw-ui-filter-description");
//      }).width(400f);
//
//      buttons.button("@settings.reset", () -> {
//        Core.settings.put("sw-menu-enabled", true);
//        Core.settings.put("sw-menu-override", true);
//      }).size(210f, 64f).pad(6f);
//    }};
    Vars.ui.settings.addCategory(Core.bundle.get("sw-settings"), "sw-setting-category", table -> {
      table.image(SWTex.settingsTitle).padBottom(10f).row();
      table.table(Tex.button, t -> {
        t.button(
          "@settings.game",
          Icon.settings,
          Styles.flatt,
          iconMed,
          () -> show(0)
        ).growX().minWidth(300f).marginLeft(8f).height(50f).row();
        t.button(
          "@settings.graphics",
          Icon.image,
          Styles.flatt,
          iconMed,
          () -> show(1)
        ).growX().minWidth(300f).marginLeft(8f).height(50f).row();
//        t.button(
//          "@settings.controls",
//          Icon.move,
//          Styles.flatt,
//          iconMed,
//          () -> {}
//        ).growX().minWidth(300f).marginLeft(8f).height(50f).row();
        t.button(
          "@settings.sw-moddata",
          Icon.save,
          Styles.flatt,
          iconMed,
          () -> gamedata.show()
        ).growX().minWidth(300f).marginLeft(8f).height(50f).row();
      });
    });
    buildSettings();
  }

  public static void show(int cat) {
    if (cat < 0 || cat >= categories.size) {
      Log.err("Category out of Bounds");
      return;
    }
    settings.clearChildren();
    settings.add(categories.get(cat));
    categories.get(cat).rebuild();
    game.show();
  }
}
