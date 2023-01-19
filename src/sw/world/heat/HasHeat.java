package sw.world.heat;

import arc.struct.Seq;
import mindustry.gen.Building;
import sw.util.SWMath;
import sw.world.modules.HeatModule;

public interface HasHeat {
  HeatModule module();

  default void transferHeat(HeatModule to) {
    float amount = SWMath.heatTransferDelta(0.2f, module().heat, to.heat, true);
    module().subHeat(amount);
    to.addHeat(amount);
  }
  default Seq<HasHeat> nextBuilds(Building from) {
    Seq<HasHeat> out = new Seq<>();
    if (!(from instanceof HasHeat)) return out;
    for (Building build : from.proximity) if (build instanceof HasHeat next) out.add(next);
    return out;
  }
}
