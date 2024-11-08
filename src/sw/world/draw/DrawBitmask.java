package sw.world.draw;

import arc.*;
import arc.func.*;
import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class DrawBitmask extends DrawBlock {
	public String suffix;

	public Intf<Building> index;

	public int tileWidth = 32;
	public int tileHeight = 32;

	public TextureRegion[][] regions;
	public TextureRegion icon;

	public DrawBitmask(String suffix, Intf<Building> index) {
		this.suffix = suffix;
		this.index = index;
	}

	@Override public void draw(Building build) {
		Draw.rect(regions[build.rotation][index.get(build)], build.x, build.y);
	}
	@Override public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
		Draw.rect(regions[plan.rotation][0], plan.drawx(), plan.drawy());
	}

	@Override public TextureRegion[] icons(Block block) {
		return new TextureRegion[]{icon};
	}

	@Override public void load(Block block) {
		regions = Core.atlas.find(block.name + suffix).split(tileWidth, tileHeight);
		icon = Core.atlas.find(block.name + suffix + "-icon");
	}
}
