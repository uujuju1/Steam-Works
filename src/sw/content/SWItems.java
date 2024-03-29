package sw.content;

import arc.graphics.Color;
import mindustry.type.Item;

public class SWItems {
  public static Item
    nickel, iron, arsenic,
    compound, bismuth, graphene,
    denseAlloy, thermite, scorch;

  public static void load() {
    nickel = new Item("nickel", Color.valueOf("CCD4D9"));
    iron = new Item("iron", Color.valueOf("646485"));
    arsenic = new Item("arsenic", Color.valueOf("857164"));

    compound = new Item("compound", Color.valueOf("515151"));
    bismuth = new Item("bismuth", Color.valueOf("97ABA4"));
    graphene = new Item("graphene", Color.valueOf("D0D3DF"));

    denseAlloy = new Item("dense-alloy", Color.valueOf("6E7080")) {{
      cost = 1f;
    }};
    thermite = new Item("thermite", Color.valueOf("6B7580")) {{
      flammability = explosiveness = 0.8f;
    }};
    scorch = new Item("scorch", Color.valueOf("3A2828"));
  }
}
