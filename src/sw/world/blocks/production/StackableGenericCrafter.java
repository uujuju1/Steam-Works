package sw.world.blocks.production;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import sw.annotations.Annotations.*;

public class StackableGenericCrafter extends SWGenericCrafter {
	public boolean[] connectSide = new boolean[]{true, true, true, true};

	public float boost = 0.25f;
	public float minBoost = 1f;
	public boolean addBoost;

	public Block stackBlock;
	public boolean useNearbyEfficiency;
	public boolean requireFacing;

	public @Load("@name$-stack") TextureRegion guideRegion;

	public StackableGenericCrafter(String name) {
		super(name);
	}

	@Override
	public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
		super.drawPlanRegion(plan, list);
		if (guideRegion.found()) Draw.rect(guideRegion, plan.drawx(), plan.drawy(), plan.rotation * 90f);
	}

	@Override
	public void init() {
		super.init();

		if (stackBlock == null) stackBlock = this;
	}

	@Override
	public void setStats() {
		super.setStats();

		stats.add(Stat.boostEffect, Core.bundle.get("stat.sw-boost-per-build.format"), boost * 100f);
	}

	public class StackableGenericCrafterBuild extends SWGenericCrafterBuild {
		@Override
		public float efficiencyScale() {
			return addBoost ? getEfficiency() + super.efficiencyScale() : getEfficiency() * super.efficiencyScale();
		}

		public float getEfficiency() {
			Point2[] edges = Edges.getEdges(size);

			float eff = minBoost;
			for(int i = 0; i < 4; i++) {
				if (connectSide[(rotation + i) % 4]) {
					Building nearby = nearby(
						edges[i * size].x,
						edges[i * size].y
					);

					if (
						nearby != null &&
						nearby.block == stackBlock &&
						(nearby.tileX() == tileX() || nearby.tileY() == tileY()) &&
						(!requireFacing || nearby.front() == this)
					) {
						eff += boost * (useNearbyEfficiency ? nearby.efficiency : 1f);
					}
				}
			}
			return eff;
		}
	}
}
