package sw.world.blocks.heat;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.meta.StatUnit;
import sw.util.SWMath;
import sw.world.heat.HasHeat;
import sw.world.meta.SWStat;
import sw.world.modules.HeatModule;

public class HeatGenericCrafter extends GenericCrafter {
  public float maxHeat = 100f;
  public float heatLoss = 0.1f;
  public float outputHeat = -1f;
  public boolean acceptsHeat = true;
  public boolean outputsHeat = true;

  public HeatGenericCrafter(String name) {
    super(name);
  }

  @Override
  public void setStats() {
    super.setStats();
    stats.add(SWStat.maxHeat, maxHeat, StatUnit.degrees);
    if (outputHeat >= 0) stats.add(SWStat.outputHeat, outputHeat, StatUnit.degrees);
  }

  @Override
  public void setBars() {
    super.setBars();
    addBar("heat", (HeatGenericCrafterBuild entity) -> new Bar(Core.bundle.get("bar.heat"), Pal.accent, () -> SWMath.heatMap(entity.module().heat, 0f, maxHeat)));
  }

  @Override
  public void getRegionsToOutline(Seq<TextureRegion> out) {
    super.getRegionsToOutline(out);
  }

  public class HeatGenericCrafterBuild extends GenericCrafterBuild implements HasHeat {
    HeatModule module = new HeatModule();

    @Override public HeatModule module() {
      return module;
    }

    @Override public boolean acceptsHeat(HasHeat from, float amount) {
      return HasHeat.super.acceptsHeat(from, amount) && acceptsHeat;
    }
    @Override public boolean outputsHeat(HasHeat to, float amount) {
      return HasHeat.super.outputsHeat(to, amount) && outputsHeat;
    }

    @Override
    public void updateTile() {
      super.updateTile();
      if (efficiency > 0 && outputHeat >= 0) module().setHeat(Mathf.approachDelta(module().heat, outputHeat * efficiencyScale(), efficiencyScale()));

      if (module().heat < 0) module().setHeat(0f);
      if (module().heat > maxHeat) kill();
      for (HasHeat build : nextBuilds(self())) transferHeat(build.module());
      module().subHeat(heatLoss * Time.delta);
    }

    @Override
    public void write(Writes write) {
      super.write(write);
      module().write(write);
    }
    @Override
    public void read(Reads read, byte revision) {
      super.read(read, revision);
      module().read(read);
    }
  }
}
