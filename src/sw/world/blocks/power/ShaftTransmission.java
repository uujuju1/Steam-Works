package sw.world.blocks.power;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.*;
import mindustry.entities.units.*;
import mindustry.world.*;
import sw.world.interfaces.*;

public class ShaftTransmission extends AxleBlock {
	public float multiplier = 2;

	// TODO make a way to generate the lowEdges array
	public int[] highEdges;
	@Deprecated public int[] lowEdges;

	public ShaftTransmission(String name) {
		super(name);
	}

	@Override
	public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
		if (spinConfig.allowedEdges != null) {
			for(int i : highEdges) {
				Draw.rect("sw-icon-spin-edge", (plan.x + Edges.getEdges(size)[spinConfig.allowedEdges[plan.rotation][i]].x) * Vars.tilesize, (plan.y + Edges.getEdges(size)[spinConfig.allowedEdges[plan.rotation][i]].y) * Vars.tilesize, 6, 6, 0);
			}
			for(int i : lowEdges) {
				Draw.rect("sw-icon-spin-edge", (plan.x + Edges.getEdges(size)[spinConfig.allowedEdges[plan.rotation][i]].x) * Vars.tilesize, (plan.y + Edges.getEdges(size)[spinConfig.allowedEdges[plan.rotation][i]].y) * Vars.tilesize, 2, 2, 0);
			}
		} else {
			for(Point2 i : Edges.getEdges(size)) {
				Draw.rect("sw-icon-spin-edge", (plan.x + i.x) * Vars.tilesize, (plan.y + i.y) * Vars.tilesize, 4, 4, 0);
			}
		}
		drawer.drawPlan(this, plan, list);
	}

	public class ShaftTransmissionBuild extends AxleBlockBuild {
		public boolean isHigh(HasSpin other) {
			for(int i : highEdges) {
				Point2 edge = getEdges()[spinConfig.allowedEdges[rotation][i]];
				if (nearby(edge.x, edge.y) == other) {
					if (other.spinConfig().allowedEdges == null) return true;
					for(int otherI : other.spinConfig().allowedEdges[other.asBuilding().rotation]) {
						if (
							other.asBuilding().tile.nearby(Edges.getInsideEdges(other.asBuilding().block.size)[otherI]) == tile.nearby(edge)
						) return true;
					}
				}
			}
			return false;
		}
		public boolean isLow(HasSpin other) {
			for(int i : lowEdges) {
				Point2 edge = getEdges()[spinConfig.allowedEdges[rotation][i]];
				if (nearby(edge.x, edge.y) == other) {
					if (other.spinConfig().allowedEdges == null) return true;
					for(int otherI : other.spinConfig().allowedEdges[other.asBuilding().rotation]) {
						if (
							other.asBuilding().tile.nearby(Edges.getInsideEdges(other.asBuilding().block.size)[otherI]) == tile.nearby(edge)
						) return true;
					}
				}
			}
			return false;
		}

		@Override
		public void drawSelect() {
			if (spinConfig.allowedEdges != null) {
				for(int i : highEdges) {
					Draw.rect("sw-icon-spin-edge", (tileX() + Edges.getEdges(size)[spinConfig.allowedEdges[rotation][i]].x) * Vars.tilesize, (tileY() + Edges.getEdges(size)[spinConfig.allowedEdges[rotation][i]].y) * Vars.tilesize, 6, 6, 0);
				}
				for(int i : lowEdges) {
					Draw.rect("sw-icon-spin-edge", (tileX() + Edges.getEdges(size)[spinConfig.allowedEdges[rotation][i]].x) * Vars.tilesize, (tileY() + Edges.getEdges(size)[spinConfig.allowedEdges[rotation][i]].y) * Vars.tilesize, 2, 2, 0);
				}
			} else {
				for(Point2 i : Edges.getEdges(size)) {
					Draw.rect("sw-icon-spin-edge", (tileX() + i.x) * Vars.tilesize, (tileY() + i.y) * Vars.tilesize, 4, 4, 0);
				}
			}
		}
		
		@Override
		public float getRotation() {
			return super.getRotation() * (1f + multiplier)/2f;
		}
		
		// TODO this is a cheap way of making this work, do it better, liz (not depend on block type)
		@Override public boolean ratioInvalid(HasSpin with) {
			return
				(isHigh(with) && isLow(with) && (with.asBuilding().block != block || with.asBuilding().rotation != rotation)) ||
				!Mathf.zero(ratioTo(with) * with.ratioScl(this) - with.spinGraph().ratios.get(with, 1), 0.00001f);
		}
		@Override public float ratioTo(HasSpin from) {
			float hScl = (1f + multiplier) / 2f;
			return super.ratioTo(from) / hScl * (isHigh(from) ? multiplier : 1f);
		}
		@Override public float ratioScl(HasSpin to) {
			return super.ratioScl(to) * (isHigh(to) ? 1f / multiplier : 1f) * (1f + multiplier) / 2f;
		}
	}
}
