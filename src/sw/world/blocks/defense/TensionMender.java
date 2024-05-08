package sw.world.blocks.defense;

import mindustry.world.blocks.defense.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class TensionMender extends MendProjector {
	public TensionConfig tensionConfig = new TensionConfig();

	public TensionMender(String name) {
		super(name);
	}

	@Override
	public void setStats() {
		super.setStats();
		tensionConfig.addStats(stats);
	}
	@Override
	public void setBars() {
		super.setBars();
		tensionConfig.addBars(this);
	}

	public class TensionMenderBuild extends MendBuild implements HasTension {
		public TensionModule tension = new TensionModule();

		@Override
		public void onProximityUpdate() {
			super.onProximityAdded();
			tensionGraph().removeBuild(this, true);
		}
		@Override
		public void onProximityRemoved() {
			super.onProximityRemoved();
			tensionGraph().removeBuild(this, false);
		}

		@Override public TensionModule tension() {
			return tension;
		}
		@Override public TensionConfig tensionConfig() {
			return tensionConfig;
		}

		@Override
		public void updateTile() {
			if (tensionGraph().getOverallTension() > tensionConfig.maxTension) kill();
			super.updateTile();
		}
	}
}
