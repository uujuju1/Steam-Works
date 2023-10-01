package sw.world.blocks.vibration;

import arc.util.io.*;
import mindustry.gen.*;
import mindustry.world.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class VibrationWire extends Block {
	public VibrationConfig vibrationConfig = new VibrationConfig();

	public VibrationWire(String name) {
		super(name);
		solid = destructible = update = sync = true;
		configurable = true;
	}

	public class VibrationWireBuild extends Building implements HasVibration {
		VibrationModule vibration = new VibrationModule();

		@Override public VibrationModule vibration() {
			return vibration;
		}
		@Override public VibrationConfig vConfig() {
			return vibrationConfig;
		}

		@Override
		public void draw() {
			drawLink();
			super.draw();
		}

		@Override
		public void onProximityAdded() {
			super.onProximityAdded();
			vGraph().add(this);
			vGraph().updateBuilds();
		}
		@Override
		public void onProximityRemoved() {
			super.onProximityRemoved();
			if (getVibrationLink() != null) vibrationUnlink();
			vGraph().links.removeAll(vibration().links);
		}

		@Override public boolean onConfigureBuildTapped(Building other) {
			return configVibrationLink(other);
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			vibration.read(read);
		}

		@Override
		public void write(Writes write) {
			super.write(write);
			vibration.write(write);
		}
	}
}
