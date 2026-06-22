package sw.content;

import mindustry.type.*;

import static arc.graphics.Color.*;

public class SWLiquids {
  public static Liquid steam, solvent, gas;

  public static void load() {
    solvent = new Liquid("solvent", valueOf("98ADC1"));
    steam = new Liquid("steam", lightGray) {{
      gas = true;
    }};
    gas = new Liquid("gas", valueOf("E3D8B6")) {{
      gas = true;
    }};
  }
}
