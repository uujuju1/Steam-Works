package sw.content;

import mindustry.content.Blocks;
import mindustry.content.TechTree;
import mindustry.game.Objectives.*;

import static arc.struct.Seq.with;
import static mindustry.content.TechTree.*;
import static sw.content.SWBlocks.*;
import static sw.content.SWItems.*;
import static sw.content.SWLiquids.*;
import static sw.content.SWUnitTypes.*;

public class SWTechTree {
  public static TechTree.TechNode root;

  public static void load() {
    root = nodeRoot("Steam Works", coreScaffold, () -> {
      // items
      nodeProduce(nickel, () -> {
        nodeProduce(steam, () -> {});
        nodeProduce(compound, () -> {});
        nodeProduce(denseAlloy, with(new Produce(steam)), () -> {});
      });

      /* blocks */

      // crafting
      node(nickelForge, () -> {
        node(rebuilder, () -> node(burner));
        node(boiler, with(new Research(burner)), () -> node(thermalBoiler));
      });

      node(hydraulicDrill, with(new Produce(steam)), () -> node(stirlingGenerator));

      node(heatPipe, with(new Produce(nickel), new Research(burner)), () -> {
        node(heatBridge, () -> {});
        node(heatRadiator, () -> {});
      });

      // turrets
      node(bolt, () -> node(light));

      node(subFactory, () -> {
        node(recluse, () -> {
          node(retreat, with(new Research(Blocks.additiveReconstructor)), () -> {
            node(evade, with(new Research(Blocks.multiplicativeReconstructor)), () -> {});
          });
        });
      });
      node(crafterFactory, with(new Research(Blocks.siliconCrucible)), () -> node(bakler));
      node(swarm, with(new Produce(compound), new Research(Blocks.airFactory)), () -> {
        node(ambush, with(new Research(Blocks.additiveReconstructor)), () -> {
          node(trap, with(new Research(Blocks.multiplicativeReconstructor)), () -> {});
        });
      });
    });
  }
}
