package sw.ui.dialog;

import arc.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;
import mindustry.ui.fragments.MenuFragment.*;

public class SectorLaunchDialog extends BaseDialog {
	public SectorLaunchDialog() {
		super("@sector.view");
		shouldPause = true;
		
		closeOnBack();
		addToMenu();
		
		titleTable.remove();
		titleTable.button("@quit", Icon.left, new TextButton.TextButtonStyle() {{
			font = Fonts.def;
			up = Tex.buttonSideRightDown;
			down = Tex.buttonSideRightOver;
		}}, this::hide).size(210f, 48f);
		titleTable.setBackground(Styles.black6);
		titleTable.margin(10);
		
		cont.clear();
		cont.stack(
			new Table(t -> t.add(titleTable).growX()).top(),
			new Table(t -> t.add(buttons)).bottom().right()
		).grow();
	}
	
	public void addToMenu() {
		if (Vars.mobile || !Core.settings.getBool("sw-menu-override", true)) {
			Vars.ui.menufrag.addButton(new MenuButton("@planet.sw-wendi.name", Icon.terrain, () -> {
				if (Vars.mods.hasContentErrors()) {
					Vars.ui.showInfo("@mod.noerrorplay");
				} else show();
			}));
		} else {
			MenuButton old = Vars.ui.menufrag.desktopButtons.first().submenu.first();
			MenuButton newButton = new MenuButton(
				"@ui.sw-main-game",
				Icon.play, () -> {},
				old, new MenuButton("@planet.sw-wendi.name", Icon.terrain, () -> {
					if (Vars.mods.hasContentErrors()) {
						Vars.ui.showInfo("@mod.noerrorplay");
					} else show();
				})
			);
			Vars.ui.menufrag.desktopButtons.first().submenu.replace(old, newButton);
		}
	}
}
