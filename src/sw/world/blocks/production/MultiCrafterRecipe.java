package sw.world.blocks.production;

import arc.*;
import arc.graphics.g2d.*;
import mindustry.gen.*;
import mindustry.world.*;
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

	public class MultiCrafterRecipeBuild extends Building {
//		convenience
		public GenericRecipe recipe() {
			return recipe;
		}

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
