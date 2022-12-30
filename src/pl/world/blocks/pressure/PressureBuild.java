package pl.world.blocks.pressure;

import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.Log;
import mindustry.gen.Building;
import mindustry.world.Block;

public interface PressureBuild<T extends Block> {
  default T getBlock() {
    return null;
  }
  default PressureModule getModule() {
    return null;
  }
  default Pressure getPressureC() {
    return getBlock() instanceof PressureBlock ? ((PressureBlock) getBlock()).getPressure() : null;
  }

  default boolean acceptsPressure(Building build, float amount) {
    return getPressureC().acceptsPressure;
  }
  default boolean outputsPressure(Building build, float amount) {
    if (build instanceof PressureBuild && getPressureC().outputsPressure) return ((PressureBuild) build).getPressure() < getPressure();
    return getPressureC().outputsPressure;
  }

  default float pressureMap() {
    return Mathf.map(getPressure(), getPressureC().minPressure, getPressureC().maxPressure, 0, 1);
  }
  default float getPressure() {return getModule().pressure;}

  default void updatePressure(Building to) {
    if (getPressureC().overflows) {
      if (getPressure() < getPressureC().minPressure || getPressure() > getPressureC().maxPressure) {
        Log.info("pressureDeath: " + getPressure(), "pressureDeath: " + getPressure());
        to.kill();
      }
    } else {
      getModule().setPressure(Mathf.clamp(getPressure(), getPressureC().minPressure, getPressureC().maxPressure));
    }
    for (Building build : getPressureBuilds(to)) {
      float value = Mathf.clamp((getPressure() - ((PressureBuild) build).getPressure()) * getPressureC().transferRate, getPressureC().minPressure, getPressureC().maxPressure)/10f;
      if (outputsPressure(build, value) && ((PressureBuild) build).acceptsPressure(to, value)) {
        getModule().transfer(((PressureBuild) build).getModule(), value, getPressureC().internalSize / ((PressureBuild) build).getPressureC().internalSize);
      }
    }
    getModule().setPressure(getPressure() * getPressureC().lossRate);
  }

  default Seq<Building> getPressureBuilds(Building with) {
    Seq<Building> builds = new Seq<>();
    for (Building build : with.proximity) if (build instanceof PressureBuild) builds.add(build);
    return builds;
  }

}
