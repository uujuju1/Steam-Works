package sw.core;

import arc.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;
import sw.*;
import sw.content.*;
import sw.ui.*;

import static mindustry.Vars.*;

public class ModSettings {
  public static BaseDialog gamedata, graphics;

  public static void load() {
    gamedata = new BaseDialog("@settings.sw-moddata") {{
      addCloseButton();

      cont.table(Tex.button, cat -> {
        cat.button(
          "@settings.clearresearch",
          Icon.trash,
          Styles.flatt,
          Vars.iconMed,
          () -> Vars.ui.showConfirm("@settings.sw-clearresearch-confirm", SWVars::clearUnlockModContent)
        ).growX().marginLeft(8).height(50).row();
        cat.button(
          "@settings.clearcampaignsaves",
          Icon.trash,
          Styles.flatt,
          Vars.iconMed,
          () -> Vars.ui.showConfirm("@settings.sw-clearcampaignsaves-confirm", () -> {
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
          () -> Vars.ui.showConfirm("@settings.sw-resethints-confirm", EventHints::resetHints)
        ).growX().marginLeft(8).height(50).row();
      }).width(400f).row();
    }};
    graphics = new BaseDialog("@settings.graphics") {{
      addCloseButton();

      cont.table(settings -> {
        settings.left();
        settings.check(
          "@settings.sw-menu-enabled",
          Core.settings.getBool("sw-menu-enabled", true),
          check -> Core.settings.put("sw-menu-enabled", check)
        )
          .left().pad(1f)
          .update(check -> check.setChecked(Core.settings.getBool("sw-menu-enabled", true)))
          .tooltip("@settings.sw-menu-enabled-description");
        
        settings.row();
        
        settings.check(
          "@settings.sw-menu-override",
          Core.settings.getBool("sw-menu-override", true),
          check -> Core.settings.put("sw-menu-override", check)
        )
          .left().pad(1f)
          .update(check -> check.setChecked(Core.settings.getBool("sw-menu-override", true)))
          .tooltip("@settings.sw-menu-override-description");
        
        settings.row();
        
        settings.check(
          "@settings.sw-hide-maps",
          Core.settings.getBool("sw-hide-maps", true),
          check -> Core.settings.put("sw-hide-maps", check)
        )
          .left().pad(1f)
          .update(check -> check.setChecked(Core.settings.getBool("sw-hide-maps", true)))
          .tooltip("@settings.sw-hide-maps-description");
      }).width(400f);

      buttons.button("@settings.reset", () -> {
        Core.settings.put("sw-menu-enabled", true);
        Core.settings.put("sw-menu-override", true);
      }).size(210f, 64f).pad(6f);
    }};
    Vars.ui.settings.addCategory(Core.bundle.get("sw-settings"), "sw-setting-category", table -> {
      table.table(Tex.button, t -> {
        t.button(
          "@settings.sw-moddata",
          Icon.save,
          Styles.flatt,
          iconMed,
          () -> gamedata.show()
        ).growX().marginLeft(8f).height(50f).row();
        t.button(
          "@settings.graphics",
          Icon.image,
          Styles.flatt,
          iconMed,
          () -> graphics.show()
        ).growX().minWidth(400f).marginLeft(8f).height(50f).row();
      });
//      table.pref(new CheckSetting("sw-menu-enabled", true, bool -> Core.settings.put("sw-menu-enabled", bool)) {{
//        title = Core.bundle.get("sw-menu-enabled");
//        description = Core.bundle.get("sw-menu-enabled-description");
//      }});
    });
  }
}
