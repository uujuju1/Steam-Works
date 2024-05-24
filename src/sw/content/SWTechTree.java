package sw.content;

import mindustry.content.*;
import sw.content.blocks.*;

import static mindustry.content.TechTree.*;
import static sw.content.SWBlocks.*;
import static sw.content.SWItems.*;
import static sw.content.SWSectorPresets.*;
import static sw.content.blocks.SWDefense.*;
import static sw.content.blocks.SWDistribution.*;
import static sw.content.blocks.SWProduction.*;
import static sw.content.blocks.SWTurrets.*;
import static sw.content.blocks.SWPower.*;
import static sw.content.blocks.SWCrafting.*;
import static sw.content.SWLiquids.*;

public class SWTechTree {
  public static TechNode root;

  public static void load() {
    root = nodeRoot("Steam Works", coreScaffold, () -> {
      // region items
      nodeProduce(nickel, () -> {
        nodeProduce(iron, () -> {});
		nodeProduce(Items.graphite, () -> {
			nodeProduce(Items.silicon, () -> {
                nodeProduce(compound, () -> {
					nodeProduce(chalk, () -> {});
					nodeProduce(denseAlloy, () -> {
						nodeProduce(thermite, () -> {});
					});
				});
            });
		});
		nodeProduce(Items.sand, () -> {});
      });
      // endregion
	  // region liquids
	  nodeProduce(solvent, () -> {
		  nodeProduce(SWLiquids.steam, () -> {});
		  nodeProduce(chloro, () -> {});
		  nodeProduce(fungi, () -> {});
	  });
	  // endregion
      // region defense
      node(nickelWall, () -> {
        node(nickelWallLarge);
        node(ironWall, () -> {
          node(ironWallLarge);
        });
		node(compoundWall, () -> {
			node(compoundWallLarge);
		});
		node(denseWall, () -> {
			node(denseWallLarge);
		});

        node(flow, () -> {
          node(trail);
          node(vniz, () -> {
            node(rozpad, () -> {
				node(sonar);
			});
          });
        });
      });
      // endregion
      // region production
      node(mechanicalBore, () -> {
        node(mechanicalCrusher);
        node(hydraulicDrill);
      });
      // endregion
      // region transportation
      node(resistantConveyor, () -> {
        node(mechanicalDistributor, () -> {
          node(mechanicalTunnel);
          node(mechanicalGate);
          node(mechanicalUnloader);
        });
        node(suspensionConveyor);
		node(liquidCollector, () -> {
          node(mechanicalConduit, () -> {
            node(mechanicalConduitJunction);
            node(mechanicalConduitRouter);
          });
		});
      });
      // endregion
      // region crafting
      node(SWCrafting.siliconBoiler, () -> {
		  node(SWCrafting.compoundSmelter, () -> {
			  node(SWCrafting.densePress, () -> {
				  node(SWCrafting.thermiteMixer);
			  });
			  node(SWCrafting.chalkSeparator);
		  });
	  });
      // endregion
	  // region sectors
	  node(intro, () -> {});
	  // endreigon
	  // region power
	  node(SWPower.combustionTensionGenerator, () -> {
		  node(SWPower.lowWire, () -> {
			  node(SWPower.lowRouter, () -> {
				  node(SWPower.coatedRouter);
			  });
			  node(SWPower.lowBridge, () -> {
				  node(SWPower.coatedBridge);
			  });
			  node(SWPower.coatedWire);
			  node(SWPower.wireAdapter);
			  node(SWPower.wireJunction);
		  });
	  });
	  // endregion
	  // region Units
	  node(SWBlocks.constructor, () -> {
		  node(SWBlocks.upgrader);
	  });
	  //endregion
    });
    SWPlanets.wendi.techTree = root;
  }
}
