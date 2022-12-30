package sw.content;

import arc.struct.Seq;
import mindustry.content.TechTree;
import mindustry.game.Objectives.Produce;

import static mindustry.content.TechTree.*;
import static sw.content.SWBlocks.*;
import static sw.content.SWItems.*;

public class SWTechTree {
  public static TechTree.TechNode root;

  public static void load() {
    root = nodeRoot("Steam Works", coreScaffold, () -> {
      // items
      nodeProduce(tin, () -> {
        nodeProduce(silver, () -> {});
        nodeProduce(denseAlloy, Seq.with(new Produce(silver)), () -> {});
      });

      /* blocks */

      // distribution
      node(pressurePipe, Seq.with(new Produce(silver)), () -> {
        node(pressureValve);
        node(pressureBridge);
      });

      // crafting
      node(hydraulicCrafter, () -> {
        node(pressurePress);
        node(steamBurner);
      });

      // turrets
      node(railgun, Seq.with(new Produce(silver)), () -> {});
    });
  }
}
