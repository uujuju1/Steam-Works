package sw.content;

import mindustry.content.*;

import static mindustry.content.TechTree.*;
import static sw.content.SWBlocks.*;
import static sw.content.SWItems.*;
import static sw.content.SWSectorPresets.*;
import static sw.content.blocks.SWCrafting.*;
import static sw.content.blocks.SWDefense.*;
import static sw.content.blocks.SWDistribution.*;
import static sw.content.blocks.SWProduction.*;
import static sw.content.blocks.SWTurrets.*;

public class SWTechTree {
  public static TechNode root;

  public static void load() {
    root = nodeRoot("Steam Works", coreScaffold, () -> {
      // region items
      nodeProduce(nickel, () -> {
        nodeProduce(iron, () -> {
          nodeProduce(Items.silicon, () -> {});
        });
      });
      // endregion
      // region defense
      node(nickelWall, () -> {
        node(nickelWallLarge);
        node(ironWall, () -> {
          node(ironWallLarge);
        });

        node(flow, () -> {
          node(trail);
          node(vniz, () -> {
            node(rozpad);
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
        node(resistantConveyor);
        node(mechanicalConduit, () -> {
          node(mechanicalConduitJunction);
          node(mechanicalConduitRouter);
        });
      });
      // endregion
      // region crafting
      node(siliconBoiler);
      // endregion
	    // region sectors
	    node(intro, () -> {});
	    // endregion
    });
    SWPlanets.wendi.techTree = root;
  }
}
