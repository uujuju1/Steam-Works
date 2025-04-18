package sw.type;

import arc.*;
import arc.audio.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.type.*;
import sw.entities.*;
import sw.entities.units.*;
//import sw.gen.*;

public class SWUnitType extends UnitType {
  // region rotor unit stuff
  public Seq<UnitRotor> rotors = new Seq<>();
  public float rotorSlowDown = 0.014f;
  public float rotateDeathSpeed = 1f;
  public Sound rotorSound = Sounds.none;
  public float rotorSoundVolumeFrom = 1f;
  public float rotorSoundVolumeTo = 0.1f;
  // endregion

  //general unit stuff
  public boolean treadsCutOutline = true;
  public float outlineLayerOffset = 0f;

  public int wrecks = -1;
  public TextureRegion[] wreckRegions;

  public SWUnitType(String name) {
    super(name);
    outlines = false;
  }

  @Override
  public void draw(Unit unit) {
    super.draw(unit);

    if (unit instanceof CopterUnit u) drawRotors(u);
  }

  @Override
  public void drawOutline(Unit unit) {
    float z = Draw.z();
    Draw.z(z + outlineLayerOffset);
    super.drawOutline(unit);
    Draw.z(z);
  }

  public void drawRotors(CopterUnit unit) {
    for (RotorMount mount : unit.rotors) {
      mount.rotor.draw(unit, mount);
    }
  }

  @Override public void getRegionsToOutline(Seq<TextureRegion> out) {
    if (outlines) super.getRegionsToOutline(out);
  }

  @Override
  public void load() {
    super.load();
    Seq<UnitRotor> rotorSeq = new Seq<>();

    if (wrecks > 0) {
      wreckRegions = new TextureRegion[wrecks];
      for(int i = 0; i < wrecks; i++) {
        wreckRegions[i] = Core.atlas.find(name + "-wreck-" + (i + 1));
      }
    }

    rotors.each(rotor -> {
      rotorSeq.add(rotor);
      var r = rotor.copy();
      r.flip();
      rotorSeq.add(r);
    });
    rotors = rotorSeq;
    rotors.each(rotor -> rotor.load(this));
  }

	public static class UnitRotor implements Cloneable {
    /**
     * Position
     */
    public float x, y;
    /**
     * Speed
     */
    public float speed = 1f, shineSpeed = -1f, slowDownSpeed = 0.005f;
    /**
     * Amount of blades this rotor has
     */
    public int blades = 1;
    /**
     * Rotor layer offset
     */
    public float layerOffset;
    /**
     * Opacity of the blur and shine regions.
     */
    public float blurAlpha = 0.25f;
    /**
     * Mirrors it between 2 sides, if false, it draws 2 opposite rotors at the same position
     */
    public boolean mirrored;
    /**
     * Name is a suffix for finding regions
     */
    public boolean isSuffix;
    /**
     * Flips the sprite during sprite generation
     */
    public boolean flipped;
    /**
     * If true, this rotor rotates along with the unit.
     */
    public boolean followParent = true;

    public String name;

    public TextureRegion region, topRegion, blurRegion, shineRegion;

    public UnitRotor(String name, boolean isSuffix) {
      this.name = name;
      this.isSuffix = isSuffix;
    }

    public UnitRotor copy() {
      try {
        return (UnitRotor) clone();
      } catch (CloneNotSupportedException uwu) {
        throw new RuntimeException("i assume this is necessary", uwu);
      }
    }

    public void draw(Unit unit, RotorMount mount) {
			float z = Draw.z();
			Draw.z(z + layerOffset);

      Tmp.v1.trns(unit.rotation() - 90f, x, y).add(unit);
      float drawX = Tmp.v1.x, drawY = Tmp.v1.y;

      if (flipped) Draw.xscl = -1f;

      Draw.alpha(1f - mount.opacity);
      Draw.rect(region, drawX, drawY, (followParent ? unit.rotation() : 0f) + mount.rotation);
      Draw.alpha(mount.opacity * blurAlpha);
      Draw.rect(blurRegion, drawX, drawY, (followParent ? unit.rotation() : 0f) + mount.rotation);
      Draw.rect(shineRegion, drawX, drawY, (followParent ? unit.rotation() : 0f) + mount.shineRotation);
      Draw.reset();

      if (topRegion.found() && (mirrored || !flipped)) Draw.rect(topRegion, drawX, drawY, unit.rotation() - 90f);
			Draw.z(z);
    }

    public void flip() {
      if (mirrored) x *= -1f;
      speed *= -1f;
      shineSpeed *= -1f;
      flipped = !flipped;
    }

    public void load(SWUnitType type) {
      String regionName = (isSuffix ? type.name : "") + name;

      region = Core.atlas.find(regionName);
      topRegion = Core.atlas.find(regionName + "-top");
      blurRegion = Core.atlas.find(regionName + "-blur");
      shineRegion = Core.atlas.find(regionName + "-shine");
    }

    public void update(Unit unit, RotorMount mount) {
			mount.rotation += (speed * mount.opacity * Time.delta);
			mount.shineRotation += (shineSpeed * mount.opacity * Time.delta);

			mount.opacity = Mathf.approachDelta(mount.opacity, unit.dead ? 0f : 1f, slowDownSpeed);
    }
  }
}
