package sw.world.blocks.liquid;

import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.blocks.liquid.*;
import sw.annotations.Annotations.*;

public class LiquidPipeTunnel extends LiquidBridge {
	public static final int splitSize = 32;
	
	public @Load(value = "@name$-tiles", splits = true, width = "splitSize", height = "splitSize") TextureRegion[][] tileRegions;
	
	public LiquidPipeTunnel(String name) {
		super(name);
	}
	
	@Override
	public void drawPlanConfigTop(BuildPlan req, Eachable<BuildPlan> list) {
		int[] tiling = {0};
		BuildPlan[] link = new BuildPlan[1];
		
		list.each(plan -> {
			if (plan == req || req.tile() == null || plan.tile() == null || !(plan.block instanceof LiquidBridge)) return;
			if (plan.config instanceof Point2 p && Tmp.p1.set(p).add(plan.x, plan.y).equals(req.x, req.y)) {
				tiling[0] |= (1 << req.tile().absoluteRelativeTo(plan.x, plan.y));
			}
			if (req.config instanceof Point2 p && Tmp.p1.set(p).add(req.x, req.y).equals(plan.x, plan.y)) {
				tiling[0] |= (1 << req.tile().absoluteRelativeTo(plan.x, plan.y));
				link[0] = plan;
			}
		});
		
		Draw.rect(tileRegions[0][tiling[0]], req.drawx(), req.drawy());
		
		if (link[0] != null) {
			Draw.rect(arrowRegion, req.drawx(), req.drawy(), req.tile().absoluteRelativeTo(link[0].x, link[0].y) * 90f);
		}
	}
	
	public class LiquidPipeBridgeBuild extends LiquidBridgeBuild {
		@Override
		public void draw() {
			int[] tiling = {0};
			
			incoming.each(i -> {
				Building b = Vars.world.build(i);
				if (b != null && b.isValid()) tiling[0] |= (1 << relativeTo(b));
			});
			if (Vars.world.build(link) != null && Vars.world.build(link) != this) {
				Building b = Vars.world.build(link);
				if (b != null && b.isValid()) tiling[0] |= (1 << relativeTo(b));
			}
			
			Draw.rect(tileRegions[0][tiling[0]], x, y);
			
			if (Vars.world.build(link) != null && Vars.world.build(link) != this) {
				Building b = Vars.world.build(link);
				if (b != null && b.isValid()) Draw.rect(arrowRegion, x, y, relativeTo(b) * 90f);
			}
		}
	}
}
