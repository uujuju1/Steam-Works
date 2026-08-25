package sw.world.blocks.defense;

import arc.util.*;
import arc.util.io.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;
import sw.world.graph.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class SWTurret extends Turret {
	public SpinConfig spinConfig;

	public boolean consumerScaleEfficiency = true;

	public boolean bulletsChangeTargeting = false;

	public boolean boosterDisplayStat = true;

	public SWTurret(String name) {
		super(name);
	}

	@Override
	public void drawPlanConfigTop(BuildPlan plan, Eachable<BuildPlan> list) {
		if (spinConfig != null) spinConfig.drawPlace(this, plan.x, plan.y, plan.rotation, true);
	}

	@Override
	public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
		if (spinConfig != null) spinConfig.drawPlace(this, plan.x, plan.y, plan.rotation, true);
		super.drawPlanRegion(plan, list);
	}

	@Override
	public void init() {
		super.init();
		if (spinConfig != null) spinConfig.init(this);
	}

	@Override
	public void setBars() {
		super.setBars();
		if (spinConfig != null) spinConfig.addBars(this);
	}

	@Override
	public void setStats() {
		super.setStats();
		if (spinConfig != null) spinConfig.addStats(stats);

		if (bulletsChangeTargeting) {
			if (targetAir) stats.remove(Stat.targetsAir);
			if (targetGround) stats.remove(Stat.targetsGround);
		}

		if(coolant != null && boosterDisplayStat){
			stats.remove(Stat.booster);
			coolant.display(stats);
		}
	}

	public class SWTurretBuild extends TurretBuild implements HasSpin {
		public SpinModule spin;
		
		@Override
		public Building create(Block block, Team team) {
			if (spinConfig != null) spin = new SpinModule();
			return super.create(block, team);
		}

		@Override public void drawSelect() {
			super.drawSelect();
			if (spin != null) spinConfig.drawPlace(block, tileX(), tileY(), ((Building) this).rotation, true);
		}

		@Override
		public float efficiencyScale() {
			float mul = 1f;
			if (consumerScaleEfficiency) for(Consume cons : consumers) {
				mul *= cons.efficiencyMultiplier(this);
			}
			return mul;
		}

		@Override
		protected Posc findEnemy(float range){
//			if(targetAir && !targetGround){
//				return Units.bestEnemy(team, x, y, range, e -> !e.dead() && !e.isGrounded() && unitFilter.get(e), unitSort);
//			}else{
//				var ammo = peekAmmo();
//				boolean buildings = targetGround && targetBlocks && (ammo == null || ammo.targetBlocks), missiles = ammo == null || ammo.targetMissiles;
//				return Units.bestTarget(team, x, y, range,
//					e -> !e.dead() && unitFilter.get(e) && (e.isGrounded() || targetAir) && (!e.isGrounded() || targetGround) && (missiles || !(e instanceof TimedKillc)),
//					b -> buildings && buildingFilter.get(b), unitSort);
//			}
			var ammo = peekAmmo();
			if (!bulletsChangeTargeting || ammo == null) return super.findEnemy(range);

			boolean canAir = targetAir && ammo.collidesAir;
			boolean canGround = targetGround && ammo.collidesGround;

			return Units.bestTarget(team, x, y, range,
				e -> !e.dead() && unitFilter.get(e) && (e.isGrounded() || canAir) && (!e.isGrounded() || canGround) && (ammo.targetMissiles || !(e instanceof TimedKillc)),
				b -> canGround && targetBlocks && ammo.targetBlocks && buildingFilter.get(b),
				unitSort
			);
		}

		@Override
		public void onProximityUpdate() {
			super.onProximityUpdate();
			
			if (spin != null) new SpinGraph().mergeFlood(this);
		}

		@Override
		public void onProximityRemoved() {
			super.onProximityRemoved();
			
			if (spin != null) spinGraph().removeBuild(this);
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);

			if (spinConfig != null) (spin == null ? new SpinModule() : spin).read(read);
		}

		@Override
		public void updateEfficiencyMultiplier(){
			if(heatRequirement > 0){
				efficiency *= Math.min(Math.max(heatReq / heatRequirement, cheating() ? 1f : 0f), maxHeatEfficiency);
			}
			efficiency *= efficiencyScale();
		}

		@Override
		public void write(Writes write) {
			super.write(write);

			if (spinConfig != null) spin.write(write);
		}
	}
}
