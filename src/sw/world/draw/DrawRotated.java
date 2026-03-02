package sw.world.draw;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;

import static sw.annotations.Annotations.*;

/**
 * Draws a sprite with the light facing the top right, swapping to a region with flipped shading when needed
 */
public class DrawRotated extends BlockDrawer {
	public String suffix = "";

	public @Load("@loadBlock.name$@suffix$") TextureRegion region;
	public @Load("@loadBlock.name$@suffix$-flip") TextureRegion flippedRegion;

	@Override
	public void draw(Building build) {
		Draw.yscl = Mathf.sign(build.rotation % 2 == 0);
		Draw.rect(
			build.rotation > 1 ? flippedRegion : region,
			build.x, build.y,
			build.rotdeg() % 180f
		);
		Draw.yscl = 1f;
	}

	@Override
	public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
		Draw.yscl = Mathf.sign(plan.rotation % 2 == 0);
		Draw.rect(
			plan.rotation > 1 ? flippedRegion : region,
			plan.drawx(), plan.drawy(),
			plan.rotation % 2 * 90f
		);
		Draw.yscl = 1f;
	}

	@Override public TextureRegion[] icons(Block block) {
		return new TextureRegion[]{region};
	}
}
