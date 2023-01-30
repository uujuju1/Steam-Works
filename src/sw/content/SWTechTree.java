package sw.content;

import arc.struct.Seq;
import mindustry.content.Blocks;
import mindustry.content.TechTree;
import mindustry.game.Objectives.*;

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
        nodeProduce(denseAlloy, Seq.with(new Produce(steam)), () -> {});
      });

      /* blocks */

      // crafting
      node(rebuilder, () -> {
        node(burner, () -> {});
        node(boiler, Seq.with(new Research(burner)), () -> node(thermalBoiler));
      });

      node(heatPipe, Seq.with(new Produce(nickel), new Research(burner)), () -> {
        node(heatBridge, () -> {});
        node(heatRadiator, () -> {});
      });

      // turrets
      node(bolt, () -> {});

      node(swarm, Seq.with(new Produce(compound), new Research(Blocks.airFactory)), () -> {
        node(ambush, Seq.with(new Research(Blocks.additiveReconstructor)), () -> {
          node(trap, Seq.with(new Research(Blocks.multiplicativeReconstructor)), () -> {});
        });
        node(recluse, Seq.with(new Research(Blocks.navalFactory)), () -> {
          node(retreat, Seq.with(new Research(Blocks.additiveReconstructor)), () -> {
            node(evade, Seq.with(new Research(Blocks.multiplicativeReconstructor)), () -> {});
          });
        });
      });
    });
  }
}
