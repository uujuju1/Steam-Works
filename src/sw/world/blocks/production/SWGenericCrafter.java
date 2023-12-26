package sw.world.blocks.production;

import arc.*;
import arc.graphics.g2d.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.blocks.production.*;
import sw.util.*;
import sw.world.graph.VibrationGraph.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

/**
 * Works as a bridge between force and heat
 */
public class SWGenericCrafter extends GenericCrafter {
	public ForceConfig forceConfig = new ForceConfig();
	public HeatConfig heatConfig = new HeatConfig();
	public VibrationConfig vibrationConfig = new VibrationConfig();

	public float outputSpeed = -1f;
	public Frequency outputVibration;
	public boolean hasForce = true, hasHeat = false, hasVibration = false;

/**
 * TODO remove
 */
	public boolean clampRotation = false;
	public float maxRotation = 30f;

	public SWGenericCrafter(String name) {
		super(name);
	}

	@Override
	public void setStats() {
		super.setStats();
		if (hasForce) forceConfig.addStats(stats);
		if (hasVibration) {
			vibrationConfig.addStats(stats);
			if (outputVibration != null) stats.add(SWStat.outputVibration, stat -> stat.add(outputVibration.display()));
		}
	}
	@Override
	public void setBars() {
		super.setBars();
		if (!hasHeat) return;
		addBar("heat", (SWGenericCrafterBuild entity) -> new Bar(Core.bundle.get("bar.heat"), Pal.accent, entity::fraction));
	}

	@Override public void drawOverlay(float x, float y, int rotation) {
		if (forceConfig.outputsForce) Drawf.dashCircle(x, y, forceConfig.range, Pal.accent);
		if (vibrationConfig.outputsVibration) Drawf.dashCircle(x, y, forceConfig.range, Pal.accent);
	}

	@Override
	public void init() {
		super.init();
		if (!hasForce) {
			forceConfig.acceptsForce = false;
			forceConfig.outputsForce = false;
		}
		if (!hasVibration) {
			vibrationConfig.acceptsVibration = false;
			vibrationConfig.outputsVibration = false;
		}
		configurable = forceConfig.outputsForce || vibrationConfig.outputsVibration;
	}

	public class SWGenericCrafterBuild extends GenericCrafterBuild implements HasHeat, HasForce, HasVibration{
		public float rotation = 0;
		HeatModule heat = new HeatModule();
		ForceModule force = new ForceModule();
		VibrationModule vibration = new VibrationModule();

		@Override public HeatModule heat() {
			return heat;
		}
		@Override public HeatConfig heatC() {
			return heatConfig;
		}

		@Override public ForceModule force() {
			return force;
		}
		@Override public ForceConfig fConfig() {
			return forceConfig;
		}

		@Override public VibrationModule vibration() {
			return vibration;
		}
		@Override public VibrationConfig vConfig() {
			return vibrationConfig;
		}

		@Override
		public void updateTile() {
			if (outputVibration instanceof StaticFrequency frequency) {
				StaticFrequency f = new StaticFrequency(efficiency > 0, frequency.min, frequency.max);
				vGraph().addFrequency(f);
			}
			rotation += speed() * Time.delta;
			if (Math.abs(rotation) > maxRotation && clampRotation) {
				rotation = (maxRotation + 1f) * (rotation > 0 ? 1f : -1f);
				efficiency = 0f;
			}
			super.updateTile();
		}

		@Override
		public void craft() {
			super.craft();
			if (outputSpeed >= 0) force().speed = outputSpeed;
			if (outputVibration != null && !(outputVibration instanceof StaticFrequency)) vGraph().addFrequency(outputVibration);
		}

		@Override
		public void draw() {
			super.draw();
			drawBelt();
			drawLink();
		}
		@Override
		public void drawConfigure() {
			drawOverlay(x, y, 0);
			SWDraw.square(Pal.accent, x, y, block.size * 6f, 0f);
			if (getForceLink() != null) SWDraw.square(Pal.place, getForceLink().x(), getForceLink().y(), getForceLink().block().size * 6f, 0f);
//			getVibrationLinks().each(build -> {
//				SWDraw.square(Pal.place, build.x(), build.y(), build.block().size * 6f, 0f);
//			});
			Draw.reset();
		}

		@Override
		public void onProximityAdded() {
			super.onProximityAdded();
			vGraph().addBuild(this);
			fGraph().addBuild(this);
		}
		@Override
		public void onProximityRemoved() {
			super.onProximityRemoved();
			vibration().links.each(link -> vGraph().removeLink(link));
			vGraph().removeBuild(this, false);
			force().links.each(link -> fGraph().removeLink(link));
			fGraph().removeBuild(this, false);
		}
		@Override
		public void onProximityUpdate() {
			super.onProximityUpdate();
			if (getVibrationLink() != null) createVibrationLink(getVibrationLink());
		}

		@Override public boolean onConfigureBuildTapped(Building other) {
			return configureForceLink(other) && configVibrationLink(other);
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			force.read(read);
			heat.read(read);
			vibration.read(read);
			rotation = read.f();
		}
		@Override
		public void write(Writes write) {
			super.write(write);
			force.write(write);
			heat.write(write);
			vibration.write(write);
			write.f(rotation);
		}
	}
}
