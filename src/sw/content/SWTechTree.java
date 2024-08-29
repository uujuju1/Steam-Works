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
    root.add(node(siliconBoiler, with(new OnSector(coast)), () -> {
      node(compoundSmelter, () -> {
        node(chalkSeparator, with(new Research(boiler)), () -> {});
      });
      node(densePress, () -> {
        node(thermiteMixer, with(new Research(boiler)), () -> {});
      });
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

      node(flow, with(new OnSector(anemoia)), () -> {
        node(trail, with(new OnSector(nostalgia)), () -> {});
        node(vniz, with(new OnSector(anemoia)), () -> {
          node(rozpad, with(new OnSector(nostalgia)), () -> {});
        });
      });
    }));
    root.peek().name = "defense";
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
      node(mechanicalConduit, with(new Produce(Items.silicon)), () -> {
        node(mechanicalConduitJunction);
        node(mechanicalConduitRouter);
      });
      node(compactContainer);
    }));
    root.peek().name = "distribution";
    root.peek().icon = Icon.distribution;
    // endregion
    //region power
    root.add(node(boiler, () -> {
      node(gasPipe, () -> {
        node(gasJunction);
      });
    }));
    root.peek().name = "power";
    root.peek().icon = Icon.power;
    //endregion
    // region production
    root.add(node(mechanicalBore, () -> {
      node(hydraulicDrill);
      node(liquidCollector);
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
