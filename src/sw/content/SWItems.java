package sw.content;

import arc.graphics.Color;
import mindustry.type.Item;

public class SWItems {
    public static Item tin, silver, denseAlloy;

    public static void load() {
        tin = new Item("tin", Color.valueOf("CCD4D9"));
        silver = new Item("silver", Color.valueOf("B0B4D6"));
        denseAlloy = new Item("dense-alloy", Color.valueOf("6E7080"));
    }
}
