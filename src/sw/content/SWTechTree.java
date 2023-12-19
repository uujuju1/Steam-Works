package sw.content;

import mindustry.content.*;
import mindustry.game.Objectives.*;

import static arc.struct.Seq.*;
import static mindustry.content.TechTree.*;
import static sw.content.SWBlocks.*;
import static sw.content.SWItems.*;
import static sw.content.SWLiquids.*;
import static sw.content.SWSectorPresets.*;
import static sw.content.SWUnitTypes.*;
import static sw.content.blocks.SWForce.*;
import static sw.content.blocks.SWVibration.*;

public class SWTechTree {
  public static TechNode root;

  public static void load() {
    root = nodeRoot("Steam Works", coreScaffold, () -> {
      // region items
      nodeProduce(nickel, () -> {
        nodeProduce(steam, () -> nodeProduce(fungi, () -> {}));
        nodeProduce(compound, () -> nodeProduce(bismuth, () -> nodeProduce(graphene, () -> {})));
        nodeProduce(denseAlloy, () -> nodeProduce(thermite, () -> nodeProduce(scorch, () -> {})));
      });
      // endregion

      // region production
      node(mechanicalBore, () -> {
        node(hydraulicDrill);
        node(mechanicalCrusher, with(new OnSector(cLake)), () -> {});
        node(excavator, with(new Research(waterWheel)), () -> {});
      });
      // endregion
      
      // region crafting
      node(siliconBoiler, with(
        new OnSector(cLake),
        new Produce(SWItems.iron)
      ), () -> {
        node(compoundSmelter, with(new OnSector(shore)), () -> {
          node(smelterModule);
          node(bismuthCrystalizer, () -> {
            node(crystalizerModule);
          });
        });
        node(densePress, with(new OnSector(path)), () -> {
          node(impactPressModule);
          node(thermiteMixer, () -> {
            node(mixerModule);
          });
        });
        node(rebuilder, with(
          new Research(densePress),
          new Research(compoundSmelter)
        ), () -> {
          node(torquePump);
          node(electricSpinner, () -> {
            node(pressureSpinner);
          });
        });
      });
      // endregion

      // region distribution
      node(resistantConveyor, () -> {
        node(suspensionConveyor);
        node(mechanicalBridge);
        node(mechanicalDistributor, () -> {
          node(mechanicalOverflowGate, () -> node(mechanicalUnderflowGate));
          node(mechanicalUnloader);
        });
        node(vibrationWire, with(new Research(windCollector)), () -> node(vibrationDistributor));
        node(beltNode, with(new Research(electricSpinner)), () -> {
          node(beltNodeLarge);
          node(omniBelt, with(new Research(graphene)), () -> {});
        });
      });
      node(waterWheel, with(
        new Research(beltNode)
      ), () -> {});
      // endregion

      // region defense
      node(nickelWall, () -> {
        node(nickelWallLarge);
        node(ironWall, () -> {
          node(ironWallLarge);
          node(compoundWall, () -> node(compoundWallLarge));
          node(denseWall, () -> node(denseWallLarge));
        });
      });
      // endregion

      // region turrets
//      node(bolt, () -> {
//        node(light);
      node(artyleriya, () -> {
        node(curve, with(
          new OnSector(cLake),
          new Produce(Items.silicon)
        ), () -> {});
        node(thermikos);
        node(mortar, with(new Research(windmill)), () -> {
          node(incend, () -> {});
        });
        node(sonus, with(new Research(windCollector)), () -> {
          node(impacto);
        });
      });
//      });
      // endregion

      // region power
      node(burner, with(
        new OnSector(cLake),
        new Produce(iron)
      ), () -> {
        node(powerWire);
        node(windCollector);
        node(windmill);
      });
      node(filler, () -> node(terra));
      // endregion

      // region units
      node(constructor, with(new OnSector(industry)), () -> {
        node(upgrader);
        node(recluse, () -> {
          node(retreat, with(new Research(upgrader)), () -> {
            node(evade);
          });
        });
        node(sentry, () -> {
          node(tower, with(new Research(upgrader)), () -> {
            node(castle);
          });
        });
        node(swarm, () -> {
          node(ambush, with(new Research(upgrader)), () -> {
            node(trap);
          });
        });
      });
      // endregion

      // region maps
      node(anthill, () -> {
        node(cLake, with(new SectorComplete(anthill)), () -> {
          node(path, with(new SectorComplete(cLake)), () -> {
            node(industry, with(new SectorComplete(path)), () -> {});
          });
          node(shore, with(new SectorComplete(cLake)), () -> {});
        });
      });
      // endregion
    });
    SWPlanets.wendi.techTree = root;
  }
}
