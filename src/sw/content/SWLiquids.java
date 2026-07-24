package sw.content;

import mindustry.type.*;

import static arc.graphics.Color.*;

public class SWLiquids {
  public static Liquid steam, solvent, gas;

  public static void load() {
    solvent = new Liquid("solvent", valueOf("98ADC1")) {
      @Override
      public boolean canExtinguish() {
        return false;
      }
    };
    steam = new Liquid("steam", lightGray) {{
      gas = true;

      temperature = 1f;
    }};
    gas = new Liquid("gas", valueOf("E3D8B6")) {{
      gas = true;

      flammability = 1.5f;
    }};
  }
}
