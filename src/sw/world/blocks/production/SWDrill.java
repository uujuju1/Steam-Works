package sw.world.blocks.production;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.entities.units.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;
import mindustry.world.consumers.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import sw.world.graph.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class SWDrill extends Drill {
	public SpinConfig spinConfig;
	
	public boolean consumerScaleEfficiency = true;
	
	public DrawBlock drawer = new DrawDefault();
	
	public SWDrill(String name) {
		super(name);
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
	public void getRegionsToOutline(Seq<TextureRegion> out) {
		drawer.getRegionsToOutline(this, out);
	}
	
	@Override
	public TextureRegion[] icons(){
		return drawer.finalIcons(this);
	}
	
	@Override
	public void load() {
		super.load();
		drawer.load(this);
	}
	
	@Override
	public void setBars() {
		super.setBars();
		if (spinConfig != null) spinConfig.addBars(this);
	}
	
	@Override
	public void setStats() {
		super.setStats();
		if (spinConfig != null) spinConfig.addStats(stats);
		
		if(liquidBoostIntensity != 1 && findConsumer(f -> f instanceof ConsumeLiquidBase && f.booster) instanceof ConsumeLiquidBase consBase){
			stats.remove(Stat.booster);
			stats.add(Stat.booster,
				StatValues.speedBoosters("{0}" + StatUnit.timesSpeed.localized(),
					consBase.amount,
					liquidBoostIntensity, false, consBase::consumes)
			);
		}
	}
	
	public class SWDrillBuild extends DrillBuild implements HasSpin {
		public SpinModule spin;
		
		@Override
		public Building create(Block block, Team team) {
			if (spinConfig != null) spin = new SpinModule();
			return super.create(block, team);
		}
		
		@Override public void draw() {
			drawer.draw(this);
		}
		@Override public void drawLight() {
			drawer.drawLight(this);
		}
		@Override public void drawSelect() {
			super.drawSelect();
			if (spinConfig != null) spinConfig.drawPlace(block, tileX(), tileY(), rotation, true);
		}
		
		@Override
		public float efficiencyScale() {
			float mul = 1f;
			if (consumerScaleEfficiency) for(Consume cons : consumers) {
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
		
		@Override public float progress() {
			return progress / getDrillTime(dominantItem);
		}
		
		@Override public float totalProgress() {
			return timeDrilled;
		}
		
		@Override
		public void updateTile() {
			if(timer(timerDump, dumpTime / timeScale)){
				dump(dominantItem != null && items.has(dominantItem) ? dominantItem : null);
			}
			
			if(dominantItem == null){
				return;
			}
			
			timeDrilled += warmup * delta();
			
			float delay = getDrillTime(dominantItem);
			
			if(items.total() < itemCapacity && dominantItems > 0 && efficiency > 0){
				float speed = Mathf.lerp(1f, liquidBoostIntensity, optionalEfficiency) * efficiency;
				
				lastDrillSpeed = (speed * dominantItems * warmup) / delay;
				warmup = Mathf.approachDelta(warmup, 1f, warmupSpeed / efficiency);
				progress += delta() * dominantItems * speed * warmup;
				
				if(Mathf.chanceDelta(updateEffectChance * warmup))
					updateEffect.at(x + Mathf.range(size * 2f), y + Mathf.range(size * 2f));
			}else{
				lastDrillSpeed = 0f;
				warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
				return;
			}
			
			if(dominantItems > 0 && progress >= delay && items.total() < itemCapacity){
				int amount = (int)(progress / delay);
				for(int i = 0; i < amount; i++){
					offload(dominantItem);
				}
				
				progress %= delay;
				
				if(wasVisible && Mathf.chanceDelta(drillEffectChance * warmup)) drillEffect.at(x + Mathf.range(drillEffectRnd), y + Mathf.range(drillEffectRnd), dominantItem.color);
			}
		}
		
		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			
			if (spin != null) spin.read(read);
		}
		
		@Override public float warmup() {
			return warmup;
		}
		
		@Override
		public void write(Writes write) {
			super.write(write);
			
			if (spin != null) spin.write(write);
		}
	}
}
