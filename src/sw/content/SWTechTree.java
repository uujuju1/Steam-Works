package sw.content;

import arc.*;
import arc.struct.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.game.Objectives.*;
import mindustry.gen.*;

import static arc.struct.Seq.*;
import static mindustry.content.TechTree.*;
import static sw.content.SWItems.*;
import static sw.content.SWLiquids.*;
import static sw.content.SWSectorPresets.*;
import static sw.content.blocks.SWCrafting.*;
import static sw.content.blocks.SWDefense.*;
import static sw.content.blocks.SWDistribution.*;
import static sw.content.blocks.SWPower.*;
import static sw.content.blocks.SWProduction.*;
import static sw.content.blocks.SWStorage.*;
import static sw.content.blocks.SWTurrets.*;

public class SWTechTree {
  public static void load() {
	  SWPlanets.wendi.techTree = nodeRoot("Steam Works", coreScaffold, () -> {});
  }

  public static void init(Seq<TechNode> root) {
    // region crafting
    root.add(node(engineSmelter, () -> {
      node(cokeOven);
    }));
    root.peek().name = "sw-crafting";
    root.peek().icon = Icon.crafting;
    // endregion
    // region defense
    root.add(node(imber, () -> {}));
    root.peek().name = "sw-defense";
    root.peek().icon = Icon.turret;
    // endregion
    // region distribution
    root.add(node(resistantConveyor, () -> {
      node(mechanicalDistributor, () -> {
        node(mechanicalTunnel, () -> {
          node(mechanicalUnloader, with(new Research(compactContainer)), () -> {});
        });
        node(mechanicalGate);
        node(mechanicalSorter);
      });
      node(suspensionConveyor);
      node(mechanicalConduit, with(new Research(liquidCollector)), () -> {
        node(mechanicalConduitJunction);
        node(mechanicalConduitRouter);
      });
      node(compactContainer, with(new NonUnlockable()), () -> {});
    }));
    root.peek().name = "sw-distribution";
    root.peek().icon = Icon.distribution;
    // endregion
    //region power
    root.add(node(evaporator, with(
      new Research(liquidCollector)
    ), () -> {
      node(wireShaft, () -> {
        node(wireShaftRouter, () -> {
          node(shaftGearbox, Seq.with(new NonUnlockable()), () -> {});
        });
        node(shaftTransmission, Seq.with(new NonUnlockable()), () -> {});
      });
    }));
    root.peek().name = "sw-power";
    root.peek().icon = Icon.power;
    //endregion
    // region production
    root.add(node(mechanicalBore, () -> {
      node(hydraulicDrill, () -> {
        node(mechanicalFracker, with(
          new Research(evaporator)
        ), () -> {});
      });
      node(liquidCollector, () -> {
        node(fogCollector, with(
          new Produce(solvent)
        ), () -> {});
      });
    }));
    root.peek().name = "sw-production";
    root.peek().icon = Icon.production;
    // endregion
    // region resources
    root.add(nodeProduce(verdigris, () -> {
      nodeProduce(Items.graphite, () -> {
        nodeProduce(coke, () -> {});
      });
      nodeProduce(iron, () -> {
//        nodeProduce(compound, () -> nodeProduce(chalk, () -> nodeProduce(soda, () -> {})));
//        nodeProduce(denseAlloy, () -> nodeProduce(thermite, () -> nodeProduce(residue, () -> {})));
//        nodeProduce(meteorite, () -> {});
      });
      nodeProduce(solvent, () -> {
        nodeProduce(Liquids.water, () -> {});
//        nodeProduce(steam, () -> {});
//        nodeProduce(fluorane, () -> {});
      });
    }));
    root.peek().name = "sw-resources";
    root.peek().icon = Icon.wrench;
    // endregion
    // region sectors
    root.add(node(jezero, () -> {
      node(myeik, with(new Research(hydraulicDrill)), () -> {
//        node(nostalgia, with(new SectorComplete(anemoia)), () -> {
//          node(coast, with(new SectorComplete(nostalgia)), () -> {});
//          node(island, with(new SectorComplete(nostalgia)), () -> {});
//        });
      });
    }));
    root.peek().name = "sw-sectors";
    root.peek().icon = Icon.terrain;
    // endregion
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
