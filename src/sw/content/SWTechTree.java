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
        node(waterBallMill, with(new Produce(Liquids.water), new OnSector(abandonedMaze)), () -> {});
      });
      // endregion
      // region defense
      root("sw-defense", imber, () -> {
        node(ironWall, with(new OnSector(theDelta)), () -> node(ironWallLarge));
        node(trebuchet, with(new SectorComplete(theDelta)), () -> {});
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
        node(suspensionConveyor, () -> {
          node(belt);
        });
        node(mechanicalPayloadConveyor, with(new Research(hydraulicFlywheel)), () -> {
          node(mechanicalPayloadRouter);
          node(payloadCatapult);
        });
        node(mechanicalConduit, with(new Research(liquidCollector)), () -> {
          node(mechanicalConduitJunction, () -> node(mechanicalConduitBridge));
          node(mechanicalConduitRouter);
          node(liquidBasin);
        });
        node(compactContainer, () -> {
          node(coreMole, with(new OnSector(abandonedMaze)), () -> {});
        });
      });
      // endregion
      //region power
      root("sw-power", evaporator, () -> {
        node(waterWheel, with(new OnSector(abandonedMaze)), () -> {});
        node(wireShaft, () -> {
          node(wireShaftRouter, () -> {
            node(shaftGearbox);
            node(overheadBelt);
          });
          node(torqueGauge);
          node(hydraulicFlywheel, with(new NonUnlockable()), () -> {
            node(winder);
            node(latch);
          });
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
          node(pumpjack, with(new OnSector(abandonedMaze)), () -> {
            node(castingOutlet, with(new Research(liquidBasin)), () -> {});
          });
        });
      });
      // endregion
      // region resources
      rootProduce("sw-resources", verdigris, () -> {
        nodeProduce(Items.graphite, () -> {
          nodeProduce(coke, () -> {});
        });
        nodeProduce(iron, () -> {
          nodeProduce(SWItems.aluminium, () -> {});
          nodeProduce(Items.sand, () -> {});
        });
        nodeProduce(solvent, () -> {
          nodeProduce(Liquids.water, () -> {});
          nodeProduce(Liquids.slag, () -> {});
        });
      });
      // endregion
      // region sectors
      root("sw-sectors", crevasse, () -> {
        node(theDelta, with(new SectorComplete(crevasse)), () -> {
          node(abandonedMaze, with(new SectorComplete(theDelta)), () -> {
          
          });
        });
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
