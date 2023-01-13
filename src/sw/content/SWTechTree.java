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
      node(hydraulicCrafter, () -> {
        node(boiler, () -> node(thermalBoiler));
      });

      // turrets
      node(bolt, Seq.with(new Produce(nickel)), () -> {});

      node(swarm, Seq.with(new Produce(nickel), new Research(Blocks.airFactory)), () -> {
        node(ambush, Seq.with(new Research(Blocks.additiveReconstructor)), () -> {
          node(trap, Seq.with(new Research(Blocks.multiplicativeReconstructor)), () -> {});
        });
      });
    });
  }
}
