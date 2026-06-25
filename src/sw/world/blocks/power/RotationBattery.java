package sw.world.blocks.power;

import arc.util.io.*;
import mindustry.world.meta.*;
import sw.world.meta.*;

public class RotationBattery extends AxleBlock {
	public float maxWindup;
	public float speed;
	public float outputForce;
	public float efficiencyScale = 0.9f;
	
	public RotationBattery(String name) {
		super(name);
	}

	@Override
	public void setStats() {
		super.setStats();

		stats.add(SWStat.outputDuration, maxWindup / speed / 60f, StatUnit.seconds);
		stats.add(SWStat.spinOutput, speed * 10f, SWStat.spinMinute);
		stats.add(SWStat.spinOutputForce, outputForce * 600f, SWStat.force);
		stats.add(Stat.maxEfficiency, efficiencyScale * 100f, StatUnit.percent);
	}

	public class RotationBatteryBuild extends AxleBlockBuild {
		public float wind;

		// TODO maybe?
//		@Override public float getResistance() {
//			return super.getResistance() * Mathf.pow(resistanceMagnitude, wind/resistanceScale);
//		}
		
		@Override public float progress() {
			return wind / maxWindup;
		}
		
		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			
			wind = read.f();
		}
		
		@Override public float totalProgress() {
			return wind / getRatio();
		}
		
		@Override
		public void write(Writes write) {
			super.write(write);
			
			write.f(wind);
		}
	}
}
