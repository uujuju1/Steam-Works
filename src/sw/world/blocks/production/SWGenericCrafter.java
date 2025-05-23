package sw.world.blocks.production;

import arc.audio.*;
import arc.func.*;
import arc.math.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;
import sw.world.consumers.*;
import sw.world.graph.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class SWGenericCrafter extends AttributeCrafter {
	public SpinConfig spinConfig = new SpinConfig();

	public boolean researchConsumers = true;

	public boolean hasAttribute = false;

	public boolean consumerScaleEfficiency = true;

	public float outputRotation = -1;
	public float outputRotationForce = 0;

	public Sound craftSound = Sounds.none;
	public float craftSoundVolume = 1f;

	public Effect updateEffectStatic = Fx.none;

	public SWGenericCrafter(String name) {
		super(name);
		displayEfficiency = false;
	}

	@Override public boolean canPlaceOn(Tile tile, Team team, int rotation) {
		return super.canPlaceOn(tile, team, rotation) || !hasAttribute;
	}

	public void consumeSpin(float start, float end, Interp interp) {
		consumeSpin(new ConsumeRotation() {{
			startSpeed = start;
			endSpeed = end;
			curve = interp;
		}});
	}
	public void consumeSpin(ConsumeRotation consumer) {
		consume(consumer);
	}

	@Override
	public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
		spinConfig.drawPlace(this, plan.x, plan.y, plan.rotation, true);
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

		//also requires inputs
		if (researchConsumers) for(var c : consumeBuilder){
			if(c.optional) continue;

			if(c instanceof ConsumeItems i){
				for(ItemStack stack : i.items){
					cons.get(stack.item);
				}
			}
			//TODO: requiring liquid dependencies is usually a bad idea, because there is no reason to pump/produce something until you actually need it.
            /*else if(c instanceof ConsumeLiquid i){
                cons.get(i.liquid);
            }else if(c instanceof ConsumeLiquids i){
                for(var stack : i.liquids){
                    cons.get(stack.liquid);
                }
            }*/
		}
	}

	@Override
	public void setBars() {
		super.setBars();
		spinConfig.addBars(this);

		if (displayEfficiency && !hasAttribute) removeBar("efficiency");
	}

	@Override
	public void setStats() {
		super.setStats();
		spinConfig.addStats(stats);
		if (outputRotation > 0 && outputRotationForce > 0) {
			stats.add(SWStat.spinOutput, StatValues.number(outputRotation * 10f, SWStat.spinMinute));
			stats.add(SWStat.spinOutputForce, StatValues.number(outputRotationForce * 600f, SWStat.spinMinuteSecond));
		}

		if (!hasAttribute) stats.remove(baseEfficiency <= 0.0001f ? Stat.tiles : Stat.affinities);
	}

	public class SWGenericCrafterBuild extends AttributeCrafterBuild implements HasSpin {
		public SpinModule spin = new SpinModule();

		@Override
		public void craft() {
			super.craft();
			craftSound.at(x, y, 1f, craftSoundVolume);
		}

		@Override public void drawSelect() {
			spinConfig.drawPlace(block, tileX(), tileY(), rotation, true);
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
			return (efficiency > 0 && outputRotation > 0 && outputRotationForce > 0) ? outputRotationForce * warmup : 0;
		}
		@Override public float getTargetSpeed() {
			return (efficiency > 0 && outputRotation > 0 && outputRotationForce > 0) ? outputRotation / spinSection().ratio * warmup : 0f;
		}

		@Override public SpinModule spin() {
			return spin;
		}
		@Override public SpinConfig spinConfig() {
			return spinConfig;
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			spin.read(read);
		}

		@Override
		public void onProximityUpdate() {
			super.onProximityUpdate();

			new SpinGraph().mergeFlood(this);
		}

		@Override
		public void onProximityRemoved() {
			super.onProximityRemoved();
			spinGraph().remove(this, true);
		}
		
		@Override
		public boolean outputsSpin() {
			return outputRotation > 0 && outputRotationForce > 0;
		}

		@Override
		public void updateTile() {
			super.updateTile();
			if (efficiency > 0) {
				if(wasVisible && Mathf.chanceDelta(updateEffectChance)){
					updateEffectStatic.at(x, y);
				}
			}
		}

		@Override
		public void write(Writes write) {
			super.write(write);
			spin.write(write);
		}
	}
}
