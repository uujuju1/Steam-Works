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

public class SWTechTree {
  public static TechNode root;

  public static void load() {
    root = nodeRoot("Steam Works", coreScaffold, () -> {
      // items
      nodeProduce(nickel, () -> {
        nodeProduce(steam, () -> nodeProduce(butane, () -> {}));
        nodeProduce(compound, () -> {});
        nodeProduce(denseAlloy, () -> {});
      });

      // production
      node(excavator, with(new Research(waterWheel)), () -> {});

      // crafting
      node(nickelForge, with(
        new SectorComplete(SectorPresets.craters)
      ), () -> {
        node(oilDistiller, with(
          new SectorComplete(SectorPresets.tarFields),
          new Produce(steam)
        ), () -> {});
        node(rebuilder, with(new SectorComplete(SectorPresets.ruinousShores)), () -> {
          node(torquePump);
          node(electricSpinner, with(new SectorComplete(hotspot)), () -> {
            node(turbineSwing, () -> node(frictionHeater));
            node(compoundMixer, with(
              new Research(electricSpinner),
              new OnSector(SectorPresets.windsweptIslands)
            ), () -> {});
          });
          node(burner, () -> node(resistance, with(new Research(stirlingGenerator)), () -> {}));
        });


        node(boiler, with(
          new Research(burner),
          new OnSector(SectorPresets.extractionOutpost)
        ), () -> node(thermalBoiler));

        node(batchPress, with(new Research(Blocks.multiPress)), () -> {});
      });

      // distribution
      node(resistantConveyor, () -> {
        node(heatPipe, with(new Research(burner)), () -> {
          node(heatBridge);
          node(heatRadiator);
        });
        node(beltNode, with(new Research(electricSpinner)), () -> node(beltNodeLarge));
      });
      node(waterWheel, with(
        new Research(beltNode),
        new SectorComplete(hotspot)
      ), () -> node(stirlingGenerator));

      // defense
      node(compoundWall, with(new Produce(compound)), () -> {
        node(compoundWallLarge);
        node(denseWall, with(new Produce(denseAlloy)), () -> node(denseWallLarge));
      });

      // turrets
      node(bolt, () -> {
        node(light);
        node(mortar, () -> {
          node(incend, with(new Research(oilDistiller)), () -> {});
        });
      });

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
          new Research(compoundMixer),
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
