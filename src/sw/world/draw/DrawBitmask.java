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

	public float layer = -1f;

	public Intf<Building> index;

	public int tileWidth = 32;
	public int tileHeight = 32;

	public TextureRegion[][] regions;
	public TextureRegion icon;

	public DrawBitmask(String suffix, Intf<Building> index) {
		this.suffix = suffix;
		this.index = index;
	}

	public DrawBitmask(String suffix, Intf<Building> index, int tileSize) {
		this(suffix, index);
		tileWidth = tileHeight = tileSize;
	}

	public DrawBitmask(String suffix, Intf<Building> index, int tileSize, float layer) {
		this(suffix, index, tileSize);
		this.layer = layer;
	}

	@Override public void draw(Building build) {
		float z = Draw.z();
		if (layer > 0) Draw.z(layer);
		Draw.rect(regions[build.rotation][index.get(build)], build.x, build.y);
		Draw.z(z);
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
