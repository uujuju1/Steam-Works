package sw.content;

import arc.struct.*;
import mindustry.content.*;
import mindustry.game.Objectives.*;
import mindustry.gen.*;

import static arc.struct.Seq.*;
import static mindustry.content.TechTree.*;
import static sw.content.SWBlocks.*;
import static sw.content.SWItems.*;
import static sw.content.SWLiquids.*;
import static sw.content.blocks.SWCrafting.*;
import static sw.content.blocks.SWDefense.*;
import static sw.content.blocks.SWDistribution.*;
import static sw.content.blocks.SWPower.*;
import static sw.content.blocks.SWProduction.*;
import static sw.content.blocks.SWTurrets.*;

public class SWTechTree {
  public static TechNode root;

  public static void load() {
    root = nodeRoot("Steam Works", coreScaffold, () -> {
      // region crafting
      node(siliconBoiler, () -> {
        node(compoundSmelter, () -> {});
        node(densePress, () -> {});
      });
      // endregion
      // region defense
      node(nickelWall, () -> {
        node(nickelWallLarge);
        node(ironWall, () -> {
          node(ironWallLarge);
          node(waveRadar);
        });

        node(flow, () -> {
          node(trail);
          node(vniz, () -> {
            node(rozpad);
          });
        });
      });
      // endregion
      // region distribution
      node(resistantConveyor, () -> {
        node(mechanicalDistributor, () -> {
          node(mechanicalTunnel);
          node(mechanicalGate);
          // node(mechanicalUnloader);
        });
        node(suspensionConveyor);
        node(mechanicalConduit, with(new Produce(Items.silicon)), () -> {
          node(mechanicalConduitJunction);
          node(mechanicalConduitRouter);
        });
      });
      // endregion
      //region power
      node(combustionTensionGenerator, with(new Produce(compound)), () -> {
        node(lowWire, () -> {
          node(lowRouter);
          node(lowBridge);
          node(wireAdapter, with(new Research(coatedWire)), () -> {});
          node(coatedWire, with(new Produce(chalk)), () -> {
            node(coatedRouter);
            node(coatedBridge);
          });
        });
        node(distributionMender);
      });
      //endregion
      // region production
      node(mechanicalBore, () -> {
        node(hydraulicDrill);
        node(dehydrator, with(new Produce(Items.silicon)), () -> {});
      });
      // endregion
      // region resources
      nodeProduce(nickel, () -> {
        nodeProduce(iron, () -> {
          nodeProduce(Items.silicon, () -> {
            nodeProduce(compound, () -> {});
            nodeProduce(denseAlloy, () -> {});
          });
        });
        nodeProduce(solvent, () -> {
          nodeProduce(steam, () -> {});
//          nodeProduce(fluorane);
        });
      });
      // endregion
	    // region sectors
	    // endregion
    });
    SWPlanets.wendi.techTree = root;
  }

  public static void init(Seq<TechNode> root) {
    // region crafting
    root.add(node(siliconBoiler, () -> {
      node(compoundSmelter, () -> {});
      node(densePress, () -> {});
    }));
    root.peek().name = "crafting";
    root.peek().icon = Icon.crafting;
    // endregion
    // region defense
    root.add(node(nickelWall, () -> {
      node(nickelWallLarge);
      node(ironWall, () -> {
        node(ironWallLarge);
        node(waveRadar);
      });

      node(flow, () -> {
        node(trail);
        node(vniz, () -> {
          node(rozpad);
        });
      });
    }));
    root.peek().name = "defense";
    root.peek().icon = Icon.turret;
    // endregion
    // region distribution
    root.add(node(resistantConveyor, () -> {
      node(mechanicalDistributor, () -> {
        node(mechanicalTunnel);
        node(mechanicalGate);
        // node(mechanicalUnloader);
      });
      node(suspensionConveyor);
      node(mechanicalConduit, with(new Produce(Items.silicon)), () -> {
        node(mechanicalConduitJunction);
        node(mechanicalConduitRouter);
      });
    }));
    root.peek().name = "distribution";
    root.peek().icon = Icon.distribution;
    // endregion
    //region power
    root.add(node(combustionTensionGenerator, () -> {
      node(lowWire, () -> {
        node(lowRouter);
        node(lowBridge);
        node(wireAdapter, with(new Research(coatedWire)), () -> {});
        node(coatedWire, with(new Produce(chalk)), () -> {
          node(coatedRouter);
          node(coatedBridge);
        });
      });
      node(distributionMender);
    }));
    root.peek().name = "power";
    root.peek().icon = Icon.power;
    //endregion
    // region production
    root.add(node(mechanicalBore, () -> {
      node(hydraulicDrill);
      node(liquidCollector, () -> node(dehydrator, with(new Research(combustionTensionGenerator)), () -> {}));
    }));
    root.peek().name = "production";
    root.peek().icon = Icon.production;
    // endregion
    // region resources
    root.add(nodeProduce(nickel, () -> {
      nodeProduce(iron, () -> {
        nodeProduce(Items.silicon, () -> {
          nodeProduce(compound, () -> {});
          nodeProduce(denseAlloy, () -> {});
        });
      });
      nodeProduce(solvent, () -> {
        nodeProduce(steam, () -> {});
//          nodeProduce(fluorane);
      });
    }));
    root.peek().name = "resources";
    root.peek().icon = Icon.wrench;
    // endregion
    // region sectors
    // endregion
  }
}
