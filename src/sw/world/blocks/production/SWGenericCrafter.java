package sw.world.blocks.production;

import arc.audio.*;
import arc.func.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.ctype.*;
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

public class SWGenericCrafter extends AttributeCrafter {
	public SpinConfig spinConfig;

	public boolean researchConsumers = true;

	public boolean hasAttribute = false;

	public boolean consumerScaleEfficiency = true;

	public float outputRotation = -1;
	public float outputRotationForce = 0;
	public boolean speedScales = false;
	public boolean forceScales = false;

	public Sound craftSound = Sounds.none;
	public float craftSoundVolume = 1f;
	
	public float ambientSoundPitch = 1f;

	public SWGenericCrafter(String name) {
		super(name);
		displayEfficiency = false;
	}

	@Override public boolean canPlaceOn(Tile tile, Team team, int rotation) {
		return super.canPlaceOn(tile, team, rotation) || !hasAttribute;
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
	public void getDependencies(Cons<UnlockableContent> cons){
		//just requires items
		for(ItemStack stack : requirements){
			cons.get(stack.item);
		}

		if (researchConsumers) for(var c : consumeBuilder){
			if(c.optional) continue;

			if(c instanceof ConsumeItems i){
				for(ItemStack stack : i.items){
					cons.get(stack.item);
				}
			}
		}
	}

	@Override
	public void setBars() {
		super.setBars();
		if (spinConfig != null) spinConfig.addBars(this);

		if (displayEfficiency && !hasAttribute) removeBar("efficiency");
	}

	@Override
	public void setStats() {
		super.setStats();
		if (spinConfig != null) spinConfig.addStats(stats);
		
		if (spinConfig != null && outputRotation > 0 && outputRotationForce > 0) {
			stats.add(SWStat.spinOutput, StatValues.number(outputRotation * 10f, SWStat.spinMinute));
			stats.add(SWStat.spinOutputForce, StatValues.number(outputRotationForce * 600f, SWStat.force));
		}

		if (!hasAttribute) stats.remove(baseEfficiency <= 0.0001f ? Stat.tiles : Stat.affinities);
	}

	public class SWGenericCrafterBuild extends AttributeCrafterBuild implements HasSpin {
		public SpinModule spin;

		@Override
		public void craft() {
			super.craft();
			craftSound.at(x, y, 1f, craftSoundVolume);
		}
		
		@Override
		public Building create(Block block, Team team) {
			if (spinConfig != null) spin = new SpinModule();
			return super.create(block, team);
		}

		@Override public void drawSelect() {
			if (spin != null) spinConfig.drawPlace(block, tileX(), tileY(), rotation, true);
		}

		@Override
		public float efficiencyMultiplier() {
			float mul = 1f;
			if (consumerScaleEfficiency) for(Consume cons : consumers) {
				mul *= cons.efficiencyMultiplier(this);
			}
			return (hasAttribute ? super.efficiencyMultiplier() : 1f) * mul;
		}

		@Override public float getForce() {
			return (efficiency > 0 && outputRotation > 0 && outputRotationForce > 0) ? outputRotationForce * (forceScales ? efficiency : 1) * warmup / getRatio() : 0;
		}
		@Override public float getTargetSpeed() {
			return (efficiency > 0 && outputRotation > 0 && outputRotationForce > 0) ? outputRotation * (speedScales ? efficiency : 1) * warmup * getRatio() : 0;
		}
		
		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);

			if (spinConfig != null) (spin == null ? new SpinModule() : spin).read(read);
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
		public boolean outputsSpin() {
			return outputRotation > 0 && outputRotationForce > 0;
		}
		
		@Override
		public void update() {
			if ((this.timeScaleDuration -= Time.delta) <= 0.0F || !this.block.canOverdrive) {
				this.timeScale = 1.0F;
			}
			
			if (!Vars.headless && this.block.ambientSound != Sounds.none && this.shouldAmbientSound()) {
				Vars.control.sound.loop(this.block.ambientSound, this,this.block.ambientSoundVolume * this.ambientVolume(), ambientSoundPitch);
			}
			
			this.updateConsumption();
			if (this.enabled || !this.block.noUpdateDisabled) {
				this.updateTile();
			}
		}

		@Override
		public void write(Writes write) {
			super.write(write);
			
			if (spinConfig != null) spin.write(write);
		}
	}
}
