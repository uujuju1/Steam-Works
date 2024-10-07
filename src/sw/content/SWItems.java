package sw.content;

import mindustry.type.*;

import static arc.graphics.Color.*;

public class SWItems {
  public static Item
    nickel, iron, meteorite,
    compound, chalk, soda,
    denseAlloy, thermite, residue;

  public static void load() {
    nickel = new Item("nickel", valueOf("CCD4D9")) {{
      hardness = 1;
    }};
    iron = new Item("iron", valueOf("646485")) {{
      hardness = 2;
    }};
    meteorite = new Item("meteorite", valueOf("E4CAAC"));

    compound = new Item("compound", valueOf("515151")) {{
      cost = 1f;
    }};
    chalk = new Item("chalk", valueOf("E2E2E8")) {{
      cost = 2f;
    }};
    soda = new Item("soda", valueOf("C9C3E4"));

    denseAlloy = new Item("dense-alloy", valueOf("6E7080")) {{
      cost = 1f;
    }};
    thermite = new Item("thermite", valueOf("6B7580")) {{
      cost = 2f;
      flammability = explosiveness = 0.8f;
    }};
    residue = new Item("residue", valueOf("1C1C1C"));
  }
}
