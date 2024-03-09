package sw.graphics.menus;

import arc.func.*;

public abstract class MenuBackground {
	public static MenuBackground[] menuBackgrounds = new MenuBackground[]{};

	public Cons<MenuBackground> init = menu -> {};

	public abstract void render();
}
