package sw.world.blocks.distribution;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import sw.util.*;

public class MechanicalTunnel extends Block {
	public TextureRegion[] regions = new TextureRegion[4];
	public TextureRegion priorityRegion;

	public int startMaxDistance = 10;
	public ObjectIntMap<Block> floors = new ObjectIntMap<>();

	public MechanicalTunnel(String name) {
		super(name);
		update = true;
		solid = true;
		rotate = true;
		hasItems = true;
		saveConfig = copyConfig = true;
		group = BlockGroup.transportation;
		noUpdateDisabled = true;
		priority = TargetPriority.transport;
		drawArrow = false;

		config(Boolean.class, (MechanicalTunnelBuild build, Boolean priority) -> {
			build.prioritized = priority;
		});
	}

	@Override
	public void load() {
		super.load();
		for (int i = 0; i < 4; i++) {
			regions[i] = Core.atlas.find(name + "-rotation" + (i + 1));
		}
		priorityRegion = Core.atlas.find(name + "-priority");
	}

	@Override
	public void drawPlace(int x, int y, int rotation, boolean valid) {
		super.drawPlace(x, y, rotation, valid);
		Point2 end = new Point2(maxDistance(Vars.world.tile(x, y), rotation), 0).rotate(rotation).add(x, y);
		Drawf.dashLine(Pal.accent, x * 8f, y * 8f, end.x * 8f, end.y * 8f);
	}

	public int getDistance(Block floor) {
		return floors.get(floor, 0);
	}
	public int maxDistance(Tile from, int rotation) {
		int distance = startMaxDistance;
		int current = 1;
		while(current > 0) {
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
		public boolean prioritized;

		public @Nullable MechanicalTunnelBuild getLink() {
			for (int i = 0; i < maxDistance(tile, rotation); i++) {
				if (
					tile.nearby(new Point2(i, 0).rotate(rotation)).build instanceof MechanicalTunnelBuild build &&
					(build.rotation + 2) % 4 == rotation
				) {
					configure(!build.prioritized);
					return build;
				}
			}
			return null;
		}

		@Override public Boolean config() {
			return prioritized;
		}

		@Override
		public void tapped() {
			configure(true);
			if (getLink() != null) getLink().configure(false);
		}

		@Override
		public void draw() {
			Draw.rect(regions[rotation], x, y, 0);
			if (prioritized) Draw.rect(priorityRegion, x, y, rotdeg());
		}
		@Override
		public void drawSelect() {
			Point2 end;
			SWDraw.square(Pal.accent, x, y, block.size * 6f, 0f);
			if (getLink() == null) {
				end = new Point2(maxDistance(tile, rotation), 0).rotate(rotation).add(tileX(), tileY());
			} else {
				end = new Point2(getLink().tileX(), getLink().tileY());
				SWDraw.square(Pal.accent, getLink().x, getLink().y, getLink().block.size * 6f, 0f);
			}
			Drawf.dashLine(Pal.accent, x, y, end.x * 8f, end.y * 8f);
		}

		@Override
		public boolean acceptItem(Building source, Item item) {
			return this.items.get(item) < this.getMaximumAccepted(item) && getLink() != null && prioritized && back() == source;
		}

		@Override
		public boolean canDump(Building to, Item item) {
			return super.canDump(to, item) && back() == to;
		}

		@Override
		public void updateTile() {
			if (!prioritized) dump();
			if (getLink() != null && items != null && prioritized) {
				if (items.any() && getLink().items.total() < getLink().block.itemCapacity) {
					Item next = items.take();
					if (next != null) {
						getLink().handleItem(this, next);
					}
				}
			}
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			prioritized = read.bool();
		}
		@Override
		public void write(Writes write) {
			super.write(write);
			write.bool(prioritized);
		}
	}
}
