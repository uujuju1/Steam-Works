package sw;

import arc.*;
import arc.assets.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.ctype.*;
import sw.content.*;
import sw.core.*;
import sw.dream.*;
import sw.gen.*;
import sw.graphics.*;
import sw.ui.*;

import java.util.*;

public class SWVars implements Loadable {
	public static SWRenderer renderer;

	public static boolean showSectorLaunchDialog = true;
	
	public static boolean isMod = true;

  public static void init() {
		if (isMod) dev();

		if (!Vars.headless) {
			SWTex.load();
			SWStyles.load();
			ModSettings.load();
			renderer = new SWRenderer();
			renderer.init();
			SWUI.load();
		}
	}
	
	public static void clearTree() {
		clearBranch(SWPlanets.wendi.techTree);
	}
	public static void clearBranch(TechTree.TechNode root) {
		root.reset();
		root.content.clearUnlock();
		root.children.each(SWVars::clearBranch);
	}
	
	/** code to erase unlocked progress on this mod */
	public static void clearUnlockModContent() {
		clearTree();
		Core.settings.put("settings-sw-techtree-category", -1);
	}
	/**cheating privileges*/
	public static void unlockModContent() {
		Vars.content.each(content -> {
			if (content instanceof UnlockableContent c && content.minfo.mod != null) {
				if (Objects.equals(c.minfo.mod.name, Vars.mods.getMod("sw").name)) c.unlock();
			}
		});
	}

	/**
	 * makes it so that i can access things with console
	 */
	public static void dev() {
		Vars.mods.getScripts().runConsole("importPackage(Packages.rhino)");
		Vars.mods.getScripts().runConsole(
			"""
				function importModClass(name){

				let constr = java.lang.Class.forName("rhino.NativeJavaPackage").getDeclaredConstructor(java.lang.Boolean.TYPE, java.lang.String, java.lang.ClassLoader);
				constr.setAccessible(true);

				let p = constr.newInstance(true, name, Vars.mods.mainLoader());

				let scope = Reflect.get(Vars.mods.getScripts(), "scope");
				Reflect.invoke(ScriptableObject, p, "setParentScope", [scope], [Scriptable]);

				importPackage(p);\s

				}"""
		);
		Vars.mods.getScripts().runConsole("importModClass(\"sw\")");
		Vars.mods.getScripts().runConsole("importModClass(\"sw.content\")");
		Vars.mods.getScripts().runConsole("importModClass(\"sw.content.blocks\")");
		Vars.mods.getScripts().runConsole("importModClass(\"sw.ui\")");
		Vars.mods.getScripts().runConsole("importModClass(\"sw.audio\")");
		Vars.mods.getScripts().runConsole("importModClass(\"sw.maps\")");
		Vars.mods.getScripts().runConsole("importModClass(\"sw.maps.generators\")");
		Vars.mods.getScripts().runConsole("importModClass(\"sw.math\")");
		Vars.mods.getScripts().runConsole("importModClass(\"sw.graphics\")");
		Vars.mods.getScripts().runConsole("importModClass(\"sw.util\")");
		Vars.mods.getScripts().runConsole("importModClass(\"sw.world.graph\")");
	}

	/**This is where you initialize your content lists. But do not forget about correct order.
	 *  correct order:
	 *  <p>ModItems.load()
	 *  <p>ModStatusEffects.load()
	 *  <p>ModLiquids.load()
	 *  <p>ModBullets.load()
	 *  <p>ModUnitTypes.load()
	 *  <p>ModBlocks.load()
	 *  <p>ModPlanets.load()
	 *  <p>ModSectorPresets.load()
	 *  <p>ModTechTree.load()
	**/
	public static void loadContent() {
		SWSounds.load();
		SWItems.load();
		SWStatusEffects.load();
		SWLiquids.load();
		SWWeathers.load();
		SWUnitTypes.load();
		SWBlocks.load();
		DreamContent.load();
		SWPlanets.load();
		SWSectorPresets.load();
		SWTechTree.load();
	}
}
