package sw.world.blocks.vibration;

import arc.*;
import arc.graphics.g2d.*;
import sw.world.interfaces.*;

import static sw.util.SWDraw.*;

public class VibrationDistributor extends VibrationWire {
	public TextureRegion[] regions;

	public VibrationDistributor(String name) {
		super(name);
	}

	@Override
	public void init() {
		super.init();
		configurable = false;
	}

	@Override
	public void load() {
		super.load();
		regions = getRegions(Core.atlas.find(name + "-tiles"), 4, 4, 32);
	}

	@Override public void drawOverlay(float x, float y, int rotation) {}

	public class VibrationDistributorBuild extends VibrationWireBuild {
		public int tiling;

		@Override
		public void draw() {
			Draw.rect(regions[tiling], x, y, 0);
		}

		@Override
		public void onProximityUpdate() {
			super.onProximityUpdate();
			vibration().links.each(link -> {
				if (link.endBuild() != getVibrationLink() && link.valid()) removeVibrationLink(link.other(this));
				if (!link.valid()) {
					vibration().links.remove(link);
					vGraph().removeLink(link);
				}
			});
			for (HasVibration build : proximity.select(b -> b instanceof HasVibration).map(b -> (HasVibration) b)) {
				if (build.vConfig().acceptsVibration || build.vConfig().outputsVibration) createVibrationLink(build);
			}
			tiling = 0;
			for (int i = 0; i < 4; i++) {
				if (nearby(i) instanceof HasVibration b && getVibrationLinks().contains(b)) tiling |= 1 << i;
			}
		}
	}
}
