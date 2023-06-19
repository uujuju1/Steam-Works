package sw.world.blocks.environment;

import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import sw.content.*;
import sw.util.*;

import static arc.struct.ObjectMap.*;
import static mindustry.Vars.*;

public class Filler extends Block {
	public UnitType unitType = SWUnitTypes.terra;
	public Entry<Block, Block>[] passes = new Entry[]{};
	public float buildTime = 60f;
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
	public void setStats() {
		super.setStats();
		stats.add(Stat.range, targetArea, StatUnit.blocksSquared);
	}

	public class FillerBuild extends Building {
		public Unit unit;
		public int unitId;

		public float time, totalTime,
		targetX, targetY;

		public Seq<Tile> targets() {
			Seq<Tile> out = new Seq<>();
			for(int i = -targetArea/2; i < targetArea/2 + 2; i++) {
				for(int j = -targetArea/2; j < targetArea/2 + 2; j++) {
					out.add(tile.nearby(i, j));
				}
			}
			return out.removeAll(b -> {
				for (Entry entry: passes) if (b.floor() == entry.key) return false;
				return true;
			});
		}
		public void swap(Tile tile) {
			if (time <= 1f) return;
			for (Entry<Block, Block> entry: passes) if (tile.floor() == entry.key) {
				tile.setFloor(entry.value.asFloor());
				Fx.itemTransfer.at(x, y, 0, unit);
				consume();
				time %= 1f;
				return;
			}
			time %= 1f;
		}

		@Override
		public void updateTile() {
			if (Vars.net.active()) return;
			Seq<Tile> targets = targets();
			Tile target;
			if (!targets.isEmpty()) {
				target = targets().first();
				targetX = target.worldx();
				targetY = target.worldy();
			}

			if(unit != null && (unit.dead || !unit.isAdded())) unit = null;
			if(unitId != -1) {
				unit = Groups.unit.getByID(unitId);
				if(unit == null || !net.client()) unitId = -1;
			}

			if(unit == null) {
				time+=edelta()/buildTime;
				if(time >= 1f && !net.client()) {
					unit = unitType.create(team);
					if(unit instanceof BuildingTetherc u) u.building(this);
					unit.set(x, y);
					unit.rotation = 90f;
					unit.add();
					Call.unitTetherBlockSpawned(tile, unit.id);
				}
			} else time += edelta() / constructTime;
		}

		@Override public float progress() {
			return time;
		}

		@Override
		public float totalProgress() {
			totalTime += Time.delta;
			return totalTime;
		}
	}
}
