package sw.type.units;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import sw.gen.*;
import sw.type.*;

public class GrassHopperUnitType extends SWUnitType {
	public Seq<GrassHopperWing> wings = new Seq<>();

	private static final Vec2 lOffset = new Vec2();

	public GrassHopperUnitType(String name) {
		super(name);
		canBoost = true;

		fallSpeed = 0.007f;
		riseSpeed = 0.03f;

		constructor = UnitGrassHopper::create;
	}

	@Override
	public void draw(Unit unit) {
		if(unit.inFogTo(Vars.player.team())) return;

		boolean isPayload = !unit.isAdded();

		float z = isPayload ? Draw.z() : unit.elevation > 0.5f ? (lowAltitude ? Layer.flyingUnitLow : Layer.flyingUnit) : groundLayer + Mathf.clamp(hitSize / 4000f, 0, 0.01f);

		if (unit instanceof GrassHopperc) {
			Draw.z(z - 0.002f);
			drawGrassHopperLegs((Unit & GrassHopperc) unit);
			Draw.z(z);
		}
		super.draw(unit);
		if (unit instanceof GrassHopperc) drawGrassHopperWings((Unit & GrassHopperc) unit);
	}

	public <T extends Unit & GrassHopperc> void drawGrassHopperLegs(T unit) {
		applyColor(unit);
		Tmp.c3.set(Draw.getMixColor());

		Leg[] legs = unit.legs();

		float ssize = footRegion.width * footRegion.scl() * 1.5f;
		float rotation = unit.rotation;
		float invDrown = 1f - unit.drownTime;

		if(footRegion.found()){
			for(Leg leg : legs){
				Drawf.shadow(leg.base.x, leg.base.y, ssize, invDrown);
			}
		}

		//legs are drawn front first
		for(int j = legs.length - 1; j >= 0; j--){
			int i = (j % 2 == 0 ? j/2 : legs.length - 1 - j/2);
			Leg leg = legs[i];
			boolean flip = i >= legs.length/2f;
			int flips = Mathf.sign(flip);

			Vec2 position = unit.legOffset(lOffset, i).add(unit);

			Tmp.v1.set(leg.base).sub(leg.joint).inv().setLength(legExtension);

			if(footRegion.found() && leg.moving && shadowElevation > 0){
				float scl = shadowElevation * invDrown;
				float elev = Mathf.slope(1f - leg.stage) * scl;
				Draw.color(Pal.shadow);
				Draw.rect(footRegion, leg.base.x + shadowTX * elev, leg.base.y + shadowTY * elev, position.angleTo(leg.base));
				Draw.color();
			}

			Draw.mixcol(Tmp.c3, Tmp.c3.a);

			if(footRegion.found()){
				Draw.rect(footRegion, leg.base.x, leg.base.y, position.angleTo(leg.base));
			}

			Lines.stroke(legRegion.height * legRegion.scl() * flips);
			Lines.line(legRegion, position.x, position.y, leg.joint.x, leg.joint.y, false);

			Lines.stroke(legBaseRegion.height * legRegion.scl() * flips);
			Lines.line(legBaseRegion, leg.joint.x + Tmp.v1.x, leg.joint.y + Tmp.v1.y, leg.base.x, leg.base.y, false);

			if(jointRegion.found()){
				Draw.rect(jointRegion, leg.joint.x, leg.joint.y);
			}
		}

		//base joints are drawn after everything else
		if(baseJointRegion.found()){
			for(int j = legs.length - 1; j >= 0; j--){
				//TODO does the index / draw order really matter?
				Vec2 position = unit.legOffset(lOffset, (j % 2 == 0 ? j/2 : legs.length - 1 - j/2)).add(unit);
				Draw.rect(baseJointRegion, position.x, position.y, rotation);
			}
		}

		if(baseRegion.found()){
			Draw.rect(baseRegion, unit.x, unit.y, rotation - 90);
		}

		Draw.reset();
	}
	public <T extends Unit & GrassHopperc> void drawGrassHopperWings(T unit) {
		applyColor(unit);
		wings.each(wing -> wing.draw(unit));
	}

	@Override
	public void load() {
		super.load();
		wings.each(wing -> wing.load(this));
	}

	public static class GrassHopperWing {
		public boolean mirror = true;
		public float
			x, y, rotation, moveRotation,
			shineScl, shineMag,
			scl, mag, blurOffset;
		public int blurs = 1;
		public String suffix;

		public TextureRegion region, blurRegion, shineRegion, topRegion;

		public GrassHopperWing(String suffix, float x, float y) {
			this.suffix = suffix;
			this.x = x;
			this.y = y;
		}

		public <T extends Unit & GrassHopperc> void draw(T unit) {
			for (int i : Mathf.signs) {
				if (!mirror && i == -1) continue;
				float dx = unit.x + Angles.trnsx(unit.rotation - 90f, x * i, y);
				float dy = unit.y + Angles.trnsy(unit.rotation - 90f, x * i, y);
				float dr = rotation + unit.rotation - 90 + moveRotation * unit.elevation * i;

				Draw.xscl *= i;
				if (blurRegion.found()) Draw.alpha(1f - unit.elevation);
				Draw.rect(region, dx, dy, dr);
				if (blurRegion.found()) {
					Draw.alpha(unit.elevation);
					for (int j = 0; j < blurs; j++) {
						float rot = Mathf.sin(Time.time + j * blurOffset, scl, mag) * i;
						Draw.rect(blurRegion, dx, dy, dr + rot);
					}
					if (shineRegion.found()) {
						float rot = Mathf.sin(shineScl, shineMag) * i;
						Draw.rect(shineRegion, dx, dy, dr + rot);
					}
				}
				if (topRegion.found()) Draw.rect(topRegion, dx, dy, dr);
				Draw.xscl *= i;
			}
		}

		public void load(GrassHopperUnitType type) {
			this.region = Core.atlas.find(type.name + suffix);
			this.blurRegion = Core.atlas.find(type.name + suffix + "-blur");
			this.shineRegion = Core.atlas.find(type.name + suffix + "-shine");
			this.topRegion = Core.atlas.find(type.name + suffix + "-top");
		}
	}
}
