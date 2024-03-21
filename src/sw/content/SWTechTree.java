package sw.content;

import static mindustry.content.TechTree.*;
import static sw.content.SWBlocks.*;
import static sw.content.SWItems.*;
import static sw.content.SWLiquids.*;

public class SWTechTree {
  public static TechNode root;

  public static void load() {
    root = nodeRoot("Steam Works", coreScaffold, () -> {
      // region items
      nodeProduce(nickel, () -> {
        nodeProduce(steam, () -> nodeProduce(fungi, () -> {}));
        nodeProduce(compound, () -> nodeProduce(bismuth, () -> nodeProduce(graphene, () -> {})));
        nodeProduce(denseAlloy, () -> nodeProduce(thermite, () -> nodeProduce(scorch, () -> {})));
      });
      // endregion
    });
    SWPlanets.wendi.techTree = root;
  }
}
