package sw.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class DrawIcon extends DrawBlock {
	public String suffix;

	public TextureRegion iconRegion;

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

	@Override
	public void load(Block block) {
		iconRegion = Core.atlas.find(block.name + suffix);
	}
}
