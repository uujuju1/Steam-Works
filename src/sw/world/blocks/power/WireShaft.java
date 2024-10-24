package sw.world.blocks.power;

import arc.*;
import arc.graphics.g2d.*;
import mindustry.gen.*;
import mindustry.world.*;
import sw.util.*;
import sw.world.graph.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class WireShaft extends Block {
	public SpinConfig spinConfig = new SpinConfig();

	/**
	 * Thickness of bar in pixels.
	 */
	public int barThickness = 7;

	public TextureRegion bottomRegion, barShadowRegion;
	public TextureRegion[] tileRegions, barRegions;

	public WireShaft(String name) {
		super(name);
		update = true;
		rotate = true;
	}

	@Override
	public boolean canReplace(Block other) {
		return super.canReplace(other) || other.getClass() == getClass();
	}

	@Override
	public void load() {
		super.load();
		bottomRegion = Core.atlas.find(name + "-bottom", "sw-shaft-bottom");
		barShadowRegion = Core.atlas.find(name + "-bar-shadow", "sw-shaft-shadow");

		tileRegions = Core.atlas.find(name + "-tiles").split(32, 32)[0];
		barRegions = Core.atlas.find(name + "-bar", "sw-shaft").split(32, barThickness)[0];
	}

	public class WireShaftBuild extends Building implements HasSpin {
		public int tiling;

		public SpinModule spin = new SpinModule();

		@Override public boolean connectTo(HasSpin other) {
			return HasSpin.super.connectTo(other) && (!rotate || other == front() || other == back());
		}

		@Override
		public void draw() {
			float rot = rotate ? ((rotdeg() + 90f) % 180f - 90f) : 0;
			Draw.rect(bottomRegion, x, y);
			SWDraw.rotatingRects(barRegions, x, y, 8f, barThickness * barRegions.length / 8f, rot, spinGraph().rotation);
			if (!rotate) SWDraw.rotatingRects(barRegions, x, y, 8f, barThickness * barRegions.length / 8f, rot - 90f, spinGraph().rotation);
			Draw.rect(barShadowRegion, x, y, rot);
			if (rotation > 0 && rotation < 3) Draw.xscl = -1;
			Draw.rect(tileRegions[tiling], x, y, rot);
			Draw.xscl = 1;
		}

		@Override
		public void onGraphUpdate() {
			tiling = 0;
			if (!rotate) return;
			if (front() instanceof HasSpin gas && HasSpin.connects(this, gas.getGasDestination(this))) tiling |= 1;
			if (back() instanceof HasSpin gas && HasSpin.connects(this, gas.getGasDestination(this))) tiling |= 2;
		}

		@Override
		public void onProximityUpdate() {
			super.onProximityUpdate();

			new SpinGraph().mergeFlood(this);
			onGraphUpdate();
		}

		@Override
		public void onProximityRemoved() {
			super.onProximityRemoved();
			spinGraph().remove(this, true);
		}

		@Override public SpinModule spin() {
			return spin;
		}
		@Override public SpinConfig spinConfig() {
			return spinConfig;
		}
	}
}
