package sw.world.blocks.power;

import arc.struct.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.blocks.liquid.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class TensionJunction extends Block {
	public TensionConfig tensionConfig = new TensionConfig();

	public TensionJunction(String name) {
		super(name);
		destructible = solid = update = true;
	}

	@Override
	public boolean canReplace(Block other) {
		return other instanceof TensionBridge || other instanceof TensionRouter || other instanceof TensionWire || super.canReplace(other);
	}

	@Override public void init() {
		tensionConfig.graphs = false;
		tensionConfig.tier = -1;
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

		@Override
		public HasTension getTensionDestination(HasTension source) {
			if (!enabled) {
				return this;
			} else {
				int dir = (source.relativeTo(this.tile.x, this.tile.y) + 4) % 4;
				HasTension next = nearby(dir) instanceof HasTension ? nearby(dir).as() : null;
				return (next != null && (next.connects(source) || next.block() instanceof LiquidJunction) ? next.getTensionDestination(this) : this);
			}
		}

		@Override
		public Seq<HasTension> nextBuilds() {
			return Seq.with();
		}

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
