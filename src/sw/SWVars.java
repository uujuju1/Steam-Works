package sw;

import arc.assets.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.ctype.*;
import mindustry.type.*;
import mindustry.ui.fragments.*;
import sw.audio.*;
import sw.content.*;
import sw.dream.*;
import sw.graphics.*;
import sw.world.*;

import java.util.*;

public class SWVars implements Loadable {
	public static Seq<FluidArea> fluidAreas = new Seq<>();
	public static SectorPreset lastSectorPreset;

  public static void init() {
    // glenn stuff
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
    Vars.mods.getScripts().runConsole("importModClass(\"sw.graphics\")");
    Vars.mods.getScripts().runConsole("importModClass(\"sw.util\")");
    Vars.mods.getScripts().runConsole("importModClass(\"sw.world.graph\")");
	}
	/** code to erase unlocked progress on this mod */
	public static void clearUnlockModContent() {
		Vars.content.each(content -> {
			if (content instanceof UnlockableContent c && content.minfo.mod != null) {
				if (Objects.equals(c.minfo.mod.name, Vars.mods.getMod("sw").name)) c.clearUnlock();
			}
		});
	}
	/**cheating privileges*/
	public static void unlockModContent() {
		Vars.content.each(content -> {
			if (content instanceof UnlockableContent c && content.minfo.mod != null) {
				if (Objects.equals(c.minfo.mod.name, Vars.mods.getMod("sw").name)) c.unlock();
			}
		});
	}

	public static boolean isOnSector(Sector sector) {
		return Vars.state.getSector() == sector;
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
//		SWEntityMapping.load();
		SWUnitTypes.load();
		SWBlocks.load();
		SWPlanets.load();
		SWSectorPresets.load();
		SWTechTree.load();

		DreamContent.load();
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
