package sw.content;

import arc.struct.Seq;
import mindustry.content.Items;
import mindustry.content.TechTree;
import mindustry.game.Objectives.*;
import mindustry.type.ItemStack;

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
      node(pressurePipe, Seq.with(new Produce(tin), new Research(steamBurner)), () -> {
        node(pressureValve, Seq.with(new Produce(Items.plastanium)), () -> {});
        node(pressureBridge, Seq.with(new Produce(silver)), () -> {});
      });

      // crafting
      node(hydraulicCrafter, ItemStack.with(Items.graphite, 1800, Items.copper, 2000, Items.lead, 1500), () -> {
        node(pressurePress);
        node(steamBurner);
      });

      // turrets
      node(bolt, Seq.with(new Produce(tin)), () -> {});
//      node(railgun, Seq.with(new Produce(silver)), () -> {});
    });
  }
}
