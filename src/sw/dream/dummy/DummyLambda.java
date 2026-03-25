package sw.dream.dummy;

import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.input.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import sw.entities.units.*;

public class DummyLambda extends CollisionlessLegsUnit {
	public void add() {
		if (!this.added) {
			this.index__all = Groups.all.addIndex(this);
			this.index__unit = Groups.unit.addIndex(this);
			this.index__sync = Groups.sync.addIndex(this);
			this.index__draw = Groups.draw.addIndex(this);
			this.added = true;
			this.updateLastPosition();
			this.resetLegs();
			this.team.data().updateCount(this.type, 1);
		}
	}

	@Override
	public boolean serialize() {
		return false;
	}

	@Override
	public void update() {
		float bot;
		float left;
		if (!Vars.net.client() || this.isLocal()) {
			bot = this.x;
			left = this.y;
			this.move(this.vel.x * Time.delta, this.vel.y * Time.delta);
			if (Mathf.equal(bot, this.x)) {
				this.vel.x = 0.0F;
			}

			if (Mathf.equal(left, this.y)) {
				this.vel.y = 0.0F;
			}

			this.vel.scl(Math.max(1.0F - this.drag * Time.delta, 0.0F));
		}

		this.updateBuildLogic();
		this.hitTime -= Time.delta / 9.0F;
		this.stack.amount = Mathf.clamp(this.stack.amount, 0, this.itemCapacity());
		this.itemTime = Mathf.lerpDelta(this.itemTime, (float) Mathf.num(this.hasItem()), 0.05F);
		if (Mathf.dst(this.deltaX(), this.deltaY()) > 0.001F) {
			this.baseRotation = Angles.moveToward(this.baseRotation, Mathf.angle(this.deltaX(), this.deltaY()), this.type.rotateSpeed);
		}

		if (this.type.lockLegBase) {
			this.baseRotation = this.rotation;
		}

		bot = this.type.legLength;
		if (this.legs.length != this.type.legCount) {
			this.resetLegs();
		}

		left = this.type.legSpeed;
		int div = Math.max(this.legs.length / this.type.legGroupSize, 2);
		this.moveSpace = bot / 1.6F / ((float) div / 2.0F) * this.type.legMoveSpace;
		this.totalLength += this.type.legContinuousMove ? this.type.speed * this.speedMultiplier * Time.delta : Mathf.dst(this.deltaX(), this.deltaY());
		float range = this.moveSpace * 0.85F * this.type.legForwardScl;
		boolean moving = this.moving();
		Vec2 moveOffset = !moving ? Tmp.v4.setZero() : Tmp.v4.trns(Angles.angle(this.deltaX(), this.deltaY()), range);
		moveOffset = this.curMoveOffset.lerpDelta(moveOffset, 0.1F);
		this.lastDeepFloor = null;
		int deeps = 0;

		for (int i = 0; i < this.legs.length; ++i) {
			float dstRot = this.legAngle(i);
			Vec2 baseOffset = this.legOffset(Tmp.v5, i).add(this.x, this.y);
			Leg l = this.legs[i];
			l.joint.sub(baseOffset).clampLength(this.type.legMinLength * bot / 2.0F, this.type.legMaxLength * bot / 2.0F).add(baseOffset);
			l.base.sub(baseOffset).clampLength(this.type.legMinLength * bot, this.type.legMaxLength * bot).add(baseOffset);
			float stageF = (this.totalLength + (float) i * this.type.legPairOffset) / this.moveSpace;
			int stage = (int) stageF;
			int group = stage % div;
			boolean move = i % div == group;
			boolean side = i < this.legs.length / 2;
			boolean backLeg = Math.abs((float) i + 0.5F - (float) this.legs.length / 2.0F) <= 0.501F;
			if (backLeg && this.type.flipBackLegs) {
				side = !side;
			}

			if (this.type.flipLegSide) {
				side = !side;
			}

			l.moving = move;
			l.stage = moving ? stageF % 1.0F : Mathf.lerpDelta(l.stage, 0.0F, 0.1F);
			Tile tile = Vars.world.tileWorld(l.base.x, l.base.y);
			Color floorColor = tile == null ? Color.clear : tile.getFloorColor();
			Floor floor = tile == null ? Blocks.air.asFloor() : tile.floor();
			if (floor.isDeep()) {
				++deeps;
				this.lastDeepFloor = floor;
			}

			if (l.group != group) {
				if (!move && (moving || !this.type.legContinuousMove) && i % div == l.group) {
					if (!Vars.headless && !this.inFogTo(Vars.player.team())) {
						Sounds.mechStepSmall.at(l.base.x, l.base.y, this.type.stepSoundPitch + Mathf.range(this.type.stepSoundPitchRange), this.type.stepSoundVolume);

						if (this.type.stepShake > 0.0F) {
							Effect.shake(this.type.stepShake, this.type.stepShake, l.base);
						}
					}
				}

				l.group = group;
			}

			Vec2 legDest = Tmp.v1.trns(dstRot, bot * this.type.legLengthScl).add(baseOffset).add(moveOffset);
			Vec2 jointDest = Tmp.v2;
			InverseKinematics.solve(bot / 2.0F, bot / 2.0F, Tmp.v6.set(l.base).sub(baseOffset), side, jointDest);
			jointDest.add(baseOffset);
			Tmp.v6.set(baseOffset).lerp(l.base, 0.5F);
			if (move) {
				float moveFract = stageF % 1.0F;
				l.base.lerpDelta(legDest, moveFract);
				l.joint.lerpDelta(jointDest, moveFract / 2.0F);
			}

			l.joint.lerpDelta(jointDest, left / 4.0F);
			l.joint.sub(baseOffset).clampLength(this.type.legMinLength * bot / 2.0F, this.type.legMaxLength * bot / 2.0F).add(baseOffset);
			l.base.sub(baseOffset).clampLength(this.type.legMinLength * bot, this.type.legMaxLength * bot).add(baseOffset);
		}

		if (deeps != this.legs.length || !this.floorOn().isDeep()) {
			this.lastDeepFloor = null;
		}

		if (this.mineTile != null) {
			Building core = this.closestCore();
			Item item = this.getMineResult(this.mineTile);
			if (core != null && item != null && !this.acceptsItem(item) && this.within(core, 220.0F) && !this.offloadImmediately()) {
				div = ((Building) core).acceptStack(this.item(), this.stack().amount, this);
				if (div > 0) {
					Call.transferItemTo(this, this.item(), div, this.mineTile.worldx() + Mathf.range(4.0F), this.mineTile.worldy() + Mathf.range(4.0F), core);
					this.clearItem();
				}
			}

			if ((!Vars.net.client() || this.isLocal()) && !this.validMine(this.mineTile)) {
				this.mineTile = null;
				this.mineTimer = 0.0F;
			} else if (this.mining() && item != null) {
				this.mineTimer += Time.delta * this.type.mineSpeed * Vars.state.rules.unitMineSpeed(this.team());
				if (Mathf.chance(0.06 * (double) Time.delta)) {
					Fx.pulverizeSmall.at(this.mineTile.worldx() + Mathf.range(4.0F), this.mineTile.worldy() + Mathf.range(4.0F), 0.0F, item.color);
				}

				if (this.mineTimer >= 50.0F + (this.type.mineHardnessScaling ? (float) item.hardness * 15.0F : 15.0F)) {
					this.mineTimer = 0.0F;
					if (Vars.state.rules.sector != null && this.team() == Vars.state.rules.defaultTeam) {
						Vars.state.rules.sector.info.handleProduction(item, 1);
					}

					if (core != null && this.within(core, 220.0F) && ((Building) core).acceptStack(item, 1, this) == 1 && this.offloadImmediately()) {
						if (this.item() == item && !Vars.net.client()) {
							this.addItem(item);
						}

						Call.transferItemTo(this, item, 1, this.mineTile.worldx() + Mathf.range(4.0F), this.mineTile.worldy() + Mathf.range(4.0F), core);
					} else if (this.acceptsItem(item)) {
						InputHandler.transferItemToUnit(item, this.mineTile.worldx() + Mathf.range(4.0F), this.mineTile.worldy() + Mathf.range(4.0F), this);
					} else {
						this.mineTile = null;
						this.mineTimer = 0.0F;
					}
				}

				if (!Vars.headless) {
					Vars.control.sound.loop(this.type.mineSound, this, this.type.mineSoundVolume);
				}
			}
		}
	}
}
