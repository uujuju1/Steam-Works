package sw.world.blocks.power;

import mindustry.gen.*;
import mindustry.world.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class TensionRouter extends Block {
	public TensionConfig tensionConfig = new TensionConfig();

	public TensionRouter(String name) {
		super(name);
		destructible = solid = update = true;
	}

	@Override
	public boolean canReplace(Block other) {
		return other instanceof TensionJunction || other instanceof TensionBridge || other instanceof TensionWire || super.canReplace(other);
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

	public class TensionWireBuild extends Building implements HasTension {
		public TensionModule tension = new TensionModule();

		@Override public TensionModule tension() {
			return tension;
		}
		@Override public TensionConfig tensionConfig() {
			return tensionConfig;
		}

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

		@Override
		public void updateTile() {
			if (tensionGraph().getOverallTension() > tensionConfig.maxTension) kill();
		}
	}
}
