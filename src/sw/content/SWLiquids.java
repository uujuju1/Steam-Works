package sw.content;

import mindustry.type.*;

import static arc.graphics.Color.*;

public class SWLiquids {
  public static Liquid steam, solvent, fungi, chloro;

  public static void load() {
    steam = new Liquid("steam", lightGray) {{
      gas = true;
    }};
    solvent = new Liquid("solvent", valueOf("98ADC1"));
    fungi = new Liquid("fungi", valueOf("91DE9B")) {{
      gas = true;
      flammability = 1f;
      explosiveness = 0.5f;
    }};
    chloro = new Liquid("chloro", valueOf("C7CBB2"));
  }
}
