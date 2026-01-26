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
    spinResistance = new Stat("sw-spin-resistance", spin),
		maxFriction = new Stat("sw-max-friction", spin),
		weight = new Stat("sw-weight", spin);
  //endregion

  // region Units
	public static final StatUnit spinMinute = new StatUnit("sw-spin-minute");
	public static final StatUnit force = new StatUnit("sw-force");
	public static final StatUnit mass = new StatUnit("sw-mass");
  // endregion
}
