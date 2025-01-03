package sw.world.blocks.production;

import arc.audio.*;
import arc.math.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.world.blocks.production.*;
import sw.world.consumers.*;
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
	public void setBars() {
		super.setBars();
		spinConfig.addBars(this);
	}

	@Override
	public void setStats() {
		super.setStats();
		spinConfig.addStats(stats);
		if (outputRotation > 0 && outputRotationForce > 0) {
			stats.add(SWStat.spinOutput, Strings.autoFixed(outputRotation * 10f, 2), SWStat.spinMinute);
			stats.add(SWStat.spinOutputForce, Strings.autoFixed(outputRotationForce * 600f, 2), SWStat.spinMinuteSecond);
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
