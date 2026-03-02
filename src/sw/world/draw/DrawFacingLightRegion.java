package sw.world.draw;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;

public class DrawFacingLightRegion extends BlockDrawer {
	public boolean reverse;

	public DrawFacingLightRegion(boolean reverse) {
		this.reverse = reverse;
	}
	public DrawFacingLightRegion() {

	}

	@Override public void draw(Building build) {
		Draw.rect(build.block.region, build.x, build.y, ((build.rotation + 1 + Mathf.num(reverse)) % 2 - 1) * 90);
	}
	@Override public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
		Draw.rect(block.region, plan.drawx(), plan.drawy(), ((plan.rotation + 1 + Mathf.num(reverse)) % 2 - 1) * 90);
	}
	
	@Override public TextureRegion[] icons(Block block) {
		return new TextureRegion[]{block.region};
	}
}
