package sw.world.blocks.power;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.entities.units.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.input.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import sw.world.graph.*;
import sw.world.interfaces.*;

public class AxleBridge extends AxleBlock {
	private static int buildCounter;

	public float range = 40f;

	public int maxConnections = 3;

	public float radius = 3f;
	public float spinScl = 1/4f;
	public float stroke = 0.5f;
	public Color outlineColor = Pal.darkestMetal;
	public Color inColor = Pal.darkestMetal.cpy().mul(0.5f);
	public Color boltColor = Pal.darkestMetal.cpy().mul(1.5f);
	public float spacing = 2f;
	
	public float ratioScl = 1f;

	public TextureRegion rotatorRegion;

	public AxleBridge(String name) {
		super(name);

		configurable = true;

		config(Integer.class, (AxleBridgeBuild build, Integer pos) -> build.link = pos);
		config(Point2.class, (AxleBridgeBuild build, Point2 pos) -> build.link = Point2.unpack(build.pos()).add(pos).pack());
		configClear((AxleBridgeBuild build) -> build.link = -1);
	}

	@Override
	public void changePlacementPath(Seq<Point2> points, int rotation) {
		Placement.calculateNodes(points, this, rotation, (point, other) -> Mathf.dst(point.x, point.y, other.x, other.y) * 8f <= range);
	}
	
	public void drawBelt(float x, float y, float x2, float y2, float radius, float radius2, float spin) {
		float angle = Angles.angle(x, y, x2, y2);
		float dst = Tmp.v1.trns(angle + 90f, radius).add(x, y).dst(Tmp.v2.trns(angle + 90f, radius2).add(x2, y2));
		float rad = Mathf.PI * radius;
		float rad2 = Mathf.PI * radius2;
		
		var mxcol = Draw.getMixColor();
		
		Lines.stroke(stroke * 3, Tmp.c1.set(outlineColor).lerp(mxcol, mxcol.a));
		Lines.arc(x, y, radius, 0.52f, angle + 90);
		Lines.arc(x2, y2, radius2, 0.52f, angle - 90);
		for(int i : Mathf.signs) {
			Lines.line(
				x + Angles.trnsx(angle + 3 + 90 * i, radius),
				y + Angles.trnsy(angle + 3 + 90 * i, radius),
				x2 + Angles.trnsx(angle + 3 + 90 * i, radius2),
				y2 + Angles.trnsy(angle + 3 + 90 * i, radius2),
				false
			);
		}
		
		Lines.stroke(stroke, Tmp.c1.set(inColor).lerp(mxcol, mxcol.a));
		Lines.arc(x, y, radius, 0.52f, angle + 90);
		Lines.arc(x2, y2, radius2, 0.52f, angle - 90);
		for(int i : Mathf.signs) {
			Lines.line(
				x + Angles.trnsx(angle + 1 + 90 * i, radius),
				y + Angles.trnsy(angle + 1 + 90 * i, radius),
				x2 + Angles.trnsx(angle + 1 + 90 * i, radius2),
				y2 + Angles.trnsy(angle + 1 + 90 * i, radius2),
				false
			);
		}
		
		float maxLen = 2 * dst + rad + rad2;
		int divisions = Mathf.floor(maxLen/spacing);
		
		Draw.color(Tmp.c1.set(boltColor).lerp(Color.white, mxcol.a));
		for(int i = 0; i < divisions; i++) {
			float num = Mathf.mod(spin * spinScl + maxLen/divisions * i, maxLen);
			
			Vec2 res = Tmp.v1.setZero();
			
			if (num <= rad) {
				res = Tmp.v1.trns(angle + 90f + num/rad * 180f, radius).add(x, y);
			}
			if (num > rad && num <= rad + dst) {
				res = Tmp.v1.set(
					x + Angles.trnsx(angle + 1f - 90f, radius),
					y + Angles.trnsy(angle + 1f - 90f, radius)
				).lerp(
					x2 + Angles.trnsx(angle + 1f - 90f, radius2),
					y2 + Angles.trnsy(angle + 1f - 90f, radius2),
					(num - rad)/dst
				);
			}
			
			if (num > rad + dst && num <= rad + rad2 + dst) {
				res = Tmp.v1.trns(angle - 90f + (num - (rad + dst))/rad2 * 180, radius2).add(x2, y2);
			}
			if (num > rad + rad2 + dst && num <= maxLen) {
				res = Tmp.v1.set(
					x2 + Angles.trnsx(angle + 1f + 90f, radius2),
					y2 + Angles.trnsy(angle + 1f + 90f, radius2)
				).lerp(
					x + Angles.trnsx(angle + 1f + 90f, radius),
					y + Angles.trnsy(angle + 1f + 90f, radius),
					(num - (rad + rad2 + dst))/dst
				);
			}
			Fill.circle(res.x, res.y, stroke);
		}
		
		Draw.reset();
		Draw.mixcol(mxcol, mxcol.a);
	}

