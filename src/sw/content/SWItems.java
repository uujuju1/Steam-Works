package sw.content;

import arc.graphics.Color;
import mindustry.type.Item;

public class SWItems {
    public static Item tin, compound, denseAlloy;

    public static void load() {
        tin = new Item("tin", Color.valueOf("CCD4D9"));
        compound = new Item("compound", Color.valueOf("515151"));
        denseAlloy = new Item("dense-alloy", Color.valueOf("6E7080"));
    }
}
