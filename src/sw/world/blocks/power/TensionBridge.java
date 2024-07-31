package sw.world.blocks.power;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.core.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.input.*;
import mindustry.world.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

import java.util.*;

import static mindustry.Vars.*;

public class TensionBridge extends Block {
	public TensionConfig tensionConfig = new TensionConfig();

	public TextureRegion end, bridge;

	public int range = 5;

	private BuildPlan otherReq;

	public TensionBridge(String name) {
		super(name);
		destructible = solid = configurable = update = true;
		config(Point2.class, (TensionBridgeBuild build, Point2 point) -> {
			build.link = Point2.pack(build.tileX() + point.x, build.tileY() + point.y);
		});
		config(Integer.class, (TensionBridgeBuild build, Integer i) -> {
			build.link = i;
		});
		configClear((TensionBridgeBuild build) -> {
			build.link = -1;
		});
	}

	@Override
	public boolean canReplace(Block other) {
		return other instanceof TensionJunction || other instanceof TensionRouter || other instanceof TensionWire || super.canReplace(other);
	}

	@Override
	public void changePlacementPath(Seq<Point2> points, int rotation){
		Placement.calculateNodes(points, this, rotation, (point, other) -> Math.max(Math.abs(point.x - other.x), Math.abs(point.y - other.y)) <= range);
		points.removeAll(point -> points.indexOf(point) > 1);
	}

	public void drawBridge(float x, float y, float ox, float oy) {
		if(Mathf.zero(Renderer.bridgeOpacity)) return;

		float rot = Angles.angle(x, y, ox, oy);
		Tmp.v1.trns(rot, 4);
		Lines.stroke(8);
		Draw.alpha(Renderer.bridgeOpacity);
		Lines.line(bridge, x + Tmp.v1.x, y + Tmp.v1.y, ox - Tmp.v1.x, oy - Tmp.v1.y, false);
		Draw.rect(end, x, y, rot);
		Draw.rect(end, ox, oy, rot + 180);
	}

	@Override
	public void drawPlanConfigTop(BuildPlan plan, Eachable<BuildPlan> list){
		otherReq = null;
		list.each(other -> {
			if(other.block == this && plan != other && plan.config instanceof Point2 p && p.equals(other.x - plan.x, other.y - plan.y)){
				otherReq = other;
			}
		});

		if(otherReq != null){
			drawBridge(plan.drawx(), plan.drawy(), otherReq.drawx(), otherReq.drawy());
		}
	}

	@Override
	public void drawPlace(int x, int y, int rotation, boolean valid) {
//		Tile link = findLink(x, y);

		for(Point2 p : Geometry.d4) {
			Drawf.dashLine(
				Pal.placing,
				x * tilesize,
				y * tilesize,
				(x + p.x * range) * tilesize,
				(y + p.y * range) * tilesize
			);
		}
	}

	@Override
	public void handlePlacementLine(Seq<BuildPlan> plans){
		for(int i = 0; i < plans.size - 1; i++){
			var cur = plans.get(i);
			var next = plans.get(i + 1);
			if(positionValid(cur.x, cur.y, next.x, next.y)){
				cur.config = new Point2(next.x - cur.x, next.y - cur.y);
				next.config = new Point2(cur.x - next.x, cur.y - next.y);
			}
		}
	}

	@Override
	public void load() {
		super.load();
		end = Core.atlas.find(name + "-end");
		bridge = Core.atlas.find(name + "-bridge");
	}

	public boolean linkValid(Tile tile, Tile other){
		if(other == null || tile == null || !positionValid(tile.x, tile.y, other.x, other.y)) return false;

		return (
			(other.block() == tile.block() && tile.block() == this) ||
			(!(tile.block() instanceof TensionBridge) && other.block() == this)
		) && (other.team() == tile.team() || tile.block() != this);
	}

	public boolean positionValid(int x, int y, int ox, int oy) {
		return (x == ox && Math.abs(y - oy) <= range) || (y == oy && Math.abs(x - ox) <= range);
	}

	@Override
	public void setStats() {
		super.setStats();
		tensionConfig.addStats(stats);
	}
	@Override
	public void setBars() {
		super.setBars();
		tensionConfig.addBars(this);
	}

	public class TensionBridgeBuild extends Building implements HasTension {
		public TensionModule tension = new TensionModule();

		public int link = -1;

		@Override public Point2 config() {
			return Point2.unpack(link).sub(tileX(), tileY());
		}

		@Override
		public void draw() {
			super.draw();
			if (getLink() != null) {
				Draw.z(Layer.blockOver);
				drawBridge(x, y, getLink().x, getLink().y);
			}
		}

		@Override
		public void drawConfigure() {
			Drawf.select(x, y, tile.block().size * tilesize / 2f + 2f, Pal.accent);
			if (getLink() != null) Drawf.select(getLink().x, getLink().y, getLink().tile.block().size * tilesize / 2f + 2f, Pal.placing);
		}

		public @Nullable TensionBridgeBuild getLink() {
			return Vars.world.build(link) instanceof TensionBridgeBuild b && linkValid(tile, b.tile) ? b : null;
		}

		@Override
		public Seq<HasTension> nextBuilds() {
			return HasTension.super.nextBuilds().add(getLink()).removeAll(Objects::isNull);
		}

		@Override public TensionModule tension() {
			return tension;
		}
		@Override public TensionConfig tensionConfig() {
			return tensionConfig;
		}

		@Override
		public boolean onConfigureBuildTapped(Building other) {
			if (other instanceof TensionBridgeBuild build) {
				if (pos() == build.link) {
					configure(-1);
					other.configure(-1);
					tensionGraph().removeBuild(this, true);
					return false;
				}

				if (connects(build) && build.connects(this) && positionValid(tileX(), tileY(), build.tileX(), build.tileY())) {
					if (build.link == -1 && link == -1) {
						configure(other.pos());
						other.configure(pos());
					}
					tensionGraph().removeBuild(this, true);
					return false;
				}
			}
			return true;
		}

		@Override
		public void onProximityUpdate() {
			super.onProximityAdded();
			tensionGraph().removeBuild(this, true);
		}
		@Override
		public void onProximityRemoved() {
			super.onProximityRemoved();
			tensionGraph().removeBuild(this, false);
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			link = read.i();
		}

		@Override
		public void updateTile() {
			if (tensionGraph().getOverallTension() > tensionConfig.maxTension) kill();
		}

		@Override
		public void write(Writes write) {
			super.write(write);
			write.i(link);
		}
	}
}
