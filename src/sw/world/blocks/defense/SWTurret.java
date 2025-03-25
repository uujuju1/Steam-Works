package sw.world.blocks.defense;

import arc.util.*;
import arc.util.io.*;
import mindustry.entities.units.*;
import mindustry.world.blocks.defense.turrets.*;
import sw.world.graph.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class SWTurret extends Turret {
	public SpinConfig spinConfig = new SpinConfig();

	public SWTurret(String name) {
		super(name);
	}

	@Override
	public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
		spinConfig.drawPlace(this, plan.x, plan.y, plan.rotation, true);
		drawDefaultPlanRegion(plan, list);
	}

	@Override
	public void drawPlanConfigTop(BuildPlan plan, Eachable<BuildPlan> list) {
		drawDefaultPlanRegion(plan, list);
	}

	@Override
	public void setBars() {
		super.setBars();
		spinConfig.addBars(this);
	}

	@Override
	public void setStats() {
		super.setStats();
		spinConfig.addStats(stats);
	}

	public class SWTurretBuild extends TurretBuild implements HasSpin {
		public SpinModule spin = new SpinModule();

		@Override public void drawSelect() {
			spinConfig.drawPlace(block, tileX(), tileY(), rotation(), true);
		}

		@Override
		public void onProximityUpdate() {
			super.onProximityUpdate();

			new SpinGraph().mergeFlood(this);
		}

		@Override
		public void onProximityRemoved() {
			super.onProximityRemoved();
			spinGraph().remove(this, true);
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			spin.read(read);
		}

		@Override public SpinModule spin() {
			return spin;
		}
		@Override public SpinConfig spinConfig() {
			return spinConfig;
		}

		@Override
		public void write(Writes write) {
			super.write(write);
			spin.write(write);
		}
	}
}
