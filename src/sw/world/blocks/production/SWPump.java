package sw.world.blocks.production;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.entities.units.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;
import sw.world.graph.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

import static mindustry.Vars.*;

public class SWPump extends Pump {
	public SpinConfig spinConfig;

	public ObjectFloatMap<Liquid> multipliers = new ObjectFloatMap<>();

	public boolean consumerScaleEfficiency = true;

	public SWPump(String name) {
		super(name);
	}

	@Override
	public boolean canPlaceOn(Tile tile, Team team, int rotation) {
		if(isMultiblock()){
			Liquid last = null;
			for(Tile other : tile.getLinkedTilesAs(this, tempTiles)){
				if(other.floor().liquidDrop == null) continue;
				if(other.floor().liquidDrop != last && last != null) return false;
				last = other.floor().liquidDrop;
			}
			return last != null && multipliers.get(last, 1) > 0;
		}else{
			return tile != null && tile.floor().liquidDrop != null && multipliers.get(tile.floor().liquidDrop, 1) > 0;
		}
	}

	@Override
	public void drawPlace(int x, int y, int rotation, boolean valid) {
		drawPotentialLinks(x, y);
		drawOverlay(x * tilesize + offset, y * tilesize + offset, rotation);

		Tile tile = world.tile(x, y);

		if(tile != null){
			float amount = 0f;
			Liquid liquidDrop = null;

			for(Tile other : tile.getLinkedTilesAs(this, tempTiles)){
				if(canPump(other)){
					if(liquidDrop != null && other.floor().liquidDrop != liquidDrop){
						liquidDrop = null;
						break;
					}
					liquidDrop = other.floor().liquidDrop;
					amount += other.floor().liquidMultiplier;
				}
			}

			if(liquidDrop != null){
				float width = drawPlaceText(Core.bundle.formatFloat(multipliers.get(liquidDrop, 1) > 0 ? "bar.pumpspeed" : "bar.sw-better-pump", amount * pumpAmount * 60f * multipliers.get(liquidDrop, 1), 0), x, y, valid);
				float dx = x * tilesize + offset - width/2f - 4f, dy = y * tilesize + offset + size * tilesize / 2f + 5, s = iconSmall / 4f;
				float ratio = (float)liquidDrop.fullIcon.width / liquidDrop.fullIcon.height;
				Draw.mixcol(Color.darkGray, 1f);
				Draw.rect(liquidDrop.fullIcon, dx, dy - 1, s * ratio, s);
				Draw.reset();
				Draw.rect(liquidDrop.fullIcon, dx, dy, s * ratio, s);
			}
		}
	}

	@Override
	public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
		if (spinConfig != null) spinConfig.drawPlace(this, plan.x, plan.y, plan.rotation, true);
		drawer.drawPlan(this, plan, list);
	}

	@Override
	public void drawPlanConfigTop(BuildPlan plan, Eachable<BuildPlan> list) {
		drawer.drawPlan(this, plan, list);
	}

	@Override
	public void init() {
		super.init();
		if (spinConfig != null) spinConfig.init(this);
	}

	@Override
	public void setBars() {
		super.setBars();
		if (spinConfig != null) spinConfig.addBars(this);
	}

	@Override
	public void setStats() {
		super.setStats();

		stats.remove(Stat.output);
		stats.add(SWStat.baseOutput, 60f * pumpAmount * size * size, StatUnit.liquidSecond);
		stats.add(Stat.output, StatValues.liquidEffMultiplier(l -> Mathf.floor(multipliers.get(l, 1f) * 100f) / 100f, 0, l -> multipliers.containsKey(l)));

		if (spinConfig != null) spinConfig.addStats(stats);
	}

	public class SWPumpBuild extends PumpBuild implements HasSpin {
		public SpinModule spin;

		@Override
		public Building create(Block block, Team team) {
			if (spinConfig != null) spin = new SpinModule();
			return super.create(block, team);
		}

		@Override public void drawSelect() {
			super.drawSelect();
			if (spinConfig != null) spinConfig.drawPlace(block, tileX(), tileY(), rotation, true);
		}

		@Override
		public float efficiencyScale() {
			float mul = 1f;
			if (consumerScaleEfficiency) for(Consume cons : nonOptionalConsumers) {
				mul *= cons.efficiencyMultiplier(this);
			}
			return mul;
		}

		@Override
		public void onProximityUpdate() {
			super.onProximityUpdate();

			if (spin != null) new SpinGraph().mergeFlood(this);
		}

		@Override
		public void onProximityRemoved() {
			super.onProximityRemoved();

			if (spin != null) spinGraph().removeBuild(this);
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);

			if (spinConfig != null && revision > 0) (spin == null ? new SpinModule() : spin).read(read);
		}

		@Override
		public byte version() {
			return 1;
		}

		@Override
		public void write(Writes write) {
			super.write(write);

			if (spinConfig != null) spin.write(write);
		}
	}
}
