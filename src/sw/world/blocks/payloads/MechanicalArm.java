package sw.world.blocks.payloads;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.core.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.io.*;
import mindustry.world.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.consumers.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import sw.world.graph.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class MechanicalArm extends PayloadBlock {
	public SpinConfig spinConfig;

	public DrawBlock drawer = new DrawDefault();

	public boolean consumerScaleEfficiency = true;

	public float range = 80f;

	public float payloadCapacity = 3f;

	public float armSpeed = 0.5f;
	public Interp armProgress = Interp.linear;

	public MechanicalArm(String name) {
		super(name);
		configurable = true;
		update = true;
		rotate = true;
		drawArrow = false;

		config(ArmConfig.class, (MechanicalArmBuild build, ArmConfig value) -> {
			if (value.takeOffset != null) {
				build.takePos = Point2.unpack(build.pos()).add(value.takeOffset).pack();
			} else {
				build.takePos = value.takePos;
			}
			if (value.putOffset != null) {
				build.putPos = Point2.unpack(build.pos()).add(value.putOffset).pack();
			} else {
				build.putPos = value.putPos;
			}

			build.updateTargets();
			build.updatePath();
		});
	}

	@Override
	public void drawPlanConfigTop(BuildPlan plan, Eachable<BuildPlan> list) {
		drawer.drawPlan(this, plan, list);
	}

	@Override
	public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
		if (spinConfig != null) spinConfig.drawPlace(this, plan.x, plan.y, plan.rotation, true);
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
	public void drawOverlay(float x, float y, int rotation) {
		Drawf.dashCircle(x, y, range, Pal.accent);
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

		stats.add(Stat.payloadCapacity, StatValues.squared(payloadCapacity, StatUnit.blocksSquared));
		stats.add(Stat.range, range / 8f, StatUnit.blocks);
		stats.add(Stat.speed, armSpeed / 8f, StatUnit.tilesSecond);
	}

	public static class ArmConfig {
		public int takePos = -1;
		public int putPos = -1;

		public Point2 takeOffset;
		public Point2 putOffset;

		public ArmConfig(int takePos, int putPos) {
			this.takePos = takePos;
			this.putPos = putPos;
		}
		public ArmConfig(Point2 takeOffset, Point2 putOffset) {
			this.takeOffset = takeOffset;
			this.putOffset = putOffset;
		}
	}

	public class MechanicalArmBuild extends PayloadBlockBuild<Payload> implements HasSpin, HasArm {
		public SpinModule spin;

		public int takePos = -1;
		public int putPos = -1;

		public Vec2 targetPos = new Vec2();
		public Vec2 startPos = new Vec2();
		public Vec2 currentArmPos = new Vec2();
		public float moveProgress;

		public boolean puttingConfig;

		@Override
		public boolean acceptPayload(Building source, Payload payload) {
			return super.acceptPayload(source, payload) && payload.size() / 8f < payloadCapacity;
		}

		public void act() {
			boolean didSomething = false;
			if (payload == null) {
				if (take(getTakeBuild())) {
					didSomething = true;
				} else {
					Seq<Unit> units = Groups.unit.intersect(targetPos.x - 4, targetPos.y - 4, 8, 8).removeAll(u -> u.controller() instanceof Player || u.hitSize / 8 > payloadCapacity);
					if (!units.isEmpty() && take(units.first())) didSomething = true;
				}
			} else {
				if (putPayload()) didSomething = true;
			}

			if (didSomething) updateTargets();
		}

		@Override public ArmConfig config() {
			return new ArmConfig(Point2.unpack(takePos).sub(tileX(), tileY()), Point2.unpack(putPos).sub(tileX(), tileY()));
		}

		@Override
		public Building create(Block block, Team team) {
			if (spinConfig != null) spin = new SpinModule();

			return super.create(block, team);
		}

		@Override
		public void draw() {
			super.draw();

			drawer.draw(this);

			updatePath();
			payVector.set(currentArmPos.x - x, currentArmPos.y - y);
			if (Vars.world.tile(takePos) != null && Vars.world.tile(putPos) != null){
				Draw.z(Layer.blockOver);
				drawPayload();
//				Draw.z(Layer.blockOver + 0.1f);
//				drawArm(x, y, currentArmPos.x - x, currentArmPos.y - y);
			}
		}

		@Override
		public void drawConfigure() {
			drawOverlay(x, y, rotation);

			Draw.color(puttingConfig ? Pal.accent : Pal.lancerLaser);
			Lines.stroke(1f);
			Lines.square(x, y, size * 4f + 1f);
			Draw.reset();

			drawSelection();
		}

		@Override public void drawLight() {
			drawer.drawLight(this);
		}

		@Override
		public void drawSelect() {
			super.drawSelect();
			if (spinConfig != null) spinConfig.drawPlace(block, tileX(), tileY(), rotation, true);

			drawSelection();
		}

		public void drawSelection() {
			float scl = 8f + Mathf.absin(6f, 2f);

			Draw.color(Pal.lancerLaser);
			if (Vars.world.tile(takePos) != null) {
				Building build = Vars.world.build(takePos);
				if (build != null) {
					Draw.rect("sw-icon-take-pos", build.x, build.y, scl, scl);
				} else {
					Point2 pos = Point2.unpack(takePos);
					Draw.rect("sw-icon-take-pos", pos.x * 8f, pos.y * 8f, scl, scl);
				}
			}

			Draw.color(Pal.accent);
			if (Vars.world.tile(putPos) != null) {
				Building build = Vars.world.build(putPos);
				if (build != null) {
					Draw.rect("sw-icon-put-pos", build.x, build.y, scl, scl);
				} else {
					Point2 pos = Point2.unpack(putPos);
					Draw.rect("sw-icon-put-pos", pos.x * 8f, pos.y * 8f, scl, scl);
				}
			}

			Draw.scl(1);
		}

		@Override
		public float efficiencyScale() {
			float mul = 1f;
			if (consumerScaleEfficiency) for(Consume cons : consumers) {
				mul *= cons.efficiencyMultiplier(this);
			}
			return mul;
		}

		@Override public Vec2 getArmPos() {
			return currentArmPos;
		}

		public @Nullable Building getPutBuild() {
			return Vars.world.build(putPos);
		}
		public @Nullable Building getTakeBuild() {
			return Vars.world.build(takePos);
		}

		@Override
		public boolean onConfigureBuildTapped(Building other) {
			if (other == this) puttingConfig = !puttingConfig;
			return false;
		}

		@Override
		public boolean onConfigureTapped(float x, float y) {
			if (dst(x, y) > range) return false;

			Tile selected = Vars.world.tileWorld(x, y);

			if (selected != null && Vars.world.buildWorld(x, y) != this) {
				if (selected.pos() != (puttingConfig ? putPos : takePos)) selectPos(selected.pos());
				return true;
			}
			return false;
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
		public void placed() {
			super.placed();
			if (Vars.world.tile(takePos) == null) takePos = tile.nearby(Mathf.mod(rotation + 1, 4)).pos();
			if (Vars.world.tile(putPos) == null) putPos = tile.nearby(Mathf.mod(rotation - 1, 4)).pos();
			currentArmPos.trns(rotdeg() + 90f, 8f).add(x, y);
			updateTargets();
		}

		// shamelessly stolen from vanilla PayloadComp and probably does not work on multiplayer
		public boolean putPayload() {
			Tile on = Vars.world.tile(putPos);

			//clear removed state of unit so it can be synced
			if(Vars.net.client() && payload instanceof UnitPayload u) Vars.netClient.clearRemovedEntity(u.unit.id);

			//drop off payload on an acceptor if possible
			if(on != null && on.build != null && on.build.team == team && on.build.acceptPayload(this, payload)){
				on.build.handlePayload(on.build, takePayload());
				return true;
			}

			if(payload instanceof BuildPayload b){
				return putPayloadBlock(b);
			}else if(payload instanceof UnitPayload p){
				return putPayloadUnit(p);
			}
			return false;
		}
		public boolean putPayloadBlock(BuildPayload payload) {
			Building tile = payload.build;
			int tx = World.toTile(targetPos.x - tile.block.offset), ty = World.toTile(targetPos.y - tile.block.offset);
			Tile on = Vars.world.tile(putPos);
			if(on != null && Build.validPlace(tile.block, tile.team, tx, ty, tile.rotation, false)){
				payload.place(on, tile.rotation);
				payload.build.lastAccessed = lastAccessed;
				this.payload = null;
				return true;
			}

			return false;
		}
		public boolean putPayloadUnit(UnitPayload payload) {
			Unit u = payload.unit;

			if(!u.canPass(World.toTile(targetPos.x), World.toTile(targetPos.y)) || Units.count(targetPos.x, targetPos.y, u.physicSize(), o -> true) > 1) return false;

			if(Vars.net.client()) return true;

			u.set(targetPos);
			u.id = EntityGroup.nextId();
			if(!u.isAdded()) u.team.data().updateCount(u.type, -1);
			u.add();
			u.unloaded();
			this.payload = null;

			return true;
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);

			if (spinConfig != null) (spin == null ? new SpinModule() : spin).read(read);

			takePos = read.i();
			putPos = read.i();

			TypeIO.readVec2(read, targetPos);
			TypeIO.readVec2(read, startPos);
			moveProgress = read.f();
		}

		public void selectPos(int pos) {
			configure(new ArmConfig(puttingConfig ? takePos : pos, puttingConfig ? pos : putPos));
		}

		@Override public boolean shouldConsume() {
			return super.shouldConsume() && takePos != -1 && putPos != -1;
		}

		public boolean take(@Nullable Building build) {
			if (build == null) return false;
			if (build.getPayload() != null && acceptPayload(build, build.getPayload())) {
				payload = build.takePayload();
				return true;
			}
			if (build.hitSize() / 8f <= payloadCapacity) {
				build.pickedUp();
				build.tile.remove();
				build.afterPickedUp();
				payload = new BuildPayload(build);
				build.payloadRotation = payRotation = build.rotation * 90f;

				return true;
			}
			return false;
		}
		public boolean take(Unit unit) {
			if (unit.hitSize / 8f > payloadCapacity || unit.controller() instanceof Player) return false;
			if (unit.isAdded()) unit.team.data().updateCount(unit.type, 1);

			unit.remove();
			payRotation = unit.rotation;
			payload = new UnitPayload(unit);
			if (Vars.net.client()) {
				Vars.netClient.clearRemovedEntity(unit.id);
			}
			return true;
		}

		public void updatePath() {
			if (Float.isNaN(moveProgress / (targetPos.dst(startPos)/armSpeed))) {
				currentArmPos.set(targetPos);
				return;
			}
			Bezier.quadratic(
				currentArmPos,
				armProgress.apply(Mathf.clamp(moveProgress / (targetPos.dst(startPos)/armSpeed))),
				startPos,
				Tmp.v1.set(startPos).lerp(targetPos, 0.5f).add(Tmp.v3.trns((angleTo(startPos) + angleTo(targetPos)) / 2f, startPos.dst(targetPos)/2f)),
				targetPos,
				Tmp.v2
			);
		}

		public void updateTargets() {
			startPos.set(currentArmPos);
			moveProgress = 0f;
			if (payload == null) {
				if (Vars.world.tile(takePos) != null) {
					Tile tile = Vars.world.tile(takePos);
					targetPos.set(tile.worldx(), tile.worldy());
				}
				if (getTakeBuild() != null) targetPos.set(getTakeBuild());
			} else {
				if (Vars.world.tile(putPos) != null) {
					Tile tile = Vars.world.tile(putPos);
					targetPos.set(tile.worldx() + (payload instanceof BuildPayload buildPayload ? buildPayload.block().offset : 0f), tile.worldy() + (payload instanceof BuildPayload buildPayload ? buildPayload.block().offset : 0f));
				}
				if (getPutBuild() != null) targetPos.set(getPutBuild());
			}
		}

		@Override
		public void updateTile() {
			if (efficiency > 0) {
				float total = targetPos.dst(startPos)/armSpeed;

				moveProgress += edelta();

				if (moveProgress >= total) act();
			}
		}

		@Override
		public void write(Writes write) {
			super.write(write);

			if (spinConfig != null) spin.write(write);

			write.i(takePos);
			write.i(putPos);

			TypeIO.writeVec2(write, targetPos);
			TypeIO.writeVec2(write, startPos);
			write.f(moveProgress);
		}
	}
}
