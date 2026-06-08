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

	public float layer = -1;

	public @Load("@loadBlock.name$@suffix$") TextureRegion region;
	public @Load(value = "@loadBlock.name$@suffix$-flip", fallBack = "@loadBlock.name$@suffix$") TextureRegion flippedRegion;

	public DrawRotated(String suffix) {
		this.suffix = suffix;
	}
	public DrawRotated() {

	}

	@Override
	public void draw(Building build) {
		float z = Draw.z();
		if (layer > 0) Draw.z(layer);
		Draw.yscl = Mathf.sign(build.rotation % 2 == 0);
		Draw.rect(
			build.rotation > 1 ? flippedRegion : region,
			build.x, build.y,
			build.rotdeg() % 180f
		);
		Draw.yscl = 1f;
		Draw.z(z);
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
