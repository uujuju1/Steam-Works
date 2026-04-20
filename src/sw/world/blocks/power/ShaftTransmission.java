package sw.world.blocks.power;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.entities.units.*;
import mindustry.ui.*;
import mindustry.world.*;
import sw.ui.*;
import sw.world.interfaces.*;

public class ShaftTransmission extends AxleBlock {
	public float multiplier = 2;

	public int[] highEdges;
	public int[] lowEdges;

	public ShaftTransmission(String name) {
		super(name);
		configurable = true;
		saveConfig = true;

		config(Float.class, (ShaftTransmissionBuild build, Float value) -> {
			build.currentRatioScale = value;
			build.spinGraph().updateRatios(build);
		});
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

	@Override
	public void init() {
		super.init();

		if (spinConfig != null && highEdges != null) {
			Seq<Point2> edges = spinConfig.outAllowedEdges[0];

			lowEdges = new int[edges.size - highEdges.length];

			int k = 0;
			for (int i = 0; i < edges.size; i++) {
				boolean isHigh = false;
				for (int j : highEdges) {
					if (j == i) {
						isHigh = true;
						break;
					}
				}
				if (!isHigh) {
					lowEdges[k] = i;
					k++;
				}
			}
		}
	}

	public class ShaftTransmissionBuild extends AxleBlockBuild {
		public float currentRatioScale = configurable ? 1f : multiplier;

		@Override
		public void buildConfiguration(Table cont) {
			cont.table(Styles.black6, table -> {
				Slider slider = table.slider(
					-multiplier + 1, multiplier, 1f, currentRatioScale < 1f ? (-1f/currentRatioScale) + 1: currentRatioScale,
					value -> configure(value > 0 ? value : -1f/(value - 1f))
				).minWidth(300f).growX().get();

				slider.setStyle(SWStyles.smallSlider);

				table.row();
				table.table(below -> below.label(
					() -> Core.bundle.get("ui.sw-scale") +
					Strings.autoFixed(slider.isDragging() ? slider.getValue() > 0 ? slider.getValue() : -1f/(slider.getValue() - 1f) : currentRatioScale, 3)
				)).left().padTop(5f).row();
			}).margin(10f);
		}

		public boolean isHigh(HasSpin other) {
			Seq<Point2> edges = getConnectingOuterEdges();
			Seq<Point2> connectingEdges = nextConnections(other);
			for(int i : highEdges) {
				if (connectingEdges.contains(edges.get(i))) return true;
			}
			return false;
		}
		public boolean isLow(HasSpin other) {
			Seq<Point2> edges = getConnectingOuterEdges();
			Seq<Point2> connectingEdges = nextConnections(other);
			for(int i : lowEdges) {
				if (connectingEdges.contains(edges.get(i))) return true;
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
		
		// TODO this is a cheap way of making this work, do it better, liz (not depend on block type)
		@Override
		public boolean ratioInvalid(HasSpin with) {
			return
				(isHigh(with) && isLow(with) && (with.asBuilding().block != block || with.asBuilding().rotation != rotation)) ||
				!Mathf.zero(ratioTo(with) * with.ratioScl(this) - with.spinGraph().ratios.get(with, 1), 0.00001f);
		}
		@Override
		public float ratioTo(HasSpin from) {
			float hScl = (1f + currentRatioScale) / 2f;
			return super.ratioTo(from) / hScl * (isHigh(from) ? currentRatioScale : 1f);
		}
		@Override
		public float ratioScl(HasSpin to) {
			return super.ratioScl(to) * (isHigh(to) ? 1f / currentRatioScale : 1f) * (1f + currentRatioScale) / 2f;
		}

		@Override
		public void write(Writes write) {
			super.write(write);

			if (configurable) write.f(currentRatioScale);
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);

			if (configurable) currentRatioScale = read.f();
		}
	}
}
