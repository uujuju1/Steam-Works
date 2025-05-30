package sw.world.blocks.power;

import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import sw.world.interfaces.*;

public class ShaftTransmission extends AxleBlock {
	public float multiplier = 2;

	// TODO make a way to generate the lowEdges array
	public int[] highEdges;
	public int[] lowEdges;

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
		@Override
		public boolean invalidConnection(HasSpin other, float ratio, float lastRatio) {
			boolean otherHigh = false;
			for(int i : highEdges) {
				Point2 edge = getEdges()[spinConfig.allowedEdges[rotation][i]];
				if (nearby(edge.x, edge.y) == other) {
					if (other.spinConfig().allowedEdges == null) {
						otherHigh = true;
						break;
					}
					for(int otherI : other.spinConfig().allowedEdges[other.asBuilding().rotation]) {
						if (
							other.asBuilding().tile.nearby(Edges.getInsideEdges(other.asBuilding().block.size)[otherI]) == tile.nearby(edge)
						) otherHigh = true;
					}
				}
			}
			boolean otherLow = false;
			for(int i : lowEdges) {
				Point2 edge = getEdges()[spinConfig.allowedEdges[rotation][i]];
				if (nearby(edge.x, edge.y) == other) {
					if (other.spinConfig().allowedEdges == null) {
						otherLow = true;
						break;
					}
					for(int otherI : other.spinConfig().allowedEdges[other.asBuilding().rotation]) {
						if (
							other.asBuilding().tile.nearby(Edges.getInsideEdges(other.asBuilding().block.size)[otherI]) == tile.nearby(edge)
						) otherLow = true;
					}
				}
			}
			relativeToEdge(tile);

			return
				other instanceof ShaftTransmissionBuild ?
				(lastRatio * multiplier < ratio || ratio < lastRatio / multiplier || (otherHigh && otherLow && other.asBuilding().rotation != rotation)) :
				(ratio != lastRatio || (otherHigh && otherLow));

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
		public float ratioOf(HasSpin other, HasSpin last, float startRatio, float lastRatio) {
			boolean otherHigh = false;
			for(int i : highEdges) {
				Point2 edge = getEdges()[spinConfig.allowedEdges[rotation][i]];
				if (nearby(edge.x, edge.y) == other) {
					if (other.spinConfig().allowedEdges == null) {
						otherHigh = true;
						break;
					}
					for(int otherI : other.spinConfig().allowedEdges[other.asBuilding().rotation]) {
						if (
							other.asBuilding().tile.nearby(Edges.getInsideEdges(other.asBuilding().block.size)[otherI]) == tile.nearby(edge)
						) otherHigh = true;
					}
				}
			}
			boolean otherLow = false;
			for(int i : lowEdges) {
				Point2 edge = getEdges()[spinConfig.allowedEdges[rotation][i]];
				if (nearby(edge.x, edge.y) == other) {
					if (other.spinConfig().allowedEdges == null) {
						otherLow = true;
						break;
					}
					for(int otherI : other.spinConfig().allowedEdges[other.asBuilding().rotation]) {
						if (
							other.asBuilding().tile.nearby(Edges.getInsideEdges(other.asBuilding().block.size)[otherI]) == tile.nearby(edge)
						) otherLow = true;
					}
				}
			}
			boolean lastHigh = false;
			for(int i : highEdges) {
				Point2 edge = getEdges()[spinConfig.allowedEdges[rotation][i]];
				if (nearby(edge.x, edge.y) == last) {
					if (last.spinConfig().allowedEdges == null) {
						lastHigh = true;
						break;
					}
					for(int lastI : last.spinConfig().allowedEdges[last.asBuilding().rotation]) {
						if (
							last.asBuilding().tile.nearby(Edges.getInsideEdges(last.asBuilding().block.size)[lastI]) == tile.nearby(edge)
						) lastHigh = true;
					}
				}
			}

			float h = startRatio * (otherHigh ? multiplier : 1f / multiplier);

			if (startRatio == 1 && lastRatio != 1) return 1;

			return otherHigh ^ lastHigh && !(otherHigh && otherLow) ? h : startRatio;
		}

		@Override
		public float totalProgress() {
			HasSpin lowBuild = null;
			for(int i : lowEdges) {
				Building b = nearby(
					Edges.getEdges(size)[spinConfig.allowedEdges[rotation][i]].x,
					Edges.getEdges(size)[spinConfig.allowedEdges[rotation][i]].y
				);
				if (b instanceof HasSpin build && HasSpin.connects(this, build)) {
					Point2 edge = getEdges()[spinConfig.allowedEdges[rotation][i]];
					if (build.spinConfig().allowedEdges == null) {
						lowBuild = build;
						break;
					}
					for(int otherI : build.spinConfig().allowedEdges[build.asBuilding().rotation]) {
						if (
							build.asBuilding().tile.nearby(Edges.getInsideEdges(build.asBuilding().block.size)[otherI]) == tile.nearby(edge)
						) lowBuild = build;
					}
				}
			}
			HasSpin highBuild = null;
			for(int i : highEdges) {
				Building b = nearby(
					Edges.getEdges(size)[spinConfig.allowedEdges[rotation][i]].x,
					Edges.getEdges(size)[spinConfig.allowedEdges[rotation][i]].y
				);
				if (b instanceof HasSpin build && HasSpin.connects(this, build)) {
					Point2 edge = getEdges()[spinConfig.allowedEdges[rotation][i]];
					if (build.spinConfig().allowedEdges == null) {
						highBuild = build;
						break;
					}
					for(int otherI : build.spinConfig().allowedEdges[build.asBuilding().rotation]) {
						if (
							build.asBuilding().tile.nearby(Edges.getInsideEdges(build.asBuilding().block.size)[otherI]) == tile.nearby(edge)
						) highBuild = build;
					}
				}
			}
			float ratio = spinGraph().ratios.get(
				(highBuild == null && lowBuild == null ?
					this :
					(lowBuild == null ?
						highBuild :
						lowBuild
					)
				), 1f) * (lowBuild == null && highBuild != null ? 0.5f : 1f);
			return spinGraph().rotation / ratio;
		}
	}
}
