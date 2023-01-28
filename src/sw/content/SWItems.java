package sw.content;

import arc.graphics.Color;
import mindustry.type.Item;

public class SWItems {
    public static Item nickel, compound, denseAlloy;

    public static void load() {
        nickel = new Item("nickel", Color.valueOf("CCD4D9"));
        compound = new Item("compound", Color.valueOf("515151"));
        denseAlloy = new Item("dense-alloy", Color.valueOf("6E7080")) {{
            cost = 1f;
        }};
    }
}
