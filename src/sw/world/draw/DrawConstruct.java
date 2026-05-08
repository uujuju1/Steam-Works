package sw.world.draw;

import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.draw.*;

public class DrawConstruct extends DrawBlock {
	public Func<Building, TextureRegion> constructed;

	public Color drawColor = Pal.accent;
	public boolean reverse = false;

	public DrawConstruct(Func<Building, TextureRegion> constructed) {
		this.constructed = constructed;
	}

	@Override
	public void draw(Building build) {
		float z = Draw.z();
		TextureRegion recipe = constructed.get(build);

		if (recipe != null) {
//			Drawf.shadow(build.x, build.y, recipe.h, reverse ? 1f - build.progress() : build.progress());
			Draw.draw(Layer.blockBuilding, () -> {
				Draw.color(drawColor);

				Shaders.blockbuild.region = recipe;
				Shaders.blockbuild.time = build.totalProgress();
				Shaders.blockbuild.progress = reverse ? 1f - build.progress() : build.progress();

				Draw.rect(recipe, build.x, build.y, 0);
				Draw.flush();

				Draw.color();
			});
		}
		Draw.z(z);
	}
}
