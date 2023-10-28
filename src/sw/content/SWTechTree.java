package sw.content;

import arc.struct.*;
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
      // items
      nodeProduce(nickel, () -> {
        nodeProduce(steam, () -> nodeProduce(butane, () -> {}));
        nodeProduce(compound, () -> nodeProduce(bismuth, () -> nodeProduce(graphene, () -> {})));
        nodeProduce(denseAlloy, () -> nodeProduce(thermite, () -> nodeProduce(scorch, () -> {})));
      });

      // production
      node(mechanicalBore, () -> {
        node(hydraulicDrill);
        node(excavator, with(new Research(waterWheel)), () -> {});
      });

      // crafting
      node(siliconBoiler, with(new SectorComplete(SectorPresets.craters)), () -> {
        node(oilDistiller, with(
          new SectorComplete(SectorPresets.tarFields)
        ), () -> {});
        node(compoundSmelter, () -> {
          node(smelterModule);
          node(bismuthCrystalizer, () -> {
            node(crystalizerModule);
          });
        });
        node(densePress, () -> {
          node(impactPressModule);
          node(thermiteMixer, () -> {
            node(mixerModule);
          });
        });
        node(rebuilder, () -> {
          node(torquePump);
          node(electricSpinner, () -> {
            node(pressureSpinner);
          });
        });
      });

      // distribution
      node(resistantConveyor, () -> {
        node(suspensionConveyor);
        node(mechanicalBridge);
        node(mechanicalDistributor, () -> {
          node(mechanicalOverflowGate, () -> node(mechanicalUnderflowGate));
          node(mechanicalUnloader);
        });
        node(vibrationWire);
        node(beltNode, with(new Research(electricSpinner)), () -> {
          node(beltNodeLarge);
          node(omniBelt, with(new Research(graphene)), () -> {});
        });
      });
      node(waterWheel, with(
        new Research(beltNode),
        new SectorComplete(hotspot)
      ), () -> {});

      // defense
      node(compoundWall, with(new Produce(compound)), () -> {
        node(compoundWallLarge);
        node(denseWall, with(new Produce(denseAlloy)), () -> node(denseWallLarge));
      });

      // turrets
//      node(bolt, () -> {
//        node(light);
      node(artyleriya, () -> {
        node(thermikos);
        node(mortar, () -> {
          node(incend, with(new Research(oilDistiller)), () -> {});
        });
        node(sonus, () -> {
          node(impacto);
        });
      });
//      });

      // misc
      node(filler, Seq.with(new OnSector(greatLake)), () -> node(terra));

      // units
      node(swarm, with(
        new Produce(compound),
        new Research(Blocks.airFactory)
      ), () -> {
        node(subFactory, () -> node(recluse, () -> {
          node(retreat, with(new Research(Blocks.additiveReconstructor)), () -> {
            node(evade, with(new Research(Blocks.multiplicativeReconstructor)), () -> {});
          });
        }));

        node(crafterFactory, with(new Research(Blocks.siliconCrucible)), () -> node(bakler));
        node(structuraFactory, with(
          new Research(compoundSmelter),
          new Research(filler)
        ), () -> node(structura));

        node(sentry, () -> node(tower, with(new Research(Blocks.additiveReconstructor)), () -> {
          node(castle, with(new Research(Blocks.multiplicativeReconstructor)), () -> {
            node(stronghold, with(new Research(Blocks.exponentialReconstructor)), () -> {});
          });
        }));
        node(ambush, with(new Research(Blocks.additiveReconstructor)), () -> {
          node(trap, with(new Research(Blocks.multiplicativeReconstructor)), () -> {});
        });
      });

      // maps
      node(hotspot, with(
        new SectorComplete(SectorPresets.craters),
        new Produce(compound)
      ), () -> {
        node(greatLake, with(new SectorComplete(hotspot)), () -> {
          node(erosion, with(new SectorComplete(greatLake)), () -> {});
          node(aurora, with(new SectorComplete(greatLake)), () -> {});
        });
      });
    });
    SWPlanets.wendi.techTree = root;
  }
}
