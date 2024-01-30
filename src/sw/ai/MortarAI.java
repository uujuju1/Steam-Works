package sw.ai;

import arc.math.*;
import mindustry.ai.*;
import mindustry.ai.types.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.world.*;

import static mindustry.Vars.*;

public class MortarAI extends GroundAI {
	@Override
	public void updateMovement(){

		Building core = unit.closestEnemyCore();

		if(core != null && unit.within(core, unit.range() / 1.3f + core.block.size * tilesize / 2f)){
			target = core;
			for(var mount : unit.mounts){
				if(mount.weapon.controllable && mount.weapon.bullet.collidesGround){
					mount.target = core;
				}
			}
		}

		if((core == null || !unit.within(core, unit.type.range * 0.9f))){
			boolean move = true;

			if(state.rules.waves && unit.team == state.rules.defaultTeam){
				Tile spawner = getClosestSpawner();
				if(spawner != null && unit.within(spawner, state.rules.dropZoneRadius + unit.range())) move = false;
				if(spawner == null && core == null) move = false;
			}

			//no reason to move if there's nothing there
			if(core == null && (!state.rules.waves || getClosestSpawner() == null)){
				move = false;
			}
			if (unit.isShooting() && target != null && unit.within(target, unit.range() * 0.9f)) move = false;

			if(move) pathfind(Pathfinder.fieldCore);
		}

		if(unit.type.canBoost && unit.elevation > 0.001f && !unit.onSolid()){
			unit.elevation = Mathf.approachDelta(unit.elevation, 0f, unit.type.riseSpeed);
		}

		faceTarget();
	}

	@Override
	public Teamc target(float x, float y, float range, boolean air, boolean ground){
		return Units.closestTarget(unit.team, x, y, range, u -> false, t -> ground);
	}
}
