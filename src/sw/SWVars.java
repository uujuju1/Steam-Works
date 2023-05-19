package sw;

import mindustry.*;
import mindustry.ctype.*;
import sw.content.*;
import sw.entities.*;
import sw.ui.*;
import sw.world.meta.*;

public class SWVars {
    public static final float maxHeatGlow = 100;
    public static final HeatConfig baseConfig = new HeatConfig(-200, 2200, 0.4f, 0f, true, true);
    public static ModSettings settings = new ModSettings();
//    public static ModNetClient netClient;
//    public static ModNetServer netServer;
//    public static ModUI modUI;
//    public static ModLogic logic;

    /** code to erase unlocked progress on this content */
    public static void clearUnlockModContent() {
        Vars.content.each(content -> {
            if (content instanceof UnlockableContent c && content.minfo.mod != null) {
                if (c.minfo.mod.name == Vars.mods.getMod("sw").name) c.clearUnlock();
            }
        });
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
     * */
    public static void loadContent() {
        SWItems.load();
        SWLiquids.load();
        SWEntityMapping.load();
        SWUnitTypes.load();
        SWBlocks.load();
        SWSectorPresets.load();
        SWTechTree.load();
    }
}
