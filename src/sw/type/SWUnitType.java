package sw.type;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.entities.part.*;
import mindustry.entities.units.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.meta.*;
import sw.entities.comp.*;
import sw.world.meta.*;
import sw.world.recipes.*;

import static mindustry.Vars.*;

public class SWUnitType extends UnitType {
  // Submarine stuff
  public boolean submerges = false;
  public float vulnerabilityTime = 60f;

  // Crafter unit stuff
  public GenericRecipe recipe;

  // shield unit stuff
  public UnitType shieldUnit;
  public DrawPart.PartProgress shieldProgress = DrawPart.PartProgress.warmup;
  public int shields = 5;

  public float shieldConstructTime = 120f;
  public float shieldSeparateRadius = -1f;

  public float shieldStartAng = 0f;
  public float shieldEndAng = 180f;
  public float shieldShootingStartAng = 0f;
  public float shieldShootingEndAng = 180f;

  public SWUnitType(String name) {
    super(name);
  }

  @Override
  public void init() {
    super.init();
    if (shieldSeparateRadius == -1) shieldSeparateRadius = hitSize;
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
    if (shieldUnit != null) {
      stats.add(SWStat.shield, table -> {
        table.row();
        table.table(Styles.grayPanel, t -> {
          if(shieldUnit.isBanned()){
            t.image(Icon.cancel).color(Pal.remove).size(40);
            return;
          }

          if(shieldUnit.unlockedNow()){
            t.image(shieldUnit.uiIcon).size(40).pad(10f).left().scaling(Scaling.fit);
            t.table(info -> {
              info.add(shieldUnit.localizedName).left();
            }).left();
          }else{
            t.image(Icon.lock).color(Pal.darkerGray).size(40);
          }
        }).growX().pad(5);
        table.row();
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
      Draw.mixcol(Color.darkGray, 0.6f * (1f - u.vulnerabilityFrame/vulnerabilityTime));
    } else {
      super.applyColor(unit);
    }
  }

  @Override
  public void draw(Unit unit) {
    super.draw(unit);
    if (unit instanceof ShieldedUnit u) drawShields(u);
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

  public void drawShields(ShieldedUnit u) {
    Draw.draw(Draw.z(), () -> {
      if (u.mounts.length > 0) {
        WeaponMount first = u.mounts[0];
        DrawPart.params.set(first.warmup, first.reload / weapons.first().reload, first.smoothReload, first.heat, first.recoil, first.charge, u.x, u.y, u.rotation);
      } else {
        DrawPart.params.set(0, 0, 0, 0, 0, 0, u.x, u.y, u.rotation);
      }
      for(int i = 0; i < shields; i++) {
        float ang = Mathf.lerp(
          shieldStartAng + (u.rotation() - 90f + shieldEndAng/(shields - 1f) * i),
          shieldShootingStartAng + (u.rotation() - 90f + shieldShootingEndAng /(shields - 1f) * i),
          shieldProgress.get(DrawPart.params)
        );
        Tmp.v1.trns(ang, shieldSeparateRadius).add(u);
        try {
          if (u.units.get(i).dead()) Drawf.construct(Tmp.v1.x, Tmp.v1.y, shieldUnit.fullIcon, Tmp.v1.angleTo(u) + 90, u.progress, 1f, u.progress * shieldConstructTime);
        } catch (IndexOutOfBoundsException e) {
          Drawf.construct(Tmp.v1.x, Tmp.v1.y, shieldUnit.fullIcon, Tmp.v1.angleTo(u) + 90, u.progress, 1f, u.progress * shieldConstructTime);
        }
      }
    });
  }
}
