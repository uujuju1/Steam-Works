package sw.content;

import arc.*;
import arc.scene.style.*;
import arc.struct.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.game.*;
import mindustry.game.Objectives.*;
import mindustry.gen.*;

import static arc.struct.Seq.*;
import static mindustry.content.TechTree.*;
import static sw.content.SWItems.*;
import static sw.content.SWLiquids.*;
import static sw.content.SWSectorPresets.*;
import static sw.content.blocks.SWCrafting.*;
import static sw.content.blocks.SWDefense.*;
import static sw.content.blocks.SWDistribution.*;
import static sw.content.blocks.SWPower.*;
import static sw.content.blocks.SWProduction.*;
import static sw.content.blocks.SWStorage.*;
import static sw.content.blocks.SWTurrets.*;
import static sw.content.blocks.SWUnits.*;

public class SWTechTree {
  public static void load() {
	  SWPlanets.wendi.techTree = nodeRoot("Steam Works", coreScaffold, () -> {});
  }

  public static void init(Seq<TechNode> root) {
    // region crafting
    root.add(root("sw-crafting", Icon.crafting, engineSmelter, () -> {
      node(cokeOven);
    }));
    // endregion
    // region defense
    root.add(root("sw-defense", Icon.turret, imber, with(new Produce(coke)), () -> {
      node(ironWall, () -> node(ironWallLarge));
    }));
    // endregion
    // region distribution
    root.add(root("sw-distribution", Icon.distribution, mechanicalConveyor, () -> {
      node(mechanicalDistributor, () -> {
        node(mechanicalBridge, () -> {
          node(mechanicalUnloader, with(new Research(compactContainer)), () -> {});
        });
        node(mechanicalGate);
        node(mechanicalSorter);
      });
      node(suspensionConveyor);
      node(belt, with(new NonUnlockable()), () -> {});
      node(mechanicalConduit, with(new Research(liquidCollector)), () -> {
        node(mechanicalConduitJunction, () -> node(mechanicalConduitBridge));
        node(mechanicalConduitRouter);
      });
      node(compactContainer);
    }));
    // endregion
    //region power
    root.add(root("sw-power", Icon.power, evaporator, () -> {
      node(wireShaft, () -> {
        node(wireShaftRouter, () -> {
          node(shaftGearbox);
          node(overheadBelt);
        });
        node(shaftTransmission, Seq.with(new NonUnlockable()), () -> {});
      });
    }));
    //endregion
    // region production
    root.add(root("sw-production", Icon.production, mechanicalBore, () -> {
      node(hydraulicDrill, () -> {
        node(mechanicalFracker, () -> {});
      });
      node(liquidCollector, () -> {
        node(fogCollector, with(
          new Produce(solvent)
        ), () -> {});
      });
    }));
    // endregion
    // region resources
    root.add(rootProduce("sw-resources", Icon.wrench, verdigris, () -> {
      nodeProduce(Items.graphite, () -> {
        nodeProduce(coke, () -> {});
      });
      nodeProduce(iron, () -> {
        nodeProduce(Items.sand, () -> {});
      });
      nodeProduce(solvent, () -> {
        nodeProduce(Liquids.water, () -> {});
      });
    }));
    // endregion
    // region sectors
    root.add(root("sw-sectors", Icon.terrain, crevasse, () -> {}));
    // endregion
    //region units
    root.add(root("sw-units", Icon.units, mechanicalAssembler, with(new NonUnlockable()), () -> {
      node(assemblerArm, with(new NonUnlockable()), () -> {});
    }));
    //endregion
  }

  public static TechNode root(String name, Drawable icon, UnlockableContent content, Runnable children) {
    TechNode root = node(content, children);
    root.name = name;
    root.icon = icon;
    return root;
  }
  public static TechNode root(String name, Drawable icon, UnlockableContent content, Seq<Objective> objectives, Runnable children) {
    TechNode root = node(content, objectives, children);
    root.name = name;
    root.icon = icon;
    return root;
  }
  public static TechNode rootProduce(String name, Drawable icon, UnlockableContent content, Runnable children) {
    TechNode root = nodeProduce(content, children);
    root.name = name;
    root.icon = icon;
    return root;
  }

  public static class NonUnlockable implements Objectives.Objective {
    @Override public boolean complete() {
      return false;
    }

    @Override
    public String display() {
      return Core.bundle.get("requirement.sw-non-unlockable");
    }
  }
}
