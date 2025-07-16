package sw.entities.bullet;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import sw.annotations.Annotations.*;
import sw.gen.*;

public class SegmentBulletType extends BulletType {
	public @Load("@sprite$") TextureRegion segmentRegion;
	public @Load("@sprite$-end") TextureRegion endRegion;
	
	public String sprite = "";
	public float spriteLength = -1f;
	public float spriteExtension = 0f;
	public float length = 80;
	public float minLength = 20f;
	
	public float attackTime = 1f;
	
	public Interp lengthInterp = Interp.one;
	
	public SegmentBulletType() {
		speed = 0;
		keepVelocity = false;
		collides = false;
		hittable = absorbable = false;
		pierce = true;
		removeAfterPierce = false;
		shootEffect = smokeEffect = Fx.none;
	}
	
	@Override
	public void draw(Bullet b) {
		inverseKinematics(b, Tmp.v2).add(b);
		Tmp.v1.set(b.aimX, b.aimY).sub(b).clampLength(0f, length).add(b);
		
		float totalLength = b.dst(Tmp.v2) + Tmp.v2.dst(Tmp.v1);
		
		float curLength = lengthInterp.apply(b.fin()) * totalLength;
		
		Tmp.bz2.set(Tmp.v3.set(b), Tmp.v2, Tmp.v1);
		
		int divisions = Mathf.floor(curLength/spriteLength);
		
		for (int i = 0; i < divisions; i++) {
			float diff = totalLength - curLength;
			Vec2 pos = Tmp.bz2.valueAt(Tmp.v4, 1f - (diff + i * spriteLength) / totalLength);
			Vec2 next = i + 1 == divisions ? Tmp.v5.set(b) : Tmp.bz2.valueAt(Tmp.v5, 1f - (diff + (i + 1) * spriteLength) / totalLength);
			
			float len = Tmp.v6.set(next).sub(pos).len();
			Tmp.v6.setLength(len + spriteExtension).add(pos);
			
			Lines.stroke((i == 0 ? endRegion : segmentRegion).height/4f);
			Lines.line(i == 0 ? endRegion : segmentRegion, pos.x, pos.y, Tmp.v6.x, Tmp.v6.y, false);
		}
	}
	
	@Override
	public void init() {
		super.init();
		
		drawSize = Math.max(drawSize, length * 2f);
	}
	
	@Override
	public void init(Bullet b) {
		super.init(b);
		
		b.vel.setZero();
	}
	
	public Vec2 inverseKinematics(Bullet b, Vec2 res) {
		InverseKinematics.solve(
			length/2f, length/2f,
			Tmp.v1.set(b.aimX, b.aimY).sub(b).clampLength(minLength, length),
			Mathf.booleans[Mathf.randomSeed(b.id, 0, 1)],
			res
		);
		return res;
	}
	
	@Override
	public void update(Bullet b) {
		if (b.owner instanceof Posc entity) b.set(entity);
		
		if (b.fin() > attackTime && !b.hit) updateAttack(b);
	}
	
	public void updateAttack(Bullet b) {
		b.hit = true;
		
		inverseKinematics(b, Tmp.v2).add(b);
		Tmp.v1.set(b.aimX, b.aimY).sub(b).clampLength(0f, length).add(b);
		
		float totalLength = b.dst(Tmp.v2) + Tmp.v2.dst(Tmp.v1);
		
		float curLength = lengthInterp.apply(b.fin()) * totalLength;
		
		Tmp.bz2.set(Tmp.v3.set(b), Tmp.v2, Tmp.v1);
		
		int divisions = Mathf.floor(curLength/spriteLength);
		
		for (int i = 0; i < divisions; i++) {
			float diff = totalLength - curLength;
			Vec2 pos = Tmp.bz2.valueAt(Tmp.v4, 1f - (diff + i * spriteLength) / totalLength);
			Vec2 next = i + 1 == divisions ? Tmp.v5.set(b) : Tmp.bz2.valueAt(Tmp.v5, 1f - (diff + (i + 1) * spriteLength) / totalLength);
			
			Tmp.r1.setCentered(
				pos.x,
				pos.y,
				pos.x - next.x,
				pos.y - next.y
			).normalize();
			
			for (Teams.TeamData data : Vars.state.teams.active) {
				if (data.team == b.team) continue;
				
				if (data.buildingTree != null && collidesGround) {
					data.buildingTree.intersect(Tmp.r1, build -> {
						Tmp.r1.getCenter(Tmp.v6);
						hitEffect.at(Tmp.v6.x, Tmp.v6.y, Tmp.v6.angleTo(build), hitColor);
						hitSound.at(Tmp.v6.x, Tmp.v6.y, hitSoundPitch, hitSoundVolume);
						build.damagePierce(damage * damageMultiplier(b) * buildingDamage(b));
					});
				}
				
				if (data.unitTree != null) {
					data.unitTree.intersect(Tmp.r1, unit -> {
						if (
							(unit.isFlying() && collidesAir) ||
							(!unit.isFlying() && collidesGround)
						) {
							Tmp.r1.getCenter(Tmp.v6);
							float angle = Tmp.v6.angleTo(unit);
							hitEffect.at(Tmp.v6.x, Tmp.v6.y, angle, hitColor);
							hitSound.at(Tmp.v6.x, Tmp.v6.y, hitSoundPitch, hitSoundVolume);
							unit.damagePierce(damage * damageMultiplier(b));
							unit.impulse(
								Angles.trnsx(angle, knockback),
								Angles.trnsy(angle, knockback)
							);
						}
					});
				}
			}
		}
	}
	
	@Override
	public void load() {
		super.load();
		
		SWContentRegionRegistry.load(this);
		if (spriteLength == -1) spriteLength = segmentRegion.width/4f;
	}
}
