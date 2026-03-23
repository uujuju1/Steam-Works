package sw.content;

import arc.*;
import arc.struct.*;
import mindustry.content.*;
import mindustry.ctype.*;
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
        node(waterBallMill, with(new Produce(Liquids.water), new OnSector(abandonedMaze)), () -> {
          node(crusher, with(new NonUnlockable()), () -> {});
          node(blastFurnace, with(new NonUnlockable()), () -> {});
        });
      });
      // endregion
      // region defense
      root("sw-defense", imber, with(new Produce(coke)), () -> {
        node(trebuchet, with(new OrObjective(new OnSector(cavern), new OnSector(liveStorm))), () -> {
          node(thermikos, with(new NonUnlockable()), () -> {});
        });
        node(ironWall, with(new OnSector(theDelta)), () -> {
          node(ironWallLarge);
          node(bloomWall, () -> node(bloomWallLarge));
          node(repairStation, with(new OnSector(liveStorm)), () -> {});
        });
        node(lamparine, with(new OnSector(cavern)), () -> {
          node(grindLamp, with(new NonUnlockable()), () -> {});
          node(lavaLamp, with(new NonUnlockable()), () -> {});
        });
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
          node(belt, with(new Research(combustionEngine)), () -> {});
        });
        node(mechanicalPayloadConveyor, with(new NonUnlockable()), () -> {
          node(mechanicalPayloadRouter);
          node(mechanicalPayloadLoader, () -> node(mechanicalPayloadUnloader));
          node(courierPort);
        });
        node(mechanicalConduit, with(new Research(liquidCollector)), () -> {
          node(mechanicalConduitJunction, () -> node(mechanicalConduitTunnel));
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
        node(handWheel);
        node(combustionEngine, with(new NonUnlockable()), () -> {});
        node(waterWheel, with(new OnSector(abandonedMaze)), () -> {
          node(convectionTurbine, with(new NonUnlockable()), () -> {});
        });
        node(wireShaft, () -> {
          node(wireShaftRouter, () -> {
            node(shaftGearbox);
            node(overheadBelt, () -> node(largeOverheadBelt));
          });
          node(clutch, with(new Research(combustionEngine)), () -> {
            node(mechanicalGovernor, with(new NonUnlockable()), () -> {});
          });
          node(flywheel, with(new NonUnlockable()), () -> {});
          node(spring, with(new Research(mechanicalPayloadUnloader)), () -> {});
          node(shaftTransmission, Seq.with(new Research(mechanicalAssembler)), () -> {});
        });
      });
      //endregion
      // region production
      root("sw-production", mechanicalBore, () -> {
        node(hydraulicDrill, () -> {
          node(mechanicalFracker, () -> {});
          node(auger, with(new NonUnlockable()), () -> {});
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
          nodeProduce(Items.sand, () -> {
            nodeProduce(Items.silicon, () -> {});
          });
          nodeProduce(bloom, () -> {});
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
            node(cavern, with(
              new SectorComplete(abandonedMaze),
              new Research(coreMole)
            ), () -> {

            });
          });
          node(liveStorm, with(new SectorComplete(theDelta), new Produce(aluminium)), () -> {});
        });
      });
      // endregion
      //region units
      root("sw-units", mechanicalAssembler, with(new OnSector(theDelta)), () -> {
        node(assemblyOutpost, with(new NonUnlockable()), () -> {
          node(ballistra, with(new NonUnlockable()), () -> {});
          node(volare);
        });
				node(soar, () -> node(wisp, with(new SectorComplete(cavern)), () -> {}));
        node(assemblerArm, with(new NonUnlockable()), () -> {});
      });
      //endregion
		  node(coreMole, with(new SectorComplete(abandonedMaze)), () -> {});
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

  public static class NonUnlockable implements Objective {
    @Override public boolean complete() {
      return false;
    }

    @Override
    public String display() {
      return Core.bundle.get("requirement.sw-non-unlockable");
    }
  }
  public static class OrObjective implements Objective {
    public Objective one, another;

    public OrObjective(Objective one, Objective another) {
      this.one = one;
      this.another = another;
    }

    @Override
    public boolean complete() {
      return one.complete() || another.complete();
    }

    @Override
    public String display() {
      return Core.bundle.format("requirement.sw-or-objective", one.display(), another.display());
    }
  }
}
