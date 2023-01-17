package sw.world.pressure;

import sw.util.SWMath;
import sw.world.modules.HeatModule;

public interface HasHeat {
  HeatModule module();

  default void transferHeat(HeatModule to) {
    float amount = SWMath.heatTransferDelta(0.2f, module().heat, to.heat, true);
    module().subHeat(amount);
    to.addHeat(amount);
  }
}
