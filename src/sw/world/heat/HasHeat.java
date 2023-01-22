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
    for (Building build : from.proximity) {

      if (build instanceof HasHeat next) {
        float amount = SWMath.heatTransferDelta(0.2f, module().heat, next.module().heat, true);
        if (next.acceptsHeat(this, amount) && outputsHeat(next, amount)) out.add(next);
      }
    }
    return out;
  }

//  only used for possible connection, won't change based on heat
  default boolean connects(HasHeat to) {return true;}

  default boolean acceptsHeat(HasHeat from, float amount) {return module().heat < from.module().heat;}
  default boolean outputsHeat(HasHeat to, float amount) {return to.module().heat < module().heat;}

  default boolean overflows(float amount) {return false;}
}