	@Override
	public void drawOverlay(float x, float y, int rotation) {
		super.drawOverlay(x, y, rotation);

		Drawf.dashCircle(x, y, range, Pal.accent);
	}

	@Override
	public void drawPlanConfigTop(BuildPlan plan, Eachable<BuildPlan> list) {
		if (plan.config instanceof Point2 p) {
			Point2 otherPos = new Point2(plan.x, plan.y).add(p);
			
			BuildPlan[] other = new BuildPlan[1];
			list.each(o -> {
				if (o.x == otherPos.x && o.y == otherPos.y) other[0] = o;
			});
			
			if (other[0] != null && other[0].block instanceof AxleBridge bridge) {
				drawBelt(plan.drawx(), plan.drawy(), other[0].drawx(), other[0].drawy(), radius, bridge.radius, 0f);
				
				Draw.rect(rotatorRegion, plan.drawx(), plan.drawy());
				Draw.rect(bridge.rotatorRegion, other[0].drawx(), other[0].drawy());
			}
		}
	}

	public int findBridges(Tile from, Team team, Boolf<AxleBridgeBuild> filter, Cons<AxleBridgeBuild> cons) {
		buildCounter = 0;
		Vars.indexer.eachBlock(team, from.worldx(), from.worldy(), range, b -> b instanceof AxleBridgeBuild build && filter.get(build), b -> {
			buildCounter++;
			cons.get((AxleBridgeBuild) b);
		});
		return buildCounter;
	}

	@Override
	public void handlePlacementLine(Seq<BuildPlan> plans) {
		for(int i = 0; i < plans.size - 1; i++){
			var cur = plans.get(i);
			var next = plans.get(i + 1);
			if(Mathf.dst(cur.x, cur.y, next.x, next.y) * 8f <= range){
				cur.config = new Point2(next.x - cur.x, next.y - cur.y);
			}
		}
	}

	@Override
	public void init() {
		super.init();
		updateClipRadius(range + 4f);
	}

	@Override
	public void load() {
		super.load();
		rotatorRegion = Core.atlas.find(name + "-rotator");
	}

	@Override
	public void setBars() {
		super.setBars();

		addBar("connections", entity -> {
			AxleBridgeBuild build = entity.as();
			int links = findBridges(build.tile, build.team, b -> b.link == build.pos() || build.link == b.pos(), b -> {});
			return new Bar(
				() -> Core.bundle.format("bar.powerlines", links, maxConnections + 1),
				() -> Pal.accent,
				() -> links / (maxConnections + 1f)
			);
		});
	}

	@Override
	public void setStats() {
		super.setStats();

		stats.add(Stat.range, StatValues.number(range/8f, StatUnit.blocks));
		stats.add(Stat.powerConnections, maxConnections);
	}

	public class AxleBridgeBuild extends AxleBlockBuild {
		public int link = -1;

		@Override public Point2 config() {
			return Point2.unpack(link).sub(tileX(), tileY());
		}

		@Override
		public void draw() {
			super.draw();

			if (getLink() != null) {
				Draw.z(Layer.power);
				drawBelt(x, y, getLink().x, getLink().y, radius, ((AxleBridge) getLink().block).radius, getRotation());
				
				
				float map = Mathf.map(
					Mathf.mod(getRotation() * spinScl, Mathf.PI * radius * 2),
					0f, Mathf.PI * radius * 2f,
					0, 360f
				);
				float map2 = Mathf.map(
					Mathf.mod(getRotation() * spinScl, Mathf.PI * ((AxleBridge) getLink().block).radius * 2),
					0f, Mathf.PI * ((AxleBridge) getLink().block).radius * 2f,
					0, 360f
				);
				
				Drawf.spinSprite(rotatorRegion, x, y, map);
				Drawf.spinSprite(((AxleBridge) getLink().block).rotatorRegion, getLink().x, getLink().y, map2);
			}
		}

		@Override
		public void drawConfigure() {
			Drawf.select(x, y, size * 4f + 1, Pal.accent);

			int links = findBridges(tile, team, b -> b.link == pos() || link == b.pos(), b -> {
				if (link == b.pos()) Drawf.select(b.x, b.y, b.block.size * 4 + 2, Pal.place);
			});

			if (links <= maxConnections) {
				findBridges(tile, team, b -> b.link != pos() && link != b.pos() && b != this, b -> Drawf.select(b.x, b.y, b.block.size * 4 + 2 + Mathf.absin(4f, 1f), Pal.remove));
			}
		}

