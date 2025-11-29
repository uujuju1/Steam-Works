package sw.world.blocks.power;

import arc.*;
import arc.audio.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.world.meta.*;
import sw.world.meta.*;

import static mindustry.Vars.*;

public class HandCrank extends AxleBlock {
	public float torque = 1f/600f;
	public float speed = 1f/10f;
	public float spinTime = 60f;
	
	public Sound tapSound = Sounds.door;
	
	public HandCrank(String name) {
		super(name);
		consumesTap = true;
		copyConfig = false;
		
		config(Float.class, (HandCrankBuild build, Float value) -> build.time = value);
	}
	
	@Override
	public void setStats() {
		super.setStats();
		
		stats.add(SWStat.spinOutput, StatValues.number(speed * 10f, SWStat.spinMinute));
		stats.add(SWStat.spinOutputForce, StatValues.number(torque * 600f, SWStat.force));
	}
	
	public class HandCrankBuild extends AxleBlockBuild {
		public float time = 0;
		
		@Override public float getForce() {
			return time <= 0f ? 0f : torque * getRatio();
		}
		@Override public float getTargetSpeed() {
			return time <= 0f ? 0f : speed * getRatio();
		}
		
		@Override public Graphics.Cursor getCursor() {
			return interactable(player.team()) ? Graphics.Cursor.SystemCursor.hand : Graphics.Cursor.SystemCursor.arrow;
		}
		
		@Override public boolean outputsSpin() {
			return true;
		}
		
		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			time = read.f();
		}
		
		@Override
		public void tapped() {
			configure(spinTime);
			tapSound.at(this);
		}
		
		@Override public void updateTile() {
			if (time > 0) time -= Time.delta;
		}
		
		@Override
		public void write(Writes write) {
			super.write(write);
			write.f(time);
		}
	}
}
