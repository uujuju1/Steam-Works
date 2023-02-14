package sw.entities.comp;

import arc.util.Time;
import mindustry.gen.UnitWaterMove;
import sw.entities.SWEntityMapping;
import sw.type.SWUnitType;

public class SubmarineUnit extends UnitWaterMove {
  public float vulnerabilityFrame;

  public boolean submerged() {return vulnerabilityFrame <= 0f;}

  @Override public String toString() {
    return "SubmarineUnit#" + id;
  }
  @Override public SWUnitType type() {
    return (SWUnitType) super.type();
  }
  @Override public int classId() {
    return SWEntityMapping.idMap.get(getClass());
  }

  @Override
  public void update() {
    super.update();
    if (isShooting()) vulnerabilityFrame = type().vulnerabilityTime;
    if (vulnerabilityFrame > 0) vulnerabilityFrame -= Time.delta;
  }
}
