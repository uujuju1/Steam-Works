package sw.world.blocks.vibration;

import arc.graphics.g2d.*;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import sw.util.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class VibrationWire extends Block {
	public VibrationConfig vibrationConfig = new VibrationConfig();

	public VibrationWire(String name) {
		super(name);
		solid = destructible = update = sync = true;
	}

	@Override
	public void setStats() {
		super.setStats();
		vibrationConfig.addStats(stats);
	}

	@Override
	public void init() {
		super.init();
		configurable = vibrationConfig.outputsVibration;
		clipSize = vibrationConfig.range;
	}

	@Override public void drawOverlay(float x, float y, int rotation) {
		if (vibrationConfig.outputsVibration) Drawf.dashCircle(x, y, vibrationConfig.range, Pal.accent);
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
		public void drawConfigure() {
			drawOverlay(x, y, 0);
			SWDraw.square(Pal.accent, x, y, block.size * 6f, 0f);
			getVibrationLinks().each(build -> {
				SWDraw.square(Pal.place, build.x(), build.y(), build.block().size * 6f, 0f);
			});
			Draw.reset();
		}

		@Override public boolean onConfigureBuildTapped(Building other) {
			return configVibrationLink(other);
		}

		@Override
		public void onProximityAdded() {
			super.onProximityAdded();
			vGraph().addBuild(this);
		}
		@Override
		public void onProximityRemoved() {
			super.onProximityRemoved();
			vibration().links.each(link -> {
				removeVibrationLink(link.other(this));
			});
			vGraph().removeBuild(this, false);
		}
		@Override
		public void onProximityUpdate() {
			super.onProximityUpdate();
			if (getVibrationLink() != null) createVibrationLink(getVibrationLink());
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
