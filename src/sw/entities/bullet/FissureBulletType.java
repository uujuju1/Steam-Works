package sw.entities.bullet;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;

public class FissureBulletType extends BulletType {
	Rand rand = new Rand();
	
	public Color fromColor = Color.white, toColor = Color.white;
	public float strokeFrom = 1, strokeTo = 1;
	
	public int divisions = 10;
	
	public float bezierOffsetMag;
	public float divisionOffsetYMag;
	
	public float hitFract = 0f;
	
	public Interp startCurve = Interp.one, endCurve = Interp.zero;
	
	public FissureBulletType() {
		speed = 0;
		keepVelocity = false;
		collides = false;
		hittable = absorbable = false;
		pierce = true;
		removeAfterPierce = false;
		impact = true;
	}
	
	@Override
	public void draw(Bullet b) {
		rand.setSeed(b.id);
		Draw.color(fromColor, toColor, b.fin());
		
		float middleOffset = rand.random(-bezierOffsetMag, bezierOffsetMag);
		
		float start = b.fin(startCurve) * divisions;
		float end = b.fin(endCurve) * divisions;
		
		Tmp.v1.set(b.originX, b.originY);
		if (b.owner instanceof Posc owner) Tmp.v1.set(owner);
		Tmp.v3.set(b.aimX, b.aimY).sub(b.originX, b.originY).clampLength(0, range).add(b.originX, b.originY);
		Tmp.bz2.set(
			Tmp.v1,
			Tmp.v2.set(Tmp.v1).lerp(Tmp.v3, 0.5f).add(
				Angles.trnsx(Tmp.v1.angleTo(Tmp.v3), 0, middleOffset),
				Angles.trnsy(Tmp.v1.angleTo(Tmp.v3), 0, middleOffset)
			),
			Tmp.v3
		);
		Tmp.v4.set(Tmp.v1);
		Tmp.v6.set(Tmp.v1);
		
		for (int i = 0; i < divisions; i++) {
			float divisionOffset = rand.random(-divisionOffsetYMag, divisionOffsetYMag);
			
			Lines.stroke(Mathf.lerp(strokeFrom, strokeTo, i/9f));
			
			Tmp.bz2.valueAt(Tmp.v5, (i + 1)/10f);
			
			float angle = Tmp.v6.angleTo(Tmp.v5);
			
			if (i != divisions - 1) Tmp.v5.add(Angles.trnsx(angle, 0, divisionOffset), Angles.trnsy(angle, 0, divisionOffset));
			
			float s = Mathf.clamp(start - i);
			float e = Mathf.clamp(end - i);
			
			if (!(i > start || (i + 1) < end)) Lines.line(
				Mathf.lerp(Tmp.v4.x, Tmp.v5.x, e),
				Mathf.lerp(Tmp.v4.y, Tmp.v5.y, e),
				Mathf.lerp(Tmp.v4.x, Tmp.v5.x, s),
				Mathf.lerp(Tmp.v4.y, Tmp.v5.y, s)
			);
			
			Tmp.v4.set(Tmp.v5);
			Tmp.v6.set(Tmp.v5).sub(Angles.trnsx(angle, 0, divisionOffset), Angles.trnsy(angle, 0, divisionOffset));
		}
	}
	
	@Override
	public void init(Bullet b) {
		super.init(b);
		
		b.set(Tmp.v1.set(b.aimX, b.aimY).sub(b.originX, b.originY).clampLength(0, range).add(b.originX, b.originY));
		b.vel.setZero();
	}
	
	@Override
	public void update(Bullet b) {
		if (b.fin() > hitFract && !b.hit) {
			Damage.collidePoint(b, b.team, Fx.none, b.x, b.y);
			createSplashDamage(b, b.x, b.y);
			
			if (!b.hit) hit(b);
			
			b.hit = true;
		}
	}
}
