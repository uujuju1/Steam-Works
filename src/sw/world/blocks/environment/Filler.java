package sw.world.blocks.environment;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import sw.util.*;

public class Filler extends Block {
	public Seq<Block[]> entries = new Seq<>();
	public TextureRegion topRegion, armRegion;
	public float armSpeed = 0.014f;
	public float constructTime = 150f;
	public int targetArea = 8;

	public Filler(String name) {
		super(name);
		placeableLiquid = true;
		solid = destructible = update = true;
	}

	@Override
	public void drawOverlay(float x, float y, int rotation) {
		SWDraw.square(Pal.accent, x, y, 4f*targetArea + 4f*size);
	}

	@Override
	public void load() {
		super.load();
		topRegion = Core.atlas.find(name + "-top");
		armRegion = Core.atlas.find(name + "-arm");
	}

	@Override
	public void setStats() {
		super.setStats();
		stats.add(Stat.range, targetArea, StatUnit.blocksSquared);
	}
	@Override
	public void setBars() {
		super.setBars();
		addBar("progress", (FillerBuild e) -> new Bar("bar.progress", Pal.ammo, e::progress));
	}

	public class FillerBuild extends Building {
		public float time, totalTime, targetX = x, targetY = y;

		public Seq<Tile> targets() {
			Seq<Tile> out = new Seq<>();
			for(int i = -targetArea/2; i < targetArea/2 + 2; i++) {
				for(int j = -targetArea/2; j < targetArea/2 + 2; j++) {
					Tile nearbyTile = tile.nearby(i, j);
					if (nearbyTile != null) {
						out.add(nearbyTile);
					}
				}
			}
			return out.removeAll(b -> {
				for (Block[] entry: entries) { 
					if (b.floor() == entry[0]) return false;
				}
				return true;
			});
		}

		public void swap(Tile tile) {
			if (!Vars.net.client()) {
				for (Block[] entry : entries) {
					if (tile.floor() == entry[0]) {
						tile.setFloorNet(entry[1], Blocks.air);
						consume();
						break;
					}
				}
			}
		}

		@Override
		public void updateTile() {
			Seq<Tile> targets = targets();
			Tile target = null;
			if (!targets.isEmpty()) {
				target = targets.first();
				targetX = Mathf.approachDelta(targetX, target.worldx(), armSpeed);
				targetY = Mathf.approachDelta(targetY, target.worldy(), armSpeed);
			} else {
				targetX = Mathf.approachDelta(targetX, x, armSpeed);
				targetY = Mathf.approachDelta(targetY, y, armSpeed);
			}

			if (efficiency > 0 && target != null) {
				time += getProgressIncrease(constructTime);
				if (time >= 1f) {
					swap(target);
					time %= 1f;
				}
			}
			
            Log.info("Target Coordinates: (@, @)", targetX, targetY);
		}

		@Override
		public void draw() {
			super.draw();
			Draw.z(Layer.blockOver);
			Tmp.v1.set(x, y).lerp(targetX, targetY, 0.5f);
			Draw.rect(armRegion, Tmp.v1.x, Tmp.v1.y, Mathf.dst(x, y, targetX, targetY), 8f, Angles.angle(x, y, targetX, targetY));
			Draw.rect(topRegion, x, y, 0);
			Draw.rect(topRegion, targetX, targetY, 0);
			Draw.z(Layer.block);
		}

		@Override public float progress() {
			return time;
		}
		@Override public float totalProgress() {
			return totalTime;
		}
	}
}
