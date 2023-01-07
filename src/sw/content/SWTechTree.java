package sw.content;

import arc.struct.Seq;
import mindustry.content.TechTree;
import mindustry.game.Objectives.*;

import static mindustry.content.TechTree.*;
import static sw.content.SWBlocks.*;
import static sw.content.SWItems.*;
import static sw.content.SWLiquids.*;

public class SWTechTree {
  public static TechTree.TechNode root;

  public static void load() {
    root = nodeRoot("Steam Works", coreScaffold, () -> {
      // items
      nodeProduce(tin, () -> {
        nodeProduce(steam, () -> {});
        nodeProduce(compound, Seq.with(new Produce(steam)), () -> {});
        nodeProduce(denseAlloy, () -> {});
      });

      /* blocks */

      // crafting
      node(hydraulicCrafter, () -> {
        node(boiler, () -> node(thermalBoiler));
        node(pressurePress);
      });

      // turrets
      node(bolt, Seq.with(new Produce(tin)), () -> {});
    });
  }
}
