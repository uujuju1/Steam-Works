package sw.world.blocks.production;

import arc.audio.*;
import arc.math.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.world.blocks.production.*;
import sw.world.graph.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class SWGenericCrafter extends GenericCrafter {
	public SpinConfig spinConfig = new SpinConfig();

	public float outputRotation = -1;
	public float outputRotationForce = 0;

	public Sound craftSound = Sounds.none;
	public float craftSoundVolume = 1f;

	public Effect updateEffectStatic = Fx.none;

	public SWGenericCrafter(String name) {
		super(name);
	}

//	public void consumeGas(float gas, float minP, float maxP, float minE, float maxE, boolean cont) {
//		consumeGas(new ConsumeGas() {{
//			amount = gas;
//			minPressure = minP;
//			maxPressure = maxP;
//			minEfficiency = minE;
//			maxEfficiency = maxE;
//			continuous = cont;
//		}});
//	}
//	public void consumeGas(ConsumeGas consumer) {
//		consume(consumer);
//	}

	@Override
	public void setBars() {
		super.setBars();
		spinConfig.addBars(this);
	}

	@Override
	public void setStats() {
		super.setStats();
		spinConfig.addStats(stats);
		if (outputRotation > 0) stats.add(SWStat.outputGas, Strings.fixed(outputRotation, 2), SWStat.gasUnit);
	}

	public class SWGenericCrafterBuild extends GenericCrafterBuild implements HasSpin {
		public SpinModule spin = new SpinModule();

		@Override
		public void craft() {
			super.craft();
			craftSound.at(x, y, 1f, craftSoundVolume);
		}

		@Override public float getForce() {
			return outputRotationForce * spinSection().ratio;
		}
		@Override public float getTargetSpeed() {
			return efficiency > 0 ? outputRotation * spinSection().ratio : 0f;
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
//			if (outputRotation > 0) spinGraph().rotation += outputRotation * Time.delta * warmup / spinSection().ratio;
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
