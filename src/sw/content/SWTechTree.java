package sw.content;

import arc.struct.*;
import mindustry.content.*;
import mindustry.game.Objectives.*;
import mindustry.gen.*;

import static arc.struct.Seq.*;
import static mindustry.content.TechTree.*;
import static sw.content.SWItems.*;
import static sw.content.SWLiquids.*;
import static sw.content.SWSectorPresets.*;
import static sw.content.blocks.SWDefense.*;
import static sw.content.blocks.SWDistribution.*;
import static sw.content.blocks.SWProduction.*;
import static sw.content.blocks.SWStorage.*;
import static sw.content.blocks.SWTurrets.*;

public class SWTechTree {
  public static void load() {
	  SWPlanets.wendi.techTree = nodeRoot("Steam Works", coreScaffold, () -> {});
  }

  public static void init(Seq<TechNode> root) {
    /* region crafting
    root.add(node(siliconBoiler, with(new OnSector(coast)), () -> {
      node(compoundSmelter, () -> {
        node(chalkSeparator, with(new Research(boiler)), () -> {});
      });
      node(densePress, () -> {
        node(thermiteMixer, with(new Research(boiler)), () -> {});
      });
    }));
    root.peek().name = "sw-crafting";
    root.peek().icon = Icon.crafting;
    // endregion
    */
    // region defense
    root.add(node(nickelWall, () -> {
      node(nickelWallLarge);
      node(ironWall, () -> {
        node(ironWallLarge);
        node(waveRadar);
      });

      node(flow, with(new OnSector(anemoia)), () -> {
        node(trail, with(new OnSector(nostalgia)), () -> {});
        node(vniz, with(new OnSector(anemoia)), () -> {
          node(rozpad, with(new OnSector(nostalgia)), () -> {});
        });
      });
    }));
    root.peek().name = "sw-defense";
    root.peek().icon = Icon.turret;
    // endregion
    // region distribution
    root.add(node(resistantConveyor, () -> {
      node(mechanicalDistributor, () -> {
        node(mechanicalTunnel, () -> {
          node(mechanicalUnloader);
        });
        node(mechanicalGate);
        node(mechanicalSorter);
      });
      node(suspensionConveyor);
      node(mechanicalConduit, with(new Produce(Items.silicon), new Research(liquidCollector)), () -> {
        node(mechanicalConduitJunction);
        node(mechanicalConduitRouter);
      });
      node(compactContainer);
    }));
    root.peek().name = "sw-distribution";
    root.peek().icon = Icon.distribution;
    // endregion
    /*region power
    root.add(node(boiler, () -> {
      node(gasPipe, () -> {
        node(gasJunction);
      });
    }));
    root.peek().name = "sw-power";
    root.peek().icon = Icon.power;
    //endregion */
    // region production
    root.add(node(mechanicalBore, () -> {
      node(hydraulicDrill);
      node(liquidCollector);
    }));
    root.peek().name = "sw-production";
    root.peek().icon = Icon.production;
    // endregion
    // region resources
    root.add(nodeProduce(nickel, () -> {
      nodeProduce(iron, () -> {
        nodeProduce(compound, () -> nodeProduce(chalk, () -> nodeProduce(soda, () -> {})));
        nodeProduce(denseAlloy, () -> nodeProduce(thermite, () -> nodeProduce(residue, () -> {})));
        nodeProduce(meteorite, () -> {});
      });
      nodeProduce(solvent, () -> {
        nodeProduce(steam, () -> {});
          nodeProduce(fluorane, () -> {});
      });
    }));
    root.peek().name = "sw-resources";
    root.peek().icon = Icon.wrench;
    // endregion
    // region sectors
    root.add(node(nowhere, () -> {
      node(anemoia, with(new SectorComplete(nowhere)), () -> {
        node(nostalgia, with(new SectorComplete(anemoia)), () -> {
          node(coast, with(new SectorComplete(nostalgia)), () -> {});
          node(island, with(new SectorComplete(nostalgia)), () -> {});
        });
      });
    }));
    root.peek().name = "sw-sectors";
    root.peek().icon = Icon.terrain;
    // endregion
  }
}
