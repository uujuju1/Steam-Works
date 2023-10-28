package sw.world.blocks.environment;

import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import sw.content.*;
import sw.util.*;

import static mindustry.Vars.*;

public class Filler extends Block {
	public UnitType unitType = SWUnitTypes.terra;
	public Seq<Block[]> entries = new Seq<>();
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
	@Override
	public void setBars() {
		super.setBars();
		addBar("progress", (FillerBuild e) -> new Bar("bar.progress", Pal.ammo, e::progress));
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
				for (Block[] entry: entries) if (b.floor() == entry[0]) return false;
				return true;
			});
		}
		public void swap(Tile tile) {
			if (time <= 1f) return;
			for (Block[] entry : entries) if (tile.floor() == entry[0]) {
				tile.setFloor(entry[1].asFloor());
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
				totalTime += Time.delta;
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

		@Override
		public void draw() {
			super.draw();
			if (unit == null) Draw.draw(Layer.blockOver, () -> Drawf.construct(this, unitType, 0, time, efficiency * Vars.state.rules.unitBuildSpeedMultiplier, totalTime));
		}

		@Override public float progress() {
			return time;
		}
		@Override public float totalProgress() {
			return totalTime;
		}
	}
}
