package sw.world.blocks.vibration;

import arc.*;
import arc.graphics.g2d.*;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

import static sw.util.SWDraw.*;

public class VibrationWire extends Block {
	public VibrationConfig vibrationConfig = new VibrationConfig();
	public TextureRegion[] regions;

	public VibrationWire(String name) {
		super(name);
		solid = destructible = update = sync = true;
	}

	@Override public void drawOverlay(float x, float y, int rotation) {
		if (vibrationConfig.outputsVibration) Drawf.dashCircle(x, y, vibrationConfig.range, Pal.accent);
	}

	@Override
	public void setStats() {
		super.setStats();
		vibrationConfig.addStats(stats);
	}

	@Override
	public void load() {
		super.load();
		regions = getRegions(Core.atlas.find(name + "-tiles"), 4, 1, 32);
	}

	public class VibrationWireBuild extends Building implements HasVibration {
		VibrationModule vibration = new VibrationModule();

		public int tiling;

		@Override
		public boolean connects(HasVibration to) {
			return to instanceof VibrationWireBuild ?
			  (front() == to || back() == to) && (to.front() == this || to.back() == this) :
				to != null && (front() == to || back() == to) && to.connects(this);
		}

		@Override
		public void draw() {
			float rot = rotate ? (90 + rotdeg()) % 180 - 90 : 0;
			Draw.rect(regions[tiling], x, y, rot);
		}

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
			tiling = 0;
			boolean inverted = rotation == 1 || rotation == 2;
			if (front() instanceof HasVibration front && connects(front)) tiling |= inverted ? 2 : 1;
			if (back() instanceof HasVibration back && connects(back)) tiling |= inverted ? 1 : 2;
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
