package sw.content;

import arc.struct.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.game.Objectives.*;

import static arc.struct.Seq.*;
import static mindustry.content.TechTree.*;
import static sw.content.SWBlocks.*;
import static sw.content.SWItems.*;
import static sw.content.SWLiquids.*;
import static sw.content.SWUnitTypes.*;

public class SWTechTree {
  public static TechNode root;

  public static TechNode nodeObj(UnlockableContent content, Seq<Objective> objectives) {
    return node(content, objectives, () -> {});
  }
  public static TechNode nodeProduceObj(UnlockableContent content) {
    return nodeProduce(content, () -> {});
  }

  public static void load() {
    root = nodeRoot("Steam Works", coreScaffold, () -> {
      // items
      nodeProduce(nickel, () -> {
        nodeProduceObj(steam);
        nodeProduceObj(compound);
        nodeProduceObj(denseAlloy);
      });

      // crafting
      node(nickelForge, with(new SectorComplete(SectorPresets.craters)), () -> {
        node(rebuilder, with(new SectorComplete(SectorPresets.ruinousShores)), () -> {
          node(electricSpinner);
          node(burner);
        });
        node(compoundMixer, with(
          new Research(electricSpinner),
          new OnSector(SectorPresets.windsweptIslands)
        ), () -> {});

        node(boiler, with(
          new Research(burner),
          new SectorComplete(SectorPresets.extractionOutpost)
        ), () -> node(thermalBoiler));

        nodeObj(batchPress, with(new Research(Blocks.multiPress)));
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

      // units
      node(subFactory, () -> {
        node(recluse, () -> {
          node(retreat, with(new Research(Blocks.additiveReconstructor)), () -> {
            nodeObj(evade, with(new Research(Blocks.multiplicativeReconstructor)));
          });
        });
      });
      node(crafterFactory, with(new Research(Blocks.siliconCrucible)), () -> node(bakler));
      node(swarm, with(new Produce(compound), new Research(Blocks.airFactory)), () -> {
        node(sentry, () -> {
          node(tower, with(new Research(Blocks.additiveReconstructor)), () -> {
            nodeObj(castle, with(new Research(Blocks.multiplicativeReconstructor)));
          });
        });
        node(ambush, with(new Research(Blocks.additiveReconstructor)), () -> {
          nodeObj(trap, with(new Research(Blocks.multiplicativeReconstructor)));
        });
      });
    });
  }
}
