package sw.world.heat;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.struct.Seq;
import mindustry.gen.Building;
import sw.SWVars;
import sw.util.SWMath;
import sw.world.modules.HeatModule;

import static sw.util.SWDraw.heatPal;

public interface HasHeat {
  HeatModule module();
  HeatBlockI type();
  default HeatConfig heatC() {
    if (type().heatConfig() == SWVars.baseConfig) throw new IllegalArgumentException("use copy()");
    return type().heatConfig();
  }

  default float heat() {return module().heat;}
  default float fraction() {return Mathf.map(heat(), heatC().minHeat, heatC().maxHeat, 0, 1);}
  default float fractionNeg() {return Mathf.map(heat(), 0, heatC().maxHeat, 0, 1);}

  default void addHeat(float amount) {module().addHeat(amount);}
  default void subHeat(float amount) {module().subHeat(amount);}
  default void setHeat(float amount) {module().setHeat(amount);}
  default void transferHeat(HasHeat to, float amount) {subHeat(amount); to.addHeat(amount);}
  default void transferHeat(HasHeat to) {transferHeat(to, SWMath.heatTransferDelta(heatC().heatEmissivity, heat(), to.heat(), true));}

  default void updateHeat(Building to) {
    if (overflows()) to.kill();
    if (heat() < heatC().minHeat) setHeat(heatC().minHeat);
    heatProximity(to).map(build -> (HasHeat) build).removeAll(build -> !build.acceptsHeat(this, 0)||build.heat() >= heat()).each(this::transferHeat);
    subHeat(heatC().heatLoss * (fractionNeg() * 4));
  }

  default void drawHeat(TextureRegion region, Building build) {
    Draw.color(heatPal);
    Draw.alpha(fractionNeg());
    Draw.rect(region, build.x, build.y, 0);
    Draw.color();
  }

  default Seq<Building> connections(Building build) {
    Seq<Building> out = new Seq<>();
    for (int i = 0; i < 4; i++) if (build.nearby(i) instanceof HasHeat) out.add(build.nearby(i));
    return out;
  }
  default Seq<Building> heatProximity(Building build) {
    Seq<Building> out = new Seq<>();
    for (Building next : build.proximity) if (next instanceof HasHeat) out.add(next);
    return out;
  }

  default boolean acceptsHeat(HasHeat from, float amount) {return heatC().acceptHeat;}
  default boolean outputsHeat(HasHeat from, float amount) {return heatC().outputHeat;}
  default boolean overflows(float amount) {return heat() + amount > heatC().maxHeat;}
  default boolean overflows() {return overflows(0);}
}
