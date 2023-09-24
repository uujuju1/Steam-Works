package sw.world.blocks.production;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.blocks.production.*;
import mindustry.world.meta.*;
import sw.util.*;
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

	public float outputSpeed = -1f, outputHeatSpeed = 0f, outputHeat = -1f, outputVibration = -1f;
	public boolean hasForce = true, hasHeat = true, hasVibration = false;

/**
 * makes so that it can spin continuously for a certain amount before stopping
 */
	public boolean clampRotation = false;
	public float maxRotation = 30f;

	public SWGenericCrafter(String name) {
		super(name);
		configurable = true;
	}

	@Override
	public void setStats() {
		super.setStats();
		if (hasHeat) heatConfig.heatStats(stats);
		if (hasForce) forceConfig.addStats(stats);
		if (outputHeat >= 0) stats.add(SWStat.outputHeat, outputHeat, StatUnit.degrees);
	}
	@Override
	public void setBars() {
		super.setBars();
		if (!hasHeat) return;
		addBar("heat", (SWGenericCrafterBuild entity) -> new Bar(Core.bundle.get("bar.heat"), Pal.accent, entity::fraction));
	}

	@Override public void drawOverlay(float x, float y, int rotation) {
		if (forceConfig.outputsForce) Drawf.dashCircle(x, y, forceConfig.range, Pal.accent);
	}

	@Override
	public void init() {
		super.init();
		if (!hasHeat) heatConfig.acceptHeat = heatConfig.outputHeat = false;
		if (!hasForce) forceConfig.acceptsForce = forceConfig.outputsForce = false;
		configurable = forceConfig.outputsForce || hasVibration;
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
		@Override public ForceConfig forceConfig() {
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
			if (efficiency > 0 && outputHeat >= 0) setHeat(Mathf.approach(temperature(), outputHeat * efficiency, outputHeatSpeed * edelta()));
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
			if (outputVibration >= 0) vGraph().frequencies.add(outputVibration);
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
			if (getVibrationLink() != null) SWDraw.square(Pal.place, getVibrationLink().x(), getVibrationLink().y(), getVibrationLink().block().size * 6f, 0f);
			Draw.reset();
		}

		@Override
		public void onProximityAdded() {
			super.onProximityAdded();
			vGraph().add(this);
			force.graph.flood(this).each(b -> graph().add(b));
		}
		@Override
		public void onProximityRemoved() {
			super.onProximityRemoved();
			forceUnLink();
			graph().remove(this);
			if (getVibrationLink() != null) vibrationUnlink();
			vGraph().links.removeAll(vibration().links);
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
