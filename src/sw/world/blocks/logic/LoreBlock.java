package sw.world.blocks.logic;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import mindustry.gen.*;
import mindustry.world.*;

public class LoreBlock extends Block {
	public String lore = "block.sw-lore.lore";
	public Seq<Color> colors = Seq.with(Color.red, Color.green, Color.blue);

	public LoreBlock(String name) {
		super(name);
		hasShadow = false;
		solid = false;
		update = true;
		configurable = true;
	}

	public class LoreBlockBuild extends Building {
		@Override
		public void draw() {
			Draw.blend(Blending.additive);
			Draw.color(colors.getFrac(Mathf.random()));
			Draw.alpha(0.5f);
			Draw.rect(region, x, y, rotdeg() + Mathf.random(-15f, 15f));

			for(int j = 0; j < 3; j++) {
				Fill.polyBegin();
				for (int i = 0; i < 8; i++) {
					float
						dx = Angles.trnsx(360f / 8f * i, 8f + Mathf.random(8f)),
						dy = Angles.trnsy(360f / 8f * i, 8f + Mathf.random(8f));
					Fill.polyPoint(x + dx, y + dy);
				}
				Draw.color(colors.getFrac(Mathf.random()));
				Draw.alpha(0.5f);
				Fill.polyEnd();
			}
			Draw.blend();
		}

		@Override public void buildConfiguration(Table table) {
			table.add(Core.bundle.get(lore + "-" + Core.settings.getInt("sw-dream-lore", 0)));
		}
	}
}
