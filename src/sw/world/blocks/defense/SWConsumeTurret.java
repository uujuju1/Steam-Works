package sw.world.blocks.defense;

import arc.graphics.g2d.*;
import arc.util.io.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.blocks.defense.turrets.*;
import sw.util.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class SWConsumeTurret extends Turret {
	public ForceConfig forceConfig = new ForceConfig();
	public VibrationConfig vibrationConfig = new VibrationConfig();
	public boolean hasForce = false, hasVibration = false;

	public BulletType shootType = Bullets.placeholder;

	public SWConsumeTurret(String name) {
		super(name);
	}

	@Override public void drawOverlay(float x, float y, int rotation) {
		if (forceConfig.outputsForce) Drawf.dashCircle(x, y, forceConfig.range, Pal.accent);
		if (vibrationConfig.outputsVibration) Drawf.dashCircle(x, y, forceConfig.range, Pal.accent);
	}

	@Override
	public void init() {
		super.init();
		if (!hasForce) forceConfig.acceptsForce = forceConfig.outputsForce = false;
		if (!hasVibration) vibrationConfig.acceptsVibration = vibrationConfig.outputsVibration = false;
		configurable = forceConfig.outputsForce || vibrationConfig.outputsVibration;
	}

	public class SWConsumeTurretBuild extends TurretBuild implements HasForce, HasVibration {
		ForceModule force = new ForceModule();
		VibrationModule vibration = new VibrationModule();

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

		@Override public BulletType useAmmo(){
			return shootType;
		}
		@Override public boolean hasAmmo(){
			return canConsume();
		}
		@Override public BulletType peekAmmo(){
			return shootType;
		}

		@Override
		protected void shoot(BulletType type) {
			super.shoot(type);
			consume();
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
			if (getVibrationLink() != null) SWDraw.square(Pal.place, getVibrationLink().x(), getVibrationLink().y(), getVibrationLink().block().size * 6f, 0f);
			Draw.reset();
		}

		@Override
		public void onProximityAdded() {
			super.onProximityAdded();
			vGraph().add(this);
			force.graph.flood(this).each(b -> graph().add(b));
			vGraph().updateBuilds();
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
			vibration.read(read);
		}
		@Override
		public void write(Writes write) {
			super.write(write);
			force.write(write);
			vibration.write(write);
		}
	}
}
