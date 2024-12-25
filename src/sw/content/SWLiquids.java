package sw.content;

import mindustry.type.*;

import static arc.graphics.Color.*;

public class SWLiquids {
  public static Liquid steam, solvent, fluorane, primordialSoup;

  public static void load() {
    solvent = new Liquid("solvent", valueOf("98ADC1"));
    primordialSoup = new Liquid("primordial-soup", valueOf("98ADC1"));
    steam = new Liquid("steam", lightGray) {{
      gas = true;
    }};
    fluorane = new Liquid("fluorane", valueOf("E3D8B6")) {{
      gas = true;
    }};
  }
}
