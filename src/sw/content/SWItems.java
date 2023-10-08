package sw.content;

import arc.graphics.Color;
import mindustry.type.Item;

public class SWItems {
    public static Item nickel,
      compound, neodymium,
      denseAlloy, frozenMatter;

    public static void load() {
        nickel = new Item("nickel", Color.valueOf("CCD4D9"));
        compound = new Item("compound", Color.valueOf("515151"));
        neodymium = new Item("neodymium", Color.valueOf("6B7580")) {{
            flammability = explosiveness = 0.8f;
        }};
        denseAlloy = new Item("dense-alloy", Color.valueOf("6E7080")) {{
            cost = 1f;
        }};
        frozenMatter = new Item("frozen-matter", Color.valueOf("D0D3DF"));
    }
}
