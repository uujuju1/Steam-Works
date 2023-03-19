package sw.ui;

import arc.*;
import mindustry.*;
import mindustry.ui.dialogs.*;
import mindustry.ui.dialogs.SettingsMenuDialog.SettingsTable.*;
import sw.*;

public class ModSettings {
  public static void load() {
    Vars.ui.settings.addCategory(Core.bundle.get("sw-settings"), "sw-setting-category", table -> {
      table.pref(new ButtonSetting("Erase Mod progress", () -> Vars.ui.showConfirm(Core.bundle.get("sw-erase-confirm"), () -> SWVars.clearUnlockModContent())));
    });
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
    public void add(SettingsMenuDialog.SettingsTable table) {
      table.button(name, listener).margin(14).width(240f).pad(6f);
      table.row();
    }
  }
}
