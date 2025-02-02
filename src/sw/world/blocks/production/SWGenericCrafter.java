package sw.world.blocks.production;

import arc.audio.*;
import arc.func.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
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

public class SWGenericCrafter extends GenericCrafter {
	public SpinConfig spinConfig = new SpinConfig();

	public boolean researchConsumers = true;

	public float outputRotation = -1;
	public float outputRotationForce = 0;

	public Sound craftSound = Sounds.none;
	public float craftSoundVolume = 1f;

	public Effect updateEffectStatic = Fx.none;

	public SWGenericCrafter(String name) {
		super(name);
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
	public void drawPlace(int x, int y, int rotation, boolean valid) {
		super.drawPlace(x, y, rotation, valid);
		spinConfig.drawPlace(this, x, y, rotation, valid);
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
	}

	@Override
	public void setStats() {
		super.setStats();
		spinConfig.addStats(stats);
		if (outputRotation > 0 && outputRotationForce > 0) {
			stats.add(SWStat.spinOutput, StatValues.number(outputRotation * 10f, SWStat.spinMinute));
			stats.add(SWStat.spinOutputForce, StatValues.number(outputRotationForce * 600f, SWStat.spinMinuteSecond));
		}
	}

	public class SWGenericCrafterBuild extends GenericCrafterBuild implements HasSpin {
		public SpinModule spin = new SpinModule();

		@Override
		public void craft() {
			super.craft();
			craftSound.at(x, y, 1f, craftSoundVolume);
		}

		@Override public float getForce() {
			return (efficiency > 0 && outputRotation > 0 && outputRotationForce > 0) ? outputRotationForce / spinSection().ratio * warmup : 0;
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
