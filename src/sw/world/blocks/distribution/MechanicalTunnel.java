package sw.world.blocks.distribution;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.input.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import sw.util.*;

public class MechanicalTunnel extends Block {
	public TextureRegion[] regions = new TextureRegion[4];

	public int startMaxDistance = 10;
	public ObjectIntMap<Block> floors = new ObjectIntMap<>();

	public MechanicalTunnel(String name) {
		super(name);
		update = true;
		solid = true;
		rotate = true;
		hasItems = true;
		group = BlockGroup.transportation;
		noUpdateDisabled = true;
		priority = TargetPriority.transport;
		drawArrow = false;
	}

	@Override
	public void changePlacementPath(Seq<Point2> points, int rotation) {
		Placement.calculateNodes(points, this, rotation, (point, other) -> Math.max(Math.abs(point.x - other.x), Math.abs(point.y - other.y)) <= maxDistance(Vars.world.tile(points.first().x, points.first().y), rotation) - 1);
		points.removeAll(point -> points.indexOf(point) > 1);
	}

	@Override
	public void drawPlace(int x, int y, int rotation, boolean valid) {
		super.drawPlace(x, y, rotation, valid);
		Point2 end = new Point2(maxDistance(Vars.world.tile(x, y), rotation) - 1, 0).rotate(rotation).add(x, y);
		SWDraw.linePoint(Pal.accent, Pal.gray, x * 8f, y * 8f, end.x * 8f, end.y * 8f);
	}

	public int getDistance(Block floor) {
		return floors.get(floor, 0);
	}

	@Override
	public void load() {
		super.load();
		for (int i = 0; i < 4; i++) {
			regions[i] = Core.atlas.find(name + "-rotation" + (i + 1));
		}
	}

	public int maxDistance(Tile from, int rotation) {
		int distance = startMaxDistance;
		int current = 1;
		while(current > 0) {
			if (from == null) return 0;
			Point2 tile = new Point2(distance, 0).rotate(rotation);
			if (from.nearby(tile) != null) {
				current --;
				current += getDistance(from.nearby(tile).floor());
				distance += getDistance(from.nearby(tile).floor());
			} else {
				distance--;
				tile = new Point2(distance, 0).rotate(rotation);
				if (from.nearby(tile) != null) {
					distance--;
					break;
				}
			}
		}
		return distance;
	}

	public class MechanicalTunnelBuild extends Building {

		@Override
		public boolean acceptItem(Building source, Item item) {
			return this.items.get(item) < this.getMaximumAccepted(item) && getLink() != null && !getLink().isSending(item) && back() == source;
		}

		@Override
		public boolean canDump(Building to, Item item) {
			return super.canDump(to, item) && !isSending(item) && back() == to;
		}

		@Override
		public void draw() {
			Draw.rect(regions[rotation], x, y, 0);
		}
		@Override
		public void drawSelect() {
			Point2 end;
			SWDraw.square(team.color, x, y, block.size * 6f, 0f);
			if (getLink() == null) {
				end = new Point2(maxDistance(tile, rotation) - 1, 0).rotate(rotation).add(tileX(), tileY());
			} else {
				end = new Point2(getLink().tileX(), getLink().tileY());
				SWDraw.square(team.color, getLink().x, getLink().y, getLink().block.size * 6f, 0f);
			}
			SWDraw.linePoint(team.color, Pal.gray, x, y, end.x * 8f, end.y * 8f);
		}

		public @Nullable MechanicalTunnelBuild getLink() {
			for (int i = 0; i < maxDistance(tile, rotation); i++) {
				if (
					tile.nearby(new Point2(i, 0).rotate(rotation)).build instanceof MechanicalTunnelBuild build &&
					(build.rotation + 2) % 4 == rotation
				) {
					return build;
				}
			}
			return null;
		}

		public boolean isSending(Item item) {
			return back() != null && !(back() instanceof MechanicalTunnelBuild) && !back().acceptItem(this, item);
		}

		@Override
		public void updateTile() {
			dump();
			if (getLink() != null && items != null) {
				if (items.any() && getLink().items.total() < getLink().block.itemCapacity) {
					Item next = items.take();
					if (next != null && isSending(next)) {
						getLink().handleItem(this, next);
					}
				}
			}
		}
	}
}
