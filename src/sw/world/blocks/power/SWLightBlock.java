package sw.world.blocks.power;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.consumers.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import sw.world.graph.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class SWLightBlock extends Block {
	public @Nullable SpinConfig spinConfig;
	
	public DrawBlock drawer = new DrawDefault();
	
	public float lightOpacity = 0.8f;
	public float warmupSpeed = 0.014f;
	public float consumeTime = 60f;
	
	public boolean scaleEfficiency = true;
	
	public Effect consumeEffect = Fx.none;
	public Effect updateEffect = Fx.none;
	public float updateEffectChance = 0.01f;
	public float updateEffectSpread = 0f;
	
	public SWLightBlock(String name) {
		super(name);
		solid = true;
		emitLight = true;
		destructible = true;
		update = true;
	}
	
	@Override
	public void drawOverlay(float x, float y, int rotation) {
		Drawf.dashCircle(x, y, lightRadius, Pal.accent);
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
		stats.timePeriod = consumeTime;
		super.setStats();
		
		stats.add(Stat.productionTime, consumeTime/60f, StatUnit.seconds);
		stats.add(Stat.range, lightRadius / 8f, StatUnit.blocks);
		if (spinConfig != null) spinConfig.addStats(stats);
	}
	
	public class SWLightBuild extends Building implements HasSpin {
		public @Nullable SpinModule spin;
		
		public float warmup;
		public float progress;
		public float totalProgress;
		
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
			Drawf.light(x, y, lightRadius * warmup, lightColor, lightOpacity);
		}
		@Override public void drawSelect() {
			if (spinConfig != null) spinConfig.drawPlace(block, tileX(), tileY(), rotation, true);
		}
		
		@Override
		public float efficiencyScale() {
			float mul = 1f;
			if (scaleEfficiency) for (Consume cons : consumers) {
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
			return progress;
		}
		
		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);

			if (spinConfig != null) (spin == null ? new SpinModule() : spin).read(read);

			warmup = read.f();
			progress = read.f();
			totalProgress = read.f();
		}
		
		@Override public float totalProgress() {
			return totalProgress;
		}
		
		@Override
		public void updateTile() {
			warmup = Mathf.approachDelta(warmup, efficiency, warmupSpeed);
			totalProgress += Time.delta * warmup;
			
			if (efficiency > 0) {
				progress += getProgressIncrease(consumeTime);
				
				if (Mathf.chance(updateEffectChance)) {
					updateEffect.at(x + Mathf.range(updateEffectSpread), y + Mathf.range(updateEffectSpread));
				}
				
				if (progress >= 1f) {
					progress %= 1f;
					consumeEffect.at(x, y);
					consume();
				}
			}
		}
		
		@Override public float warmup() {
			return Mathf.clamp(warmup);
		}
		
		@Override
		public void write(Writes write) {
			super.write(write);

			if (spinConfig != null) spin.write(write);

			write.f(warmup);
			write.f(progress);
			write.f(totalProgress);
		}
	}
}
