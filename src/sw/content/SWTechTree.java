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

public class SWTechTree {
  public static TechNode root;

  public static void load() {
    root = nodeRoot("Steam Works", coreScaffold, () -> {
      // items
      nodeProduce(nickel, () -> {
        nodeProduce(steam, () -> {});
        nodeProduce(compound, () -> {});
        nodeProduce(denseAlloy, () -> {});
      });

      // crafting
      node(nickelForge, with(new SectorComplete(SectorPresets.craters)), () -> {
        node(rebuilder, with(new SectorComplete(SectorPresets.ruinousShores)), () -> {
          node(electricSpinner, with(new SectorComplete(frozenHotspot)), () -> {});
          node(burner);
        });
        node(compoundMixer, with(
          new Research(electricSpinner),
          new OnSector(SectorPresets.windsweptIslands)
        ), () -> {});

        node(boiler, with(
          new Research(burner),
          new OnSector(SectorPresets.extractionOutpost)
        ), () -> node(thermalBoiler));

        node(batchPress, with(new Research(Blocks.multiPress)), () -> {});
      });

      // distribution
      node(heatPipe, with(new Research(burner)), () -> {
        node(heatBridge);
        node(heatRadiator);
      });
      node(beltNode, with(new Research(electricSpinner)), () -> node(beltNodeLarge));

      // defense
      node(compoundWall, with(new Produce(compound)), () -> {
        node(compoundWallLarge);
        node(denseWall, with(new Produce(denseAlloy)), () -> node(denseWallLarge));
      });

      // turrets
      node(bolt, () -> node(light));
      node(mortar, () -> node(incend));

      // units
      node(subFactory, () -> {
        node(recluse, () -> {
          node(retreat, with(new Research(Blocks.additiveReconstructor)), () -> {
            node(evade, with(new Research(Blocks.multiplicativeReconstructor)), () -> {});
          });
        });
      });
      node(crafterFactory, with(new Research(Blocks.siliconCrucible)), () -> node(bakler));
      node(swarm, with(new Produce(compound), new Research(Blocks.airFactory)), () -> {
        node(sentry, () -> {
          node(tower, with(new Research(Blocks.additiveReconstructor)), () -> {
            node(castle, with(new Research(Blocks.multiplicativeReconstructor)), () -> {});
          });
        });
        node(ambush, with(new Research(Blocks.additiveReconstructor)), () -> {
          node(trap, with(new Research(Blocks.multiplicativeReconstructor)), () -> {});
        });
      });

      // maps
      node(frozenHotspot, with(new SectorComplete(SectorPresets.craters), new Produce(compound)), () -> {});
    });
  }
}
