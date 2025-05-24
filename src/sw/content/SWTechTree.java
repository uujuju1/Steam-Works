package sw.content;

import arc.*;
import arc.struct.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.game.*;
import mindustry.game.Objectives.*;

import static arc.struct.Seq.*;
import static mindustry.content.TechTree.*;
import static sw.content.SWItems.*;
import static sw.content.SWLiquids.*;
import static sw.content.SWSectorPresets.*;
import static sw.content.SWUnitTypes.*;
import static sw.content.blocks.SWCrafting.*;
import static sw.content.blocks.SWDefense.*;
import static sw.content.blocks.SWDistribution.*;
import static sw.content.blocks.SWPower.*;
import static sw.content.blocks.SWProduction.*;
import static sw.content.blocks.SWStorage.*;
import static sw.content.blocks.SWTurrets.*;
import static sw.content.blocks.SWUnits.*;

@SuppressWarnings("UnusedReturnValue")
public class SWTechTree {
  public static void load() {
	  SWPlanets.wendi.techTree = nodeRoot("Steam Works", coreScaffold, () -> {
      // region crafting
      root("sw-crafting", engineSmelter, () -> {
        node(cokeOven);
      });
      // endregion
      // region defense
      root("sw-defense", imber, with(new Produce(coke)), () -> {
        node(ironWall, with(new OnSector(theDelta)), () -> node(ironWallLarge));
        node(trail, with(new SectorComplete(theDelta)), () -> {});
      });
      // endregion
      // region distribution
      root("sw-distribution", mechanicalConveyor, () -> {
        node(mechanicalDistributor, () -> {
          node(mechanicalBridge, () -> {
            node(mechanicalUnloader, with(new Research(compactContainer)), () -> {});
          });
          node(mechanicalGate);
          node(mechanicalSorter);
        });
        node(suspensionConveyor);
        node(belt, with(new NonUnlockable()), () -> {
          node(mechanicalPayloadConveyor, () -> {
            node(mechanicalPayloadRouter);
          });
        });
        node(mechanicalConduit, with(new Research(liquidCollector)), () -> {
          node(mechanicalConduitJunction, () -> node(mechanicalConduitBridge));
          node(mechanicalConduitRouter);
        });
        node(compactContainer);
      });
      // endregion
      //region power
      root("sw-power", evaporator, () -> {
        node(wireShaft, () -> {
          node(wireShaftRouter, () -> {
            node(shaftGearbox);
            node(overheadBelt);
          });
          node(torqueGauge);
          node(shaftTransmission, Seq.with(new OnSector(theDelta)), () -> {});
        });
      });
      //endregion
      // region production
      root("sw-production", mechanicalBore, () -> {
        node(hydraulicDrill, () -> {
          node(mechanicalFracker, () -> {});
        });
        node(liquidCollector, () -> {
          node(artesianWell, with(
            new Produce(solvent)
          ), () -> {});
        });
      });
      // endregion
      // region resources
      rootProduce("sw-resources", verdigris, () -> {
        nodeProduce(Items.graphite, () -> {
          nodeProduce(coke, () -> {});
        });
        nodeProduce(iron, () -> {
          nodeProduce(Items.sand, () -> {});
        });
        nodeProduce(solvent, () -> {
          nodeProduce(Liquids.water, () -> {});
        });
      });
      // endregion
      // region sectors
      root("sw-sectors", crevasse, () -> {
        node(theDelta);
        node(kettle, with(new Research(mechanicalAssembler)), () -> {});
      });
      // endregion
      //region units
      root("sw-units", mechanicalAssembler, with(new Research(shaftTransmission)), () -> {
				node(soar);
        node(assemblerArm, with(new OnSector(kettle)), () -> {});
      });
      //endregion
    });
  }

  public static TechNode root(String name, UnlockableContent content, Runnable children) {
    TechNode root = node(content, children);
    root.name = name;
    return root;
  }
  public static TechNode root(String name, UnlockableContent content, Seq<Objective> objectives, Runnable children) {
    TechNode root = node(content, objectives, children);
    root.name = name;
    return root;
  }
  public static TechNode rootProduce(String name, UnlockableContent content, Runnable children) {
    TechNode root = nodeProduce(content, children);
    root.name = name;
    return root;
  }

  public static class NonUnlockable implements Objectives.Objective {
    @Override public boolean complete() {
      return false;
    }

    @Override
    public String display() {
      return Core.bundle.get("requirement.sw-non-unlockable");
    }
  }
}
