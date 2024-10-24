package sw.world.blocks.power;

import arc.*;
import arc.audio.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import sw.content.*;
import sw.util.*;
import sw.world.interfaces.*;
import sw.world.meta.*;

public class GasValve extends GasPipe {
	public float pumpSpeed = 0.1f;
	public float pumpOpenTime = 60f;

	public Effect pumpEffect = SWFx.valveElevation;
	public float pumpEffectChance = 0.8f;

	public Effect swapEffect = Fx.hitBulletColor;

	public Sound swapSound = Sounds.door;
	public float swapSoundVolume = 1f;

	public TextureRegion topRegion;

	public GasValve(String name) {
		super(name);
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
					if (other.block instanceof GasPipe) current.tiling |= 1 << finalI /2;
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

	public class GasValveBuild extends GasPipeBuild {
		public boolean open;
		public float openTimer = 0;

		@Override public boolean connectTo(HasSpin other) {
			return super.connectTo(other) && (other == front() || other == back());
		}

		@Override
		public void draw() {
			Draw.rect(regions[tiling], x, y, (rotdeg() + 90) % 180 - 90f);
			Draw.rect(topRegion, x, y, (rotdeg() + (open ? 90f : 180f)) % 180 - 90f);
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

		@Override
		public void updateGas() {
			if (getGas() > gasConfig.gasCapacity) {
				if (!open && openTimer <= 0) {
					openTimer = pumpOpenTime;
					swapEffect.at(x, y, (rotdeg() + (open ? 90f : 180f)) % 180 - 90f);
					open = true;
					swapSound.at(x, y, 1, swapSoundVolume);
				}
			} else {
				if (open && openTimer <= 0) {
					openTimer = pumpOpenTime;
					swapEffect.at(x, y, (rotdeg() + (open ? 90f : 180f)) % 180 - 90f);
					open = false;
					swapSound.at(x, y, 1, swapSoundVolume);
				}
			}
			if (open) {
				spin().subAmount(Math.min(getGas(), pumpSpeed * Time.delta * (getGasPressure() / gasConfig.gasCapacity)));
				if (Mathf.chance(pumpEffectChance)) pumpEffect.at(x, y, (rotdeg() + (open ? 90f : 180f)) % 180 - 90f);
			}

			if (openTimer > 0) openTimer -= Time.delta;
			super.updateGas();
		}
	}
}
