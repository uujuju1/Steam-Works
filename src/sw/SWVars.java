package sw;

import sw.content.*;

public class SWVars {
//    public static ModSettings settings;
//    public static ModNetClient netClient;
//    public static ModNetServer netServer;
//    public static ModUI modUI;
//    public static ModLogic logic;

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
        SWUnitTypes.load();
        SWBlocks.load();
        SWPlanets.load();
        SWTechTree.load();
    }
}
