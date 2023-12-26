package sw.world.interfaces;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import mindustry.gen.*;
import sw.world.graph.*;
import sw.world.meta.*;
import sw.world.modules.*;

import static sw.util.SWDraw.*;

public interface HasHeat extends Buildingc{
  HeatModule heat();
  HeatConfig heatC();
  default HeatGraphDeprecated hGraph() {
    return heat().graph;
  }
  default Building asBuild() {
    return (Building) this;
  }

  default float temperature() {return heat().heat;}
  default float fraction() {return Mathf.map(temperature(), heatC().minHeat, heatC().maxHeat, 0, 1);}
  default float fractionNeg() {return Mathf.map(temperature(), 0, heatC().maxHeat, 0, 1);}

  default void addHeat(float amount) {
    heat().addHeat(amount);
  }
  default void setHeat(float amount) {
    heat().setHeat(amount);
  }
  default void transferHeat(HasHeat to, float amount) {
    addHeat(-amount);
    to.addHeat(amount);
  }

  default void drawHeat(TextureRegion region) {
    Draw.color(heatPal);
    Draw.alpha(fractionNeg());
    Draw.rect(region, x(), y(), 0);
    Draw.color();
  }

  default Seq<Building> heatProximity() {
    Seq<Building> out = new Seq<>();
    for (Building next : proximity()) if (next instanceof HasHeat) out.add(next);
    return out;
  }

  default boolean acceptsHeat(HasHeat from, float amount) {return heatC().acceptHeat;}
  default boolean outputsHeat(HasHeat from, float amount) {return heatC().outputHeat;}
  default boolean overflows(float amount) {return temperature() + amount > heatC().maxHeat;}
  default boolean overflows() {return overflows(0);}
}
