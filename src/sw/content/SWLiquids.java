package sw.content;

import mindustry.type.*;

import static arc.graphics.Color.*;

public class SWLiquids {
  public static Liquid steam, butane;

  public static void load() {
    steam = new Liquid("steam", lightGray) {{
      gas = true;
    }};
    butane = new Liquid("butane", valueOf("91DE9B")) {{
      gas = true;
      flammability = 1f;
      explosiveness = 0.5f;
    }};
  }
}
