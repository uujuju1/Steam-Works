package sw.world.blocks.production;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import sw.world.meta.*;
import sw.world.recipes.*;

public class MultiCrafterRecipe extends Block {
	public TextureRegion top;
	public MultiCrafter parent;
	public GenericRecipe recipe;

	public MultiCrafterRecipe(String name) {
		super(name);
		solid = destructible = true;
		rotate = true;
	}
	public MultiCrafterRecipe(String name, Block parent) {
		this(name);
		this.parent = (MultiCrafter) parent;
	}

	@Override
	public void load() {
		super.load();
		if (parent != null) {
			recipe.drawer.load(parent);
		} else {
			throw new IllegalArgumentException("no parent?");
		}
		top = Core.atlas.find(name + "-top");
	}

	@Override
	public void setStats() {
		super.setStats();
		stats.add(SWStat.recipes, t -> {
			if (!(recipe.checkUnlocked())) {
				t.table(recipeTable -> {
					recipeTable.button(button -> {
						for (ItemStack stack : recipe.outputItems) button.image(stack.item.uiIcon).padLeft(5);
						for (LiquidStack stack : recipe.outputLiquids) button.image(stack.liquid.uiIcon).padLeft(5);
					}, () -> {
					}).growX().row();
					recipeTable.image(Icon.cancel).color(Color.scarlet).growX();
				}).growX().row();
			} else {
				t.table(table -> {
					table.add(Core.bundle.get("category.crafting")).color(Pal.accent).pad(3).left().row();
					table.table(input -> {
						input.add(Core.bundle.get("stat.input") + ":").color(Color.lightGray);

						for (ItemStack stack : recipe.consumeItems) input.add(new ItemImage(stack)).padLeft(5);
						for (LiquidStack stack : recipe.consumeLiquids) input.image(stack.liquid.uiIcon).padLeft(5);

					}).left().pad(3).padLeft(6).row();
					table.table(time -> {
						time.add(Core.bundle.get("stat.productiontime") + ":").color(Color.lightGray).padRight(5);
						StatValues.number(recipe.craftTime / 60f, StatUnit.seconds).display(time);
					}).left().pad(3).padLeft(6).row();
					table.table(power -> {
						power.add(Core.bundle.get("stat.poweruse") + ":").color(Color.lightGray).padRight(5);
						StatValues.number(recipe.consumePower * 60f, StatUnit.powerSecond).display(power);
					}).left().pad(3).padLeft(6).row();
					table.table(output -> {
						output.add(Core.bundle.get("stat.output") + ":").color(Color.lightGray);

						for (ItemStack stack : recipe.outputItems) output.add(new ItemImage(stack)).padLeft(5);
						for (LiquidStack stack : recipe.outputLiquids) output.image(stack.liquid.uiIcon).padLeft(5);
					}).left().pad(3).padLeft(6).row();
					table.left().margin(5).row();
				}).growX().row();
			}
		});
	}

	public class MultiCrafterRecipeBuild extends Building {

		@Override
		public MultiCrafterRecipe block() {
			return (MultiCrafterRecipe) block;
		}

		@Override
		public void draw() {
			Draw.rect(region, x, y, 0);
			Draw.rect(top, x, y, rotdeg());
		}
	}
}
