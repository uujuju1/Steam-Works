package sw.content;

import mindustry.type.*;

import static arc.graphics.Color.*;

public class SWItems {
  public static Item
    iron, aluminium, coke,

  compound, denseAlloy, thermite, oxycarbide, steel,

  nickel, meteorite,
    chalk, soda,
   residue;

  public static void load() {
    nickel = new Item("nickel", valueOf("CCD4D9")) {{
      hardness = 1;
    }};
    meteorite = new Item("meteorite", valueOf("E4CAAC"));
    chalk = new Item("chalk", valueOf("E2E2E8")) {{
      cost = 2f;
    }};
    soda = new Item("soda", valueOf("C9C3E4"));
    residue = new Item("residue", valueOf("1C1C1C"));


    aluminium = new Item("aluminium", valueOf("1C1C1C"));
    steel = new Item("steel", valueOf("1C1C1C"));
    oxycarbide = new Item("oxycarbide", valueOf("1C1C1C"));
    coke = new Item("coke", valueOf("1C1C1C"));
    denseAlloy = new Item("dense-alloy", valueOf("6E7080")) {{
      cost = 1f;
    }};
    thermite = new Item("thermite", valueOf("6B7580")) {{
      cost = 2f;
      flammability = explosiveness = 0.8f;
    }};
    compound = new Item("compound", valueOf("515151")) {{
      cost = 1f;
    }};
    iron = new Item("iron", valueOf("646485")) {{
      hardness = 2;
    }};
  }
}
