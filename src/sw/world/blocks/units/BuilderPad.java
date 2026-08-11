package sw.world.blocks.units;

import arc.math.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.type.*;
import sw.world.*;

public class BuilderPad extends GenericSpinBlock {
	public UnitType unitType;

	public float unitBuildTime = 60f;
	public float warmupSpeed = 0.014f;

	public BuilderPad(String name) {
		super(name);
	}

	public class BuilderPadBuild extends GenericSpinBuild {
		public Unit unit;
		public int unitID = -1;

		public float progress, totalProgress, warmup;

		@Override public float progress() {
			return progress;
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);

			unitID = read.i();

			progress = read.f();
			totalProgress = read.f();
			warmup = read.f();
		}

		public void readUnit() {
			if (unitID != -1) {
				unit = Groups.unit.getByID(unitID);
				unitID = -1;
			}
		}

		@Override
		public boolean shouldConsume() {
			return enabled && team.data().countType(unitType) <= Units.getCap(team);
		}

		@Override public float totalProgress() {
			return totalProgress;
		}

		@Override
		public void updateTile() {
			readUnit();

			if (efficiency > 0) {
				progress += getProgressIncrease(unitBuildTime);
				warmup = Mathf.approachDelta(warmup, 1, warmupSpeed);

				if (progress >= 1f) {
					if (!Vars.net.client()) {
						unit = unitType.create(team);
					}
				}
			} else {
				warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
			}

			totalProgress += warmup * Time.delta * efficiencyScale();
		}

		@Override public float warmup() {
			return warmup;
		}

		@Override
		public void write(Writes write) {
			super.write(write);

			write.i(unit.id);

			write.f(progress);
			write.f(totalProgress);
			write.f(warmup);
		}
	}
}
