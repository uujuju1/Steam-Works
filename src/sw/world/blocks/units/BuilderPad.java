package sw.world.blocks.units;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.blocks.*;
import mindustry.world.consumers.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import sw.world.*;

public class BuilderPad extends GenericSpinBlock {
	public UnitType unitType;

	public float unitBuildTime = 60f;
	public float warmupSpeed = 0.014f;

	public boolean consumerScaleEfficiency = true;

	public DrawBlock drawer = new DrawDefault();

	public BuilderPad(String name) {
		super(name);

		update = true;
		destructible = true;
	}

	@Override
	public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
		if (spinConfig != null) spinConfig.drawPlace(this, plan.x, plan.y, plan.rotation, true);
		drawer.drawPlan(this, plan, list);
	}

	@Override
	public void drawPlanConfigTop(BuildPlan plan, Eachable<BuildPlan> list) {
		drawer.drawPlan(this, plan, list);
	}

	@Override
	public void getRegionsToOutline(Seq<TextureRegion> out) {
		drawer.getRegionsToOutline(this, out);
	}

	@Override
	public TextureRegion[] icons(){
		return drawer.finalIcons(this);
	}

	@Override
	public void load() {
		super.load();
		drawer.load(this);
	}

	@Override
	public void setStats() {
		super.setStats();

		stats.add(Stat.activationTime, unitBuildTime, StatUnit.seconds);

		stats.addPercent(Stat.buildSpeed, unitType.buildSpeed);
	}

	public class BuilderPadBuild extends GenericSpinBuild implements UnitTetherBlock {
		public Unit unit;
		public int unitID = -1;

		public float progress, totalProgress, warmup;

		@Override public void draw() {
			drawer.draw(this);
		}
		@Override public void drawLight() {
			drawer.drawLight(this);
		}
		@Override public void drawSelect() {
			super.drawSelect();
			if (spinConfig != null) spinConfig.drawPlace(block, tileX(), tileY(), rotation, true);
		}

		@Override
		public float efficiencyScale() {
			float mul = 1f;
			if (consumerScaleEfficiency) for(Consume cons : consumers) {
				mul *= cons.efficiencyMultiplier(this);
			}
			return mul;
		}

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
			return enabled && (team.data().countType(unitType) < Units.getCap(team) || (unit != null && unit.isValid()));
		}

		@Override
		public void spawned(int id) {
			Fx.spawn.at(x, y);
			progress = 0f;
			if (Vars.net.client()) {
				unitID = id;
			}
		}

		@Override public float totalProgress() {
			return totalProgress;
		}

		@Override
		public void updateTile() {
			readUnit();

			if (efficiency > 0) {
				warmup = Mathf.approachDelta(warmup, 1, warmupSpeed);
				if (unit == null || !unit.isValid()) {
					progress += getProgressIncrease(unitBuildTime);

					if (progress >= 1f) {
						if (!Vars.net.client()) {
							unit = unitType.create(team);
							if (unit instanceof BuildingTetherc bt) {
								bt.building(this);
							}
							unit.set(x, y);
							unit.rotation = 90f;
							unit.add();
							Call.unitTetherBlockSpawned(tile, unit.id);
						}
					}
				}
			} else {
				warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
			}

			if (unit != null && unit.isValid()) {
				unit.buildSpeedMultiplier(efficiency);
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
