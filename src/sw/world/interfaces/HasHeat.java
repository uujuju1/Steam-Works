package sw.world.interfaces;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import mindustry.gen.*;
import sw.util.*;
import sw.world.meta.*;
import sw.world.modules.*;

import static sw.util.SWDraw.*;

public interface HasHeat {
  HeatModule heat();
  HeatConfig heatC();

  default float temperature() {return heat().heat;}
  default float fraction() {return Mathf.map(temperature(), heatC().minHeat, heatC().maxHeat, 0, 1);}
  default float fractionNeg() {return Mathf.map(temperature(), 0, heatC().maxHeat, 0, 1);}

  default void addHeat(float amount) {
    heat().addHeat(amount);}
  default void subHeat(float amount) {
    heat().subHeat(amount);}
  default void setHeat(float amount) {
    heat().setHeat(amount);}
  default void transferHeat(HasHeat to, float amount) {subHeat(amount); to.addHeat(amount);}
  default void transferHeat(HasHeat to) {transferHeat(to, SWMath.heatTransferDelta(heatC().heatEmissivity, temperature(), to.temperature(), true));}

  default void updateHeat(Building to) {
    if (overflows()) to.kill();
    if (temperature() < heatC().minHeat) setHeat(heatC().minHeat);
    heatProximity(to).map(build -> (HasHeat) build).removeAll(build -> !build.acceptsHeat(this, 0)||build.temperature() >= temperature()).each(this::transferHeat);
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
  default boolean overflows(float amount) {return temperature() + amount > heatC().maxHeat;}
  default boolean overflows() {return overflows(0);}
}
