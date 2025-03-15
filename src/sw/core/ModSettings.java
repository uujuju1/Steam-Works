package sw.core;

import arc.*;
import arc.scene.ui.layout.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;
import mindustry.ui.dialogs.SettingsMenuDialog.*;
import mindustry.ui.dialogs.SettingsMenuDialog.SettingsTable.*;
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
        ).update(check -> check.setChecked(Core.settings.getBool("sw-menu-enabled", true)));
      }).width(400f);

      buttons.button("@settings.reset", () -> {
        Core.settings.put("sw-menu-enabled", true);
      }).size(210f, 64f).pad(6f);
    }};
    Vars.ui.settings.addCategory(Core.bundle.get("sw-settings"), "sw-setting-category", table -> {
      table.pref(new TableSetting("sw-categories", new Table(Tex.button, t -> {
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
      })));
//      table.pref(new CheckSetting("sw-menu-enabled", true, bool -> Core.settings.put("sw-menu-enabled", bool)) {{
//        title = Core.bundle.get("sw-menu-enabled");
//        description = Core.bundle.get("sw-menu-enabled-description");
//      }});
    });
  }

  static class TableSetting extends Setting {
    Table table;

    public TableSetting(String name, Table table) {
      super(name);
      this.table = table;
    }

    @Override
    public void add(SettingsTable s) {
      s.add(table).growX();
      addDesc(table);
      s.row();
    }
  }

  static class ButtonSetting extends Setting {
    String name;
    Runnable listener;

    public ButtonSetting(String name, Runnable listener){
      super(name);
      this.name = name;
      this.listener = listener;
    }

    @Override
    public void add(SettingsTable table) {
      table.button(b -> b.add(Core.bundle.get(name, name)), SWStyles.settingButton, listener).margin(14).width(240f).pad(6f);
      table.row();
    }
  }
}
