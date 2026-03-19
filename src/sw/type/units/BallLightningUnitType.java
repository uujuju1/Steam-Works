package sw.type.units;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.gen.*;
import sw.type.*;

/**
 * Unit type whose body ties to its payload and has parallax scrolling.
 * Do not put weapons on this thing.
 */
public class BallLightningUnitType extends SWUnitType {
	public float sizeScl = 1;
	public float sizeMag = 2;

	public Color tint = Color.white;

	public BallLightningUnitType(String name) {
		super(name);
		hittable = targetable = false;
		physics = false;
		isEnemy = false;
	}

	@Override
	public void draw(Unit unit) {
		boolean isPayload = !unit.isAdded();

		Segmentc seg = unit instanceof Segmentc c ? c : null;
		float z =
			isPayload ? Draw.z() :
			//dead flying units are assumed to be falling, and to prevent weird clipping issue with the dark "fog", they always draw above it
			unit.elevation > 0.5f || (flying && unit.dead) ? (flyingLayer) :
			seg != null ? groundLayer + seg.segmentIndex() / 4000f * Mathf.sign(segmentLayerOrder) + (!segmentLayerOrder ? 0.01f : 0f) :
			groundLayer + Mathf.clamp(hitSize / 4000f, 0, 0.01f);
		Draw.z(z);

		Draw.color(tint);
		Fill.circle(unit.x, unit.y, hitSize / 2f + Mathf.absin(unit.id + Time.time, sizeScl, sizeMag));
	}
}
