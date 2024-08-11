package sw.world.meta;

import mindustry.world.meta.*;

public class SWStat {
  public static final StatCat gas = new StatCat("sw-gas");

  public static final StatUnit
  gasSecond = new StatUnit("sw-gas-second"),
  gasUnit = new StatUnit("sw-gas-unit"),
  pressureUnit = new StatUnit("sw-pressure-unit");

  public static final Stat
  consumeGas = new Stat("sw-consume-gas", gas),
  gasCapacity = new Stat("sw-gas-capacity", gas),
  gasTranfer = new Stat("sw-gas-transfer", gas),
  outputGas = new Stat("sw-output-gas", gas),
  maxPressure = new Stat("sw-max-pressure", gas);

  public static final Stat recipes = new Stat("sw-recipes");
}
