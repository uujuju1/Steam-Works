package sw.content;

import mindustry.type.*;

import static arc.graphics.Color.*;

public class SWItems {
  public static Item
    verdigris, iron, aluminium, coke,
    thermite, bloom, compound;

  public static void load() {
    verdigris = new Item("verdigris", valueOf("40916C")) {{
      hardness = 1;
    }};
    iron = new Item("iron", valueOf("646485")) {{
      hardness = 2;
      cost = 2f;
    }};
    aluminium = new Item("aluminium", valueOf("8F858C")) {{
      cost = 3f;
      hardness = 3;
    }};
    
    coke = new Item("coke", valueOf("B8B8B8")) {{
      buildable = false;
      flammability = explosiveness = 0.5f;
    }};
    thermite = new Item("thermite", valueOf("6C5656")) {{
      buildable = false;
      flammability = explosiveness = 0.8f;
    }};
    bloom = new Item("bloom", valueOf("5F595D")) {{
      cost = 4f;
    }};

    compound = new Item("compound", valueOf("515151")) {{
      cost = 1f;
    }};
  }
}
