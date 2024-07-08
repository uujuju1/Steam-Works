package sw.content;

import mindustry.content.*;
import mindustry.game.Objectives.*;

import static arc.struct.Seq.*;
import static mindustry.content.TechTree.*;
import static sw.content.SWBlocks.*;
import static sw.content.SWItems.*;
import static sw.content.SWLiquids.*;
import static sw.content.SWSectorPresets.*;
import static sw.content.blocks.SWCrafting.*;
import static sw.content.blocks.SWPower.*;
import static sw.content.blocks.SWDefense.*;
import static sw.content.blocks.SWDistribution.*;
import static sw.content.blocks.SWProduction.*;
import static sw.content.blocks.SWTurrets.*;

public class SWTechTree {
  public static TechNode root;

  public static void load() {
    root = nodeRoot("Steam Works", coreScaffold, () -> {
      // region crafting
      node(siliconBoiler, () -> {
        node(compoundSmelter, with(new OnSector(trinity)), () -> {});
        node(densePress, with(new OnSector(light)), () -> {});
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
        node(resistantConveyor);
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
	    node(intro, () -> {
        node(piedmont, with(new SectorComplete(intro)), () -> {
          node(trinity, with(new Produce(Items.silicon)), () -> {});
          node(light, with(new Produce(Items.silicon)), () -> {});
        });
      });
	    // endregion
    });
    SWPlanets.wendi.techTree = root;
  }
}
