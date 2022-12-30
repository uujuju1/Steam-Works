package sw.world.meta;

import arc.func.Cons;
import arc.func.Prov;
import arc.scene.ui.Button;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import sw.world.recipes.GenericRecipe;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.ui.Styles;

public class TableSelection {
  public static <T extends GenericRecipe> void genericRecipeSelection(Table table, Seq<T> items, Cons<T> cons, Prov<T> hold) {
    Table cont = new Table().defaults().minSize(128, 40).getTable();
    for (T recipe : items) {
      if (!(recipe.checkUnlocked())) continue;
      Button button = cont.button(button1 -> {
        for (ItemStack stack : recipe.outputItems) {
          if (stack.item.unlocked()) {
            button1.image(stack.item.uiIcon).pad(3);
          }
        }
        for (LiquidStack stack : recipe.outputLiquids) {
          if (stack.liquid.unlocked()) {
            button1.image(stack.liquid.uiIcon).pad(3);
          }
        }
      }, Styles.clearTogglei, () -> {}).growX().get();
      cont.row();
      button.update(() -> button.setChecked(hold.get() == recipe));
      button.changed(() -> cons.get(button.isChecked() ? recipe : null));
    }
    ScrollPane pane = new ScrollPane(cont);
    pane.setOverscroll(false, false);
    pane.setScrollingDisabled(false, true);
    table.add(pane);
  }
}
