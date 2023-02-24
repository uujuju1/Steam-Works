package sw.type;

import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.game.Team;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.type.ItemStack;
import mindustry.type.UnitType;
import mindustry.ui.ItemImage;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.meta.Stat;
import sw.entities.comp.SubmarineUnit;
import sw.world.recipes.GenericRecipe;

import static mindustry.Vars.world;

public class SWUnitType extends UnitType {
  // Submarine stuff
  public boolean submerges = false;
  public float vulnerabilityTime = 60f;

  // Crafter unit stuff
  public GenericRecipe recipe;

  public SWUnitType(String name) {
    super(name);
  }

  @Override
  public void setStats() {
    super.setStats();
    if (recipe != null) {
      stats.add(Stat.input, t -> {
        for (ItemStack stack : recipe.consumeItems) t.add(new ItemImage(stack)).pad(3f);
      });
      stats.add(Stat.output, t -> {
        for (ItemStack stack : recipe.outputItems) t.add(new ItemImage(stack)).pad(3f);
      });
    }
  }

  @Override
  public boolean targetable(Unit unit, Team from) {
    if (submerges && unit instanceof SubmarineUnit u) return !u.submerged();
    return super.targetable(unit, from);
  }

  @Override
  public void applyColor(Unit unit) {
    if (submerges && unit instanceof SubmarineUnit u) {
      Draw.mixcol(Tmp.c1.set(Vars.world.floorWorld(unit.x, unit.y).mapColor).mul(0.83f), 0.6f * (1f - u.vulnerabilityFrame/vulnerabilityTime));
    } else {
      super.applyColor(unit);
    }
  }

  @Override
  public void drawShadow(Unit unit) {
    if (submerges && unit instanceof SubmarineUnit u) {
      float e = Mathf.clamp(unit.elevation, shadowElevation, 1f) * shadowElevationScl * (1f - unit.drownTime) * (u.vulnerabilityFrame/vulnerabilityTime);
      float x = unit.x + shadowTX * e, y = unit.y + shadowTY * e;
      Floor floor = world.floorWorld(x, y);

      float dest = floor.canShadow ? 1f : 0f;
      //yes, this updates state in draw()... which isn't a problem, because I don't want it to be obvious anyway
      unit.shadowAlpha = unit.shadowAlpha < 0 ? dest : Mathf.approachDelta(unit.shadowAlpha, dest, 0.11f);
      Draw.color(Pal.shadow, Pal.shadow.a * unit.shadowAlpha);

      Draw.rect(shadowRegion, unit.x + shadowTX * e, unit.y + shadowTY * e, unit.rotation - 90);
      Draw.color();
    } else super.drawShadow(unit);
  }
}
