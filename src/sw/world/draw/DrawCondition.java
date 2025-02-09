package sw.world.draw;

import arc.func.*;
import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class DrawCondition extends DrawBlock {
	public DrawBlock drawer;

	public Boolf<Building> condition;

	public DrawCondition(DrawBlock drawer, Boolf<Building> condition) {
		this.drawer = drawer;
		this.condition = condition;
	}

	@Override public void draw(Building build) {
		if (condition.get(build)) drawer.draw(build);
	}
	@Override public void drawLight(Building build) {
		if (condition.get(build)) drawer.drawLight(build);
	}
	@Override public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
		drawer.drawPlan(block, plan, list);
	}

	@Override public void getRegionsToOutline(Block block, Seq<TextureRegion> out) {
		drawer.getRegionsToOutline(block, out);
	}

	@Override public TextureRegion[] icons(Block block) {
		return drawer.icons(block);
	}

	@Override public void load(Block block) {
		drawer.load(block);
	}
}
