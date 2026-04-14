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
	private static AxleBridgeBuild lastBuild;
	private static float maxRange = -1;

	public float range = 40f;

	public int maxConnections = 4;

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

	public int findBridges(Tile from, Team team, float range, Boolf<AxleBridgeBuild> filter, Cons<AxleBridgeBuild> cons) {
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

		maxRange = Math.max(maxRange, range);
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
			return new Bar(
				() -> Core.bundle.format("bar.powerlines", build.links(), maxConnections),
				() -> Pal.accent,
				() -> (float) build.links() / maxConnections
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
		public IntSeq incoming = new IntSeq();

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

			if (getLink() != null) Drawf.select(getLink().x, getLink().y, getLink().block.size * 4f + 2f, Pal.place);

			if (links() < maxConnections) {
				findBridges(tile, team, range, b -> b.spinGraph() != spinGraph() && b.links() < ((AxleBridge) b.block).maxConnections, b -> {
					Drawf.select(b.x, b.y, b.block.size * 4 + 2 + Mathf.absin(4f, 1f), Pal.remove);
				});
			}
		}

		@Override
		public void drawSelect() {
			findBridges(tile, team, maxRange, b -> b.link == pos(), b -> {
				float rad = ((AxleBridge) b.block).radius;
				float angle = b.angleTo(this);
				Lines.stroke(3f, Pal.gray);
				Lines.circle(b.x, b.y, rad);
				Lines.line(
					b.x + Angles.trnsx(angle, rad),
					b.y + Angles.trnsy(angle, rad),
					x - Angles.trnsx(angle, radius),
					y - Angles.trnsy(angle, radius)
				);
				Lines.stroke(1f, Pal.accent);
				Lines.circle(b.x, b.y, rad);
				Lines.line(
					b.x + Angles.trnsx(angle, rad),
					b.y + Angles.trnsy(angle, rad),
					x - Angles.trnsx(angle, radius),
					y - Angles.trnsy(angle, radius)
				);
			});

			if (getLink() != null) {
				var b = getLink();
				float rad = ((AxleBridge) b.block).radius;
				float angle = b.angleTo(this);
				Lines.stroke(3f, Pal.gray);
				Lines.circle(b.x, b.y, rad);
				Lines.line(
					b.x + Angles.trnsx(angle, rad),
					b.y + Angles.trnsy(angle, rad),
					x - Angles.trnsx(angle, radius),
					y - Angles.trnsy(angle, radius)
				);
				Lines.stroke(1f, Pal.place);
				Lines.circle(b.x, b.y, rad);
				Lines.line(
					b.x + Angles.trnsx(angle, rad),
					b.y + Angles.trnsy(angle, rad),
					x - Angles.trnsx(angle, radius),
					y - Angles.trnsy(angle, radius)
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

		public int links() {
			return incoming.size + Mathf.num(getLink() != null);
		}

		@Override
		public Seq<HasSpin> nextBuilds() {
			var seq = super.nextBuilds();
			incoming.each(i -> {
				Building next = Vars.world.build(i);

				if (next instanceof AxleBridgeBuild build && build.isValid() && build.link == pos()) seq.add((HasSpin) Vars.world.build(i));
			});
			if (getLink() != null) seq.add(getLink());

			return seq;
		}

		@Override
		public boolean onConfigureBuildTapped(Building other) {
			AxleBridgeBuild link = getLink();

			if (
				!(other instanceof AxleBridgeBuild build) ||
				!other.isValid() ||
				other.team != team ||
				spin == null
			) return true;

			// Case other is the link, deselect
			if (link == other) {
				configure(-1);
				link.incoming.removeValue(pos());

				new SpinGraph().mergeFlood(this);

				return false;
			}
			// Case other's link is this, reverse if possible
			if (build.getLink() == this && ((AxleBridge) build.block).radius <= radius) {
				configure(build.pos());
				build.configure(-1);
				incoming.removeValue(build.pos());
				build.incoming.add(pos());

				new SpinGraph().mergeFlood(this);

				return true;
			}
			// Case neither of the above, link, reverse if needed and possibe
			if (build.links() < ((AxleBridge) build.block).maxConnections && links() < maxConnections) {
				if (((AxleBridge) build.block).radius <= radius) {
					if (link != null) link.incoming.removeValue(pos());
					configure(build.pos());
					build.incoming.add(pos());
				} else if (build.getLink() == null) {
					build.configure(pos());
					incoming.add(build.pos());
				}
			} else return true;

			new SpinGraph().mergeFlood(this);

			return false;
		}
		
		@Override
		public float ratioScl(HasSpin from) {
			return (from == getLink() || (from instanceof AxleBridgeBuild bridge && bridge.getLink() == this) ? (ratioScl / ((AxleBridge) from.asBuilding().block).ratioScl) : 1);
		}
		
		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);

			link = read.i();

			if (revision == 1) {
				byte size = read.b();

				for (int i = 0; i < size; i++) {
					incoming.add(read.i());
				}
			}
		}

		@Override
		public void update() {
			super.update();

			// changes
			if (getLink() != null) getLink().incoming.addUnique(pos());

			new IntSeq(incoming).each(i -> {
				Building next = Vars.world.build(i);

				if (!(next instanceof AxleBridgeBuild build) || !build.isValid() || build.link != pos()) incoming.removeValue(i);
			});
			incoming.each(i -> {
				if (Vars.world.build(i) instanceof AxleBridgeBuild build && build.spinGraph() != spinGraph()) new SpinGraph().mergeFlood(this);
			});

			if (getLink() != null && getLink().spinGraph() != spinGraph()) new SpinGraph().mergeFlood(this);
		}

		@Override public byte version() {
			return 1;
		}

		@Override
		public void write(Writes write) {
			super.write(write);

			write.i(link);

			write.b(incoming.size);
			incoming.each(write::i);
		}
	}
}
