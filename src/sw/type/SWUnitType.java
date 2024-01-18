package sw.type;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
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
import sw.gen.*;
import sw.world.meta.*;
import sw.world.recipes.*;

import static mindustry.Vars.*;

public class SWUnitType extends UnitType {
  // Submarine stuff
  public boolean submerges = false;
  public float vulnerabilityTime = 60f;
  public float minSubmergeAlpha = 0.1f;

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

  //rotor unit stuff
  public Seq<UnitRotor> rotors = new Seq<>();
  public float rotorSlowDown = 0.014f;
  public float rotateDeathSpeed = 1f;
  public boolean rotatesDeath = true;
  public boolean drawRotors = true;

  public SWUnitType(String name) {
    super(name);
  }

  @Override
  public void applyColor(Unit unit) {
    if (submerges && unit instanceof Submarinec u) {
      Draw.mixcol(Pal.shadow, 0.6f * (1f - u.submergeTime()/vulnerabilityTime));
      Draw.alpha(Math.max(minSubmergeAlpha, (1f - u.submergeTime()/vulnerabilityTime)));
    } else {
      super.applyColor(unit);
    }
  }

  @Override
  public void draw(Unit unit) {
    super.draw(unit);
    if (unit instanceof Shieldedc u) drawShields(u);
    if (unit instanceof Copterc u) drawRotors(u);
  }

  @Override
  public void drawShadow(Unit unit) {
    if (submerges && unit instanceof Submarinec u) {
      float e = Mathf.clamp(unit.elevation, shadowElevation, 1f) * shadowElevationScl * (1f - unit.drownTime) * (u.submergeTime()/vulnerabilityTime);
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

  public void drawShields(Shieldedc u) {
    Draw.draw(Draw.z(), () -> {
      if (u.mounts().length > 0) {
        WeaponMount first = u.mounts()[0];
        DrawPart.params.set(first.warmup, first.reload / weapons.first().reload, first.smoothReload, first.heat, first.recoil, first.charge, u.x(), u.y(), u.rotation());
      } else {
        DrawPart.params.set(0, 0, 0, 0, 0, 0, u.x(), u.y(), u.rotation());
      }
      for(int i = 0; i < shields; i++) {
        float ang = Mathf.lerp(
          shieldStartAng + (u.rotation() - 90f + shieldEndAng/(shields - 1f) * i),
          shieldShootingStartAng + (u.rotation() - 90f + shieldShootingEndAng /(shields - 1f) * i),
          shieldProgress.get(DrawPart.params)
        );
        Tmp.v1.trns(ang, shieldSeparateRadius).add(u);
        try {
          if (u.units().get(i).dead()) Drawf.construct(Tmp.v1.x, Tmp.v1.y, shieldUnit.fullIcon, Tmp.v1.angleTo(u) + 90, u.progress(), 1f, u.progress() * shieldConstructTime);
        } catch (IndexOutOfBoundsException e) {
          Drawf.construct(Tmp.v1.x, Tmp.v1.y, shieldUnit.fullIcon, Tmp.v1.angleTo(u) + 90, u.progress(), 1f, u.progress() * shieldConstructTime);
        }
      }
    });
  }

  public void drawRotors(Copterc unit) {
    if (drawRotors) rotors.each(rotor -> rotor.draw(unit));
  }

  @Override
  public void init() {
    super.init();
    if (shieldSeparateRadius == -1) shieldSeparateRadius = hitSize;
  }

  @Override
  public void load() {
    super.load();
    rotors.each(rotor -> rotor.load(this));
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
    if (submerges && unit instanceof Submarinec u) return !u.submerged();
    return super.targetable(unit, from);
  }

	public class UnitRotor {
    /**
     * position
     */
    public float x, y;
    /**
     * speed
     */
    public float speed = 1f;
    /**
     * rotor layer offset
     */
    public float layerOffset;
    /**
     * draws a top region
     */
    public boolean drawTop = true;
    /**
     * mirrors it between 2 sides, if false, it draws 2 opposite rotors at the same position
     */
    public boolean mirrored;
    /**
     * name is a suffix for finding regions
     */
    public boolean isSuffix;

    public String name;

    public TextureRegion region, topRegion, blurRegion, shineRegion;

    public UnitRotor(String name, boolean isSuffix) {
      this.name = name;
      this.isSuffix = isSuffix;
    }

    public void draw(Copterc unit) {
			float z = Draw.z();
			Draw.z(z + layerOffset);
      for (int i : Mathf.signs) {
        Tmp.v1.trns(unit.rotation() - 90, x * i, y * i).add(unit.x(), unit.y());
        if (mirrored) Tmp.v1.trns(unit.rotation() - 90, mirrored && i == 1 ? -x : x, mirrored && i == 1 ? -y : y).add(unit.x(), unit.y());

        float drawX = Tmp.v1.x, drawY = Tmp.v1.y;
        if (mirrored && i == -1) Draw.scl(-1, 1);
        Draw.alpha(1f - unit.rotorBlur());
        Draw.rect(region, drawX, drawY, unit.rotation() + Time.time * speed * i);
        Draw.alpha(unit.rotorBlur());
        if (mirrored || i == 1) {
          Draw.rect(blurRegion, drawX, drawY, unit.rotation() + Time.time * speed * i);
          Draw.rect(shineRegion, drawX, drawY, -Time.time * i);
          Draw.alpha(1f);
          if (drawTop) Draw.rect(topRegion, drawX, drawY, unit.rotation() - 90);
        }
        Draw.reset();
      }
			Draw.z(z);
    }

    public void load(SWUnitType type) {
      String regionName = (isSuffix ? type.name : "") + name;

      region = Core.atlas.find(regionName);
      topRegion = Core.atlas.find(regionName + "-top");
      blurRegion = Core.atlas.find(regionName + "-blur");
      shineRegion = Core.atlas.find(regionName + "-shine");
    }
  }
}
