package sw.world.blocks.power;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.entities.units.*;
import sw.util.*;
import sw.world.interfaces.*;
import sw.world.meta.*;

public class GasPump extends GasPipe {
	public float pumpSpeed = 0.1f;

	public TextureRegion topRegion;

	public GasPump(String name) {
		super(name);
		rotate = true;
	}

	@Override
	public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
		Draw.rect(getPlanRegion(plan, list), plan.drawx(), plan.drawy(), !rotate ? 0 : (plan.rotation * 90f + 90) % 180 - 90f);
		Draw.rect(topRegion, plan.drawx(), plan.drawy(), plan.rotation * 90f);
	}

	@Override
	public TextureRegion getPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
		var current = new Object() {
			public int tiling;
		};
		for (int i = 0; i < 4; i+=2) {
			int finalI = i;
			list.each(other -> {
				if (new Point2(plan.x, plan.y).add((Geometry.d4((((plan.rotation + 1) % 2 - 1 + finalI) + 4) % 4))).equals(other.x, other.y)) {
					if (other.block instanceof GasPipe && !(other.block instanceof GasPump)) current.tiling |= 1 << finalI /2;
				}
			});
		}
		return regions[current.tiling];
	}

	@Override
	public void load() {
		super.load();

		if (Core.atlas.find(name + "-tiles").found()) {
			regions = SWDraw.getRegions(Core.atlas.find(name + "-tiles"), 4, 1, 32, 32);
		} else {
			regions = new TextureRegion[4];
			for (int i = 0; i < regions.length; i++) {
				regions[i] = Core.atlas.find("error");
			}
		}

		topRegion = Core.atlas.find(name + "-top");
	}

	@Override
	public void setStats() {
		super.setStats();
		stats.add(SWStat.gasTranfer, Strings.fixed(pumpSpeed, 2), SWStat.gasSecond);
	}

	public class GasPumpBuild extends GasPipeBuild {
		@Override public boolean acceptsGas(HasSpin from, float amount) {
			return false;
		}

		@Override public boolean connectTo(HasSpin other) {
			return super.connectTo(other) && (other == front() || other == back()) && !(other instanceof GasPumpBuild);
		}

		@Override
		public void draw() {
			Draw.rect(regions[tiling], x, y, (rotdeg() + 90) % 180 - 90f);
			Draw.rect(topRegion, x, y, rotdeg());
		}

		@Override
		public void onGraphUpdate() {
			tiling = 0;
			for (int i = 0; i < 4; i+=2) {
				if (nearby((((rotation + 1) % 2 - 1 + i) + 4) % 4) instanceof HasSpin gas && HasSpin.connects(this, gas)) {
					tiling |= 1 << i/2;
				}
			}
		}

		@Override public boolean outputsGas(HasSpin to, float amount) {
			return false;
		}

		@Override
		public void updateGas() {
			spin().setAmount(Math.max(
				front() instanceof HasSpin gas && HasSpin.connects(this, gas) ? gas.getGas() : 0,
				back() instanceof HasSpin gas && HasSpin.connects(this, gas) ? gas.getGas() : 0
			));
			if (
				front() instanceof HasSpin front &&
				back() instanceof HasSpin back && back.getGas() > 0 &&
				HasSpin.connects(this, front) && HasSpin.connects(this, back)
			) {
				front.handleGas(back, Mathf.clamp(pumpSpeed * Time.delta, 0, back.getGas()), true);
			}
			super.updateGas();
		}
	}
}
