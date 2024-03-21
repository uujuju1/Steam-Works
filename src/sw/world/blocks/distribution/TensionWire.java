package sw.world.blocks.distribution;

import arc.*;
import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import sw.util.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class TensionWire extends Block {
	public TensionConfig tensionConfig = new TensionConfig();

	public TextureRegion[] tiles;

	public TensionWire(String name) {
		super(name);
		rotate = destructible = solid = update = true;
	}

	@Override
	public boolean canReplace(Block other) {
		return other instanceof TensionJunction || other instanceof TensionBridge || other instanceof TensionRouter || super.canReplace(other);
	}

	@Override
	public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
		plan.rotation %= 2;
		Draw.rect(region, plan.drawx(), plan.drawy(), plan.rotation * 90);
	}

	@Override
	public void load() {
		super.load();
		tiles = SWDraw.getRegions(Core.atlas.find(name + "-tiles"), 4, 1, 32);
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

		public int tiling = 0;

		@Override
		public boolean connects(HasTension to) {
			boolean otherWire = !(to instanceof TensionWireBuild) || to.front() == this || to.back() == this;
			return (otherWire && (to == front() || to == back()) || !proximity.contains((Building) to)) && HasTension.super.connects(to);
		}

		@Override
		public void draw() {
			if (rotation == 1) Draw.yscl = -1f;
			Draw.rect(tiles[tiling], x, y, rotdeg());
			Draw.reset();
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
			rotation %= 2;
			tiling = 0;
			if (front() instanceof HasTension b && b.getTensionDestination(this).connects(this) && connects(b.getTensionDestination(this))) tiling |= 1;
			if (back() instanceof HasTension b && b.getTensionDestination(this).connects(this) && connects(b.getTensionDestination(this))) tiling |= 2;
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
