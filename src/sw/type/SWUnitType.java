package sw.type;

import arc.graphics.g2d.Draw;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.game.Team;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import sw.entities.comp.SubmarineUnit;

public class SWUnitType extends UnitType {
  public boolean submerges = false;
  public float vulnerabilityTime = 60f;

  public SWUnitType(String name) {
    super(name);
  }

  @Override
  public boolean targetable(Unit unit, Team targeter) {
    if (submerges && unit instanceof SubmarineUnit u) return !u.submerged();
    return super.targetable(unit, targeter);
  }

  @Override
  public void applyColor(Unit unit) {
    if (submerges && unit instanceof SubmarineUnit u) {
      Draw.mixcol(Tmp.c1.set(Vars.world.floorWorld(unit.x, unit.y).mapColor).mul(0.83f), 0.6f * (1 - u.vulnerabilityFrame/vulnerabilityTime));
    } else {
      super.applyColor(unit);
    }
  }
}
