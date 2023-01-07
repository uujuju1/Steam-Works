package sw.content;

import arc.graphics.Color;
import mindustry.type.Liquid;

public class SWLiquids {
  public static Liquid steam;

  public static void load() {
    steam = new Liquid("steam", Color.lightGray) {{
      gas = true;
    }};
  }
}
