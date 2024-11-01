package sw;

import arc.*;
import arc.assets.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.ctype.*;
import mindustry.ui.fragments.*;
import sw.audio.*;
import sw.content.*;
import sw.core.*;
import sw.dream.*;
import sw.graphics.*;
import sw.ui.*;
import sw.world.*;

import java.util.*;

public class SWVars implements Loadable {
	public static Seq<FluidArea> fluidAreas = new Seq<>();

	public static EnvProcess envProcess;

	public static boolean showSectorLaunchDialog = true;

	public static float maxRatio = 65536f;
	public static float minRatio = 1f/65536f;

  public static void init() {
		dev();

		if (!Vars.headless) {
			SWStyles.load();
			ModSettings.load();
			envProcess = new EnvProcess();
			SWShaders.load();
			SWUI.load();
		}
	}
	/** code to erase unlocked progress on this mod */
	public static void clearUnlockModContent() {
		SWUI.techtreeDialog.clearTree();
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

				let constr = Class.forName("rhino.NativeJavaPackage").getDeclaredConstructor(java.lang.Boolean.TYPE, java.lang.String, ClassLoader);
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
		Vars.mods.getScripts().runConsole("importModClass(\"sw.dream\")");
		Vars.mods.getScripts().runConsole("importModClass(\"sw.dream.events\")");
		Vars.mods.getScripts().runConsole("importModClass(\"sw.maps\")");
		Vars.mods.getScripts().runConsole("importModClass(\"sw.maps.generators\")");
		Vars.mods.getScripts().runConsole("importModClass(\"sw.math\")");
		Vars.mods.getScripts().runConsole("importModClass(\"sw.graphics\")");
		Vars.mods.getScripts().runConsole("importModClass(\"sw.util\")");
		Vars.mods.getScripts().runConsole("importModClass(\"sw.world.graph\")");
	}

	/**This is where you initialize your content lists. But do not forget about correct order.
	 *  correct order:
	 *  ModItems.load()
	 *  ModStatusEffects.load()
	 *  ModLiquids.load()
	 *  ModBullets.load()
	 *  ModUnitTypes.load()
	 *  ModBlocks.load()
	 *  ModPlanets.load()
	 *  ModSectorPresets.load()
	 *  ModTechTree.load()
	**/
	public static void loadContent() {
		ModSounds.load();
		SWItems.load();
		SWLiquids.load();
		SWWeathers.load();
		SWUnitTypes.load();
		SWBlocks.load();
		DreamContent.load();
		SWPlanets.load();
		SWSectorPresets.load();
		SWTechTree.load();
	}

	public static SWMenuRenderer getMenuRenderer() {
		try{
			return Reflect.get(MenuFragment.class, Vars.ui.menufrag, "renderer");
		}catch(Exception ex){
			Log.err("Failed to return renderer", ex);
			return new SWMenuRenderer();
		}
	}
}
