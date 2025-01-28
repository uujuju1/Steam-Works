package sw.world.meta;

import mindustry.world.meta.*;

public class SWStat {
  // region Categories
  public static final StatCat spin = new StatCat("sw-spin");
  //endregion

  // region Stats
  public static final Stat
    spinRequirement = new Stat("sw-spin-requirement", spin),
    spinOutput = new Stat("sw-spin-output", spin),
    spinOutputForce = new Stat("sw-spin-output-force", spin),
    spinResistance = new Stat("sw-spin-resistance", spin);

  public static final Stat recipes = new Stat("sw-recipes");
  //endregion

  // region Units
  public static final StatUnit spinMinuteSecond = new StatUnit("sw-spin-minute-second");
	public static final StatUnit spinMinute = new StatUnit("sw-spin-minute");
  // endregion
}
