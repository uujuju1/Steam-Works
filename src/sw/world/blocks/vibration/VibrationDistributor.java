package sw.world.blocks.vibration;

import arc.struct.*;
import arc.util.io.*;
import mindustry.gen.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class VibrationDistributor extends VibrationWire {

	public VibrationDistributor(String name) {
		super(name);
	}

	@Override public void drawOverlay(float x, float y, int rotation) {}

	public class VibrationDistributorBuild extends Building implements HasVibration {
		VibrationModule vibration = new VibrationModule();

		@Override
		public void onProximityAdded() {
			super.onProximityAdded();
			vibrationGraph().addBuild(this);
		}
		@Override
		public void onProximityRemoved() {
			super.onProximityRemoved();
			vibrationGraph().removeBuild(this, true);
		}
		@Override
		public void onProximityUpdate() {
			super.onProximityUpdate();
			vibrationGraph().removeBuild(this, false);
		}

		@Override public VibrationModule vibration() {
			return vibration;
		}
		@Override public VibrationConfig vibrationConfig() {
			return vibrationConfig;
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
