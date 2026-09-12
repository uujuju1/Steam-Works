package sw.world.blocks.defense;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.meta.*;
import sw.annotations.*;
import sw.world.meta.*;

public class ChainTurret extends ConsumeTurret {
	public float holdTime;
	public float pull;
	public float pullDamageScale;
	public float pullStrengthScale;

	public float extension;

	public boolean targetAsEffectData = true;
	public Effect endChainEffect = Fx.none;

	public Interp chainInterp = Interp.linear;
	public float chainLayer = Layer.turret - 0.0005f;

	public @Annotations.Load(value = "@name$-chain", fallBack = "sw-chain") TextureRegion chainRegion;
	public @Annotations.Load(value = "@name$-chain-tip", fallBack = "sw-chain-tip") TextureRegion chainTipRegion;

	public ChainTurret(String name) {
		super(name);

		shootEffect = Fx.none;
		predictTarget = false;
		// do NOT make it target blocks
		targetBlocks = false;

		shootCone = 1f;
	}

	@Override
	public void init() {
		super.init();

		updateClipRadius(range);
	}

	@Override
	public void setStats() {
		super.setStats();

		stats.remove(Stat.ammo);
		stats.add(Stat.damageMultiplier, pullDamageScale * 8f * 60f, SWStat.perBlock);
	}

	public class ChainTurretBuild extends ConsumeTurretBuild {
		public Unit hit;
		public int hitID = -1;

		public float hitDst = -1;

		public float hold;

		@Override public boolean canControl() {
			return false;
		}

		@Override
		public void draw() {
			super.draw();

			if (hit == null) return;

			Draw.z(chainLayer);
			Tmp.v1.trns(angleTo(hit), (dst(hit)) * chainInterp.apply(hold / holdTime) + extension);

			float segmentWidth = chainRegion.width / 4f;
			Lines.stroke(chainRegion.height / 4f);

			float max = Tmp.v1.len() / segmentWidth;
			Tmp.v1.add(this);
			for (float i = max; i >= 0; i--) {
				Tmp.v2.set(this).lerp(Tmp.v1, Mathf.clamp(i / max));
				Tmp.v3.set(this).lerp(Tmp.v1, Mathf.clamp((i - 1) / max));

				Lines.line(chainRegion, Tmp.v2.x, Tmp.v2.y, Tmp.v3.x, Tmp.v3.y, false);
			}

			Draw.rect(chainTipRegion, Tmp.v1.x, Tmp.v1.y, angleTo(hit));
		}

		@Override
		protected void findTarget() {
			if (hit == null) {
				super.findTarget();
			} else target = hit;
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);

			hitID = read.i();

			hold = read.f();
		}

		@Override protected void shoot(BulletType type) {
			if (target instanceof Unit unit) {
				hit = unit;
				hold = 0;

				shootEffect.at(x, y, angleTo(hit), targetAsEffectData ? hit : new Vec2().set(hit));
				shootSound.at(x, y, Mathf.random(soundPitchMin, soundPitchMax), shootSoundVolume);
			}
		}

		protected void updateChain() {
			target = hit;
			rotation = angleTo(hit);

			hold += Time.delta;

			hitDst -= pull * Time.delta;
			if (hitDst < 0 || hitDst > dst(hit)) hitDst = dst(hit);

			Tmp.v1.set(hit).sub(this).setLength(hitDst).add(this);

			float distanceFrom = hit.dst(Tmp.v1);
			hit.vel().add(Tmp.v2.trns(hit.angleTo(this), distanceFrom * pullStrengthScale));
			hit.damage(distanceFrom * pullDamageScale * Time.delta);

			if (hold > holdTime || hit.dead) {
				endChainEffect.at(x, y, angleTo(hit), new Vec2().set(hit));
				hit = null;
				hitDst = -1f;
			}
		}

		@Override
		public void updateTile() {
			if (hitID != -1) {
				Unit unit = Groups.unit.getByID(hitID);
				if (unit != null && unit.isValid() && !unit.dead) hit = unit;

				hitID = -1;
			}

			if (hit != null) updateChain();

			super.updateTile();
		}

		@Override
		protected boolean validateTarget() {
			return (target != null && target == hit) || super.validateTarget();
		}

		@Override
		public void write(Writes write) {
			super.write(write);

			write.i(hit != null ? hit.id() : -1);

			write.f(hold);
		}
	}
}
