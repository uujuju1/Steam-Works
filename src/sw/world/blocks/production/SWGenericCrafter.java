package sw.world.blocks.production;

import mindustry.world.blocks.production.*;
import sw.world.consumers.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class SWGenericCrafter extends GenericCrafter {
	public TensionConfig tensionConfig = new TensionConfig();

	public float outputTension = 0;

	public SWGenericCrafter(String name) {
		super(name);
	}

	public ConsumeTension consumeTension(float min, float max) {
		return consume(new ConsumeTension(min, max));
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

	public class SWGenericCrafterBuild extends GenericCrafterBuild implements HasTension {
		public TensionModule tension = new TensionModule();

		@Override public TensionModule tension() {
			return tension;
		}
		@Override public TensionConfig tensionConfig() {
			return tensionConfig;
		}

		@Override public float tensionMobile() {
			return efficiency * outputTension;
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
			super.updateTile();
			if (tensionGraph().getOverallTension() > tensionConfig.maxTension) kill();
		}
	}
}
