package sw.type;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import sw.gen.*;

public class SWUnitType extends UnitType {
  // Submarine stuff
  public boolean submerges = false;
  public float vulnerabilityTime = 60f;

  // revealable units
  public float revealSpeed = 0.03f;
	public float revealedAt = 0.2f;
  public float hiddenAlpha = 0.1f;
  public boolean canHide = true;
  // endregion

  // region rotor unit stuff
  public Seq<UnitRotor> rotors = new Seq<>();
  public float rotorSlowDown = 0.014f;
  public float rotateDeathSpeed = 1f;
  public boolean rotatesDeath = true;
  public boolean drawRotors = true;
  // endregion

  //general unit stuff
  public float outlineLayerOffset = 0f;

  public SWUnitType(String name) {
    super(name);
  }

  @Override
  public void applyColor(Unit unit) {
    if (canHide && unit instanceof Revealc u) {
      Draw.mixcol(Pal.shadow, 1f - u.revealTime());
      Draw.alpha(Math.max(hiddenAlpha, u.revealTime()));
    } else {
      super.applyColor(unit);
    }
  }

  @Override
  public void draw(Unit unit) {
    super.draw(unit);
    if (unit instanceof Copterc u) drawRotors(u);
  }

  @Override
  public void drawCell(Unit unit) {
    applyColor(unit);

    Draw.color(cellColor(unit));
    if (canHide && unit instanceof Revealc u) {
      Draw.alpha(Math.max(hiddenAlpha, u.revealTime()));
    }
    Draw.rect(cellRegion, unit.x, unit.y, unit.rotation - 90);
    Draw.reset();
  }

  @Override
  public void drawOutline(Unit unit) {
    float z = Draw.z();
    Draw.z(z + outlineLayerOffset);
    super.drawOutline(unit);
    Draw.z(z);
  }

  public void drawRotors(Copterc unit) {
    if (drawRotors) rotors.each(rotor -> rotor.draw(unit));
  }

  @Override public void getRegionsToOutline(Seq<TextureRegion> out) {
    if (outlines) super.getRegionsToOutline(out);
  }

  @Override
  public void load() {
    super.load();
    rotors.each(rotor -> rotor.load(this));
  }

  @Override
  public boolean targetable(Unit unit, Team from) {
    if (canHide && unit instanceof Revealc u) return u.revealTime() >= revealedAt && super.targetable(unit, from);
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
