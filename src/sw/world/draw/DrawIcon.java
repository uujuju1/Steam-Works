package sw.world.draw;

import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.world.*;
import sw.annotations.Annotations.*;

public class DrawIcon extends BlockDrawer {
	public String suffix;

	public @Load("@loadBlock.name$@suffix$") TextureRegion iconRegion;

	public DrawIcon() {
		this("-icon");
	}
	public DrawIcon(String name) {
		suffix = name;
	}

	@Override
	public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
		Draw.rect(iconRegion, plan.drawx(), plan.drawy());
	}

	@Override
	public TextureRegion[] icons(Block block) {
		return new TextureRegion[]{iconRegion};
	}
}
