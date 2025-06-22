package sw.world.blocks.power;

import arc.math.*;
import arc.util.io.*;

public class RotationBattery extends AxleBlock {
	public float maxWindup;
	public float speed;
	public float resistanceScale;
	public float resistanceMagnitude;
	public float outputForce;
	
	public RotationBattery(String name) {
		super(name);
	}
	
	public class RotationBatteryBuild extends AxleBlockBuild {
		public float wind;
		
		@Override public float getResistance() {
			return super.getResistance() * Mathf.pow(resistanceMagnitude, wind/resistanceScale);
		}
		
		@Override public float progress() {
			return wind / maxWindup;
		}
		
		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			
			wind = read.f();
		}
		
		@Override public float totalProgress() {
			return wind / spinGraph().ratios.get(this, 1);
		}
		
		@Override
		public void write(Writes write) {
			super.write(write);
			
			write.f(wind);
		}
	}
}
