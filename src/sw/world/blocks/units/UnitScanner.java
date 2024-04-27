package sw.world.blocks.units;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import sw.gen.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class UnitScanner extends Block {
	public TensionConfig tensionConfig = new TensionConfig();

	public float range = 80;
	public Color areaColor = Color.valueOf("A1A1B8").a(0.3f);

	public UnitScanner(String name) {
		super(name);
		solid = update = destructible = sync = true;
	}

	@Override
	public void init() {
		super.init();
		clipSize = range * 2f;
	}

	@Override
	public void setStats() {
		super.setStats();
		stats.add(Stat.range, range/8f, StatUnit.blocks);
		tensionConfig.addStats(stats);
	}
	@Override
	public void setBars() {
		super.setBars();
		tensionConfig.addBars(this);
	}

	public class UnitScannerBuild extends Building implements HasTension {
		public TensionModule tension = new TensionModule();

		public float warmup;

		@Override
		public void draw() {
			super.draw();
			if (warmup <= 0.001f) return;
			Draw.z(Layer.blockOver);
			Lines.stroke(1f, areaColor);
			Draw.alpha(1f);
			Lines.circle(x, y, range * warmup);
			Draw.alpha(areaColor.a);
			Fill.circle(x, y, range * warmup);
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
			if (efficiency > 0) {
				Groups.unit.intersect(x - range, y - range, range * 2f, range * 2f, unit -> {
					if (unit instanceof Revealc u && unit.dst(x, y) <= range) u.reveal();
				});
				warmup = Mathf.approachDelta(warmup, 1f, 0.03f);
			} else {
				warmup = Mathf.approachDelta(warmup, 0f, 0.03f);
			}
		}

	}
}