		@Override
		public void drawSelect() {
			findBridges(tile, team, b -> b.link == pos(), b -> {
				Lines.stroke(3f, Pal.gray);
				Lines.circle(b.x, b.y, ((AxleBridge) b.block).radius);
				Lines.line(
					b.x + Angles.trnsx(b.angleTo(this), b.block.size * 2f),
					b.y + Angles.trnsy(b.angleTo(this), b.block.size * 2f),
					x - Angles.trnsx(b.angleTo(this), b.block.size * 2f),
					y - Angles.trnsy(b.angleTo(this), b.block.size * 2f)
				);
				Lines.stroke(1f, Pal.accent);
				Lines.circle(b.x, b.y, ((AxleBridge) b.block).radius);
				Lines.line(
					b.x + Angles.trnsx(b.angleTo(this), b.block.size * 2f),
					b.y + Angles.trnsy(b.angleTo(this), b.block.size * 2f),
					x - Angles.trnsx(b.angleTo(this), b.block.size * 2f),
					y - Angles.trnsy(b.angleTo(this), b.block.size * 2f)
				);
			});

			if (getLink() != null) {
				var b = getLink();
				Lines.stroke(3f, Pal.gray);
				Lines.circle(b.x, b.y, ((AxleBridge) b.block).radius);
				Lines.line(
					b.x + Angles.trnsx(b.angleTo(this), b.block.size * 2f),
					b.y + Angles.trnsy(b.angleTo(this), b.block.size * 2f),
					x - Angles.trnsx(b.angleTo(this), b.block.size * 2f),
					y - Angles.trnsy(b.angleTo(this), b.block.size * 2f)
				);
				Lines.stroke(1f, Pal.place);
				Lines.circle(b.x, b.y, ((AxleBridge) b.block).radius);
				Lines.line(
					b.x + Angles.trnsx(b.angleTo(this), b.block.size * 2f),
					b.y + Angles.trnsy(b.angleTo(this), b.block.size * 2f),
					x - Angles.trnsx(b.angleTo(this), b.block.size * 2f),
					y - Angles.trnsy(b.angleTo(this), b.block.size * 2f)
				);
			}

			Drawf.dashCircle(x, y, range, Pal.accent);
		}

		public AxleBridgeBuild getLink() {
			Building build = Vars.world.build(link);

			if (
				build == null ||
				build == this ||
				build.team != team ||
				!(build instanceof AxleBridgeBuild other)
			) return null;

			return other;
		}

		@Override
		public Seq<HasSpin> nextBuilds() {
			var seq = super.nextBuilds();
			findBridges(tile, team, b -> b.link == pos() || link == b.pos(), seq::add);
			return seq;
		}

		@Override
		public boolean onConfigureBuildTapped(Building other) {
			AxleBridgeBuild link = getLink();
			if (this != other) {
				if (
					other != null &&
					other.team == team &&
					dst(other) <= range &&
					other instanceof AxleBridgeBuild bridge
				) {
					if (bridge.link == pos()) {
						configure(bridge.pos());
						other.configure(-1);
						return false;
					}
					if (other != getLink()) {
						configure(other.pos());
						if (link != null) {
							spinGraph().removeBuild(link);
							new SpinGraph().mergeFlood(link);
						}
					} else {
						configure(-1);
						spinGraph().removeBuild(link);
						new SpinGraph().mergeFlood(link);
					}
					spinGraph().mergeFlood(this);
					return false;
				}
				return true;
			} else {
				configure(-1);
				if (link != null) {
					spinGraph().removeBuild(link);
					new SpinGraph().mergeFlood(link);
				}
			}
			return false;
		}
		
		@Override
		public boolean ratioInvalid(HasSpin with) {
			return super.ratioInvalid(with);
		}
		
		@Override
		public float ratioScl(HasSpin from) {
			return (findBridges(tile, team, b -> (b.link == pos() || link == b.pos()) && b == from, b -> {}) != 0 ? (ratioScl / ((AxleBridge) from.asBuilding().block).ratioScl) : 1);
		}
		
		@Override
		public float ratioTo(HasSpin to) {
			return super.ratioTo(to);
		}
		
		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);

			link = read.i();
		}

		@Override
		public void updateTile() {
			if (getLink() != null && getLink().spinGraph() != spinGraph()) spinGraph().mergeFlood(getLink());
		}

		@Override
		public void write(Writes write) {
			super.write(write);

			write.i(link);
		}
	}
}
