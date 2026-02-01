package sw.world.blocks.payloads;

import arc.*;
import arc.audio.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.meta.*;
import sw.math.*;
import sw.type.units.*;

import static sw.world.blocks.payloads.PayloadCourierPort.PayloadCourierState.*;

public class PayloadCourierPort extends PayloadBlock {
	public UnitType unitType;
	public float buildTime = 60f;

	public float range = 80f;

	public float payloadCapacity = 3f;

	public Effect courierCreateEffect = Fx.none;
	public Sound courierCreateSound = Sounds.none;
	public float courierCreateSoundVolume = 1f;

	public Effect courierRemoveEffect = Fx.none;
	public Sound courierRemoveSound = Sounds.none;
	public float courierRemoveSoundVolume = 1f;

	public PayloadCourierPort(String name) {
		super(name);
		update = true;
		solid = true;
		sync = true;
		rotate = true;
		configurable = true;
		outputsPayload = true;
		rotateDraw = false;
		rotateDrawEditor = false;

		config(Integer.class, (PayloadCourierPortBuild build, Integer link) -> {
			build.link = link;
			build.buildCounter = 0f;
			if (build.unit != null && build.unit.isValid()) Call.unitDespawn(build.unit);
			build.unit = null;
		});
		config(Point2.class, (PayloadCourierPortBuild build, Point2 link) -> {
			build.link = Point2.pack(build.tileX() + link.x, build.tileY() + link.y);
			build.buildCounter = 0f;
			if (build.unit != null && build.unit.isValid()) Call.unitDespawn(build.unit);
			build.unit = null;
		});
	}

	@Override
	public void drawDefaultPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
		super.drawDefaultPlanRegion(plan, list);
		Draw.rect(outRegion, plan.drawx(), plan.drawy(), plan.rotation * 90f);
	}

	@Override
	public void drawOverlay(float x, float y, int rotation) {
		Drawf.dashCircle(x, y, range, Pal.accent);
	}

	@Override
	public void setStats() {
		stats.timePeriod = buildTime;

		super.setStats();

		stats.add(Stat.range, range / 8f, StatUnit.blocks);
		stats.add(Stat.payloadCapacity, StatValues.squared(payloadCapacity, StatUnit.blocksSquared));
		stats.add(Stat.reload, 60f / buildTime, StatUnit.perSecond);
	}

	public class PayloadCourierPortBuild extends PayloadBlockBuild<Payload> {
		public @Nullable Unit unit;
		public int unitID = -1;
		public int link = -1;
		public float buildCounter;
		public PayloadCourierState state = idle;
		public Queue<PayloadCourierPortBuild> launchers = new Queue<>();

		@Override
		public boolean acceptPayload(Building source, Payload payload) {
			return super.acceptPayload(source, payload) && payload.fits(payloadCapacity) && launchers.isEmpty();
		}

		@Override
		public void draw() {
			super.draw();
			if (getLink() == null) Draw.rect(outRegion, x, y, rotdeg());
			drawPayload();

			if (payload == null || getLink() == null) return;

			TextureRegion buildingUnitRegion = unitType.region;
			float prog = Mathf.clamp(buildCounter/buildTime);

			Draw.scl(prog);
			Draw.z(unitType.flyingLayer);
			Tmp.v1.set(x, y);
			if (unitType instanceof BalloonUnitType u) {
				Parallax.getParallaxFrom(Tmp.v1, Core.camera.position, u.bodyHeight * prog);
				Draw.color(u.outlineColor);
				Draw.alpha(Mathf.clamp(Core.camera.position.dst(this) / u.ropeSpacing));
				for (Point2 offset : Geometry.d4) {
					Lines.stroke(u.ropeThickness * prog);
					Lines.line(
						x + payload.size() / 2f * offset.x,
						y + payload.size() / 2f * offset.y,
						Tmp.v1.x + u.ropeSpacing * offset.x * prog,
						Tmp.v1.y + u.ropeSpacing * offset.y * prog,
						false
					);
				}
				Draw.color();
				Draw.mixcol();
				Draw.alpha(Mathf.clamp(Core.camera.position.dst(this) / u.ropeSpacing));
			}
			Draw.rect(buildingUnitRegion, Tmp.v1.x, Tmp.v1.y);
			Draw.z(Layer.darkness - 1);
			Draw.mixcol(Pal.shadow, 1);
			Draw.alpha(0.3f);
			Draw.rect(buildingUnitRegion, x + UnitType.shadowTX * prog, y + UnitType.shadowTY * prog);
			Draw.scl(1);
		}

		@Override
		public void drawConfigure() {
			Drawf.dashCircle(x, y, range, Pal.accent);

			float sin = Mathf.absin(Time.time, 6f, 1f);

			Drawf.circles(x, y, hitSize() / 2 * Mathf.sqrt2 + sin);
			PayloadCourierPortBuild link = getLink();
			if (link != null) {
				Drawf.circles(link.x, link.y, link.hitSize() / 2f * Mathf.sqrt2 + sin, Pal.place);
				Drawf.arrow(x, y, link.x, link.y, (hitSize() / 2f + hitSize() / 3f) * Mathf.sqrt2 + sin, hitSize() / 6f + sin);
			}
		}

		public void checkState() {
			if ((getPayload() != null || unit != null) && getLink() != null) state = launching;
			if (getPayload() == null) state = awaiting;
			if (getPayload() != null && getLink() == null) state = idle;
		}

		@Override
		public Point2 config() {
			if(tile == null) return null;
			return Point2.unpack(link).sub(tile.x, tile.y);
		}

		public @Nullable PayloadCourierPortBuild getLink() {
			Building build = Vars.world.build(link);

			return (build != null && build.isValid() && build instanceof PayloadCourierPortBuild link) ? link : null;
		}

		public boolean launch() {
			if (!getLink().launchers.contains(this)) getLink().launchers.addLast(this);
			if ((unit == null || !unit.isValid()) && getLink().launchers.first() == this && getLink().state == awaiting && getLink().payload == null) {
				if (!Vars.net.client()) {
					unit = unitType.create(team);
					if (unit instanceof BuildingTetherc tether) {
						tether.building(this);
					}
					if (unit instanceof Payloadc tether) {
						tether.addPayload(payload);
						payload = null;
					}
					unit.set(x, y);
					unit.rotation(90f);
					unit.add();
					Call.unitTetherBlockSpawned(tile, unit.id);
					consume();
				}
				courierCreateEffect.at(this);
				courierCreateSound.at(this, 1, courierCreateSoundVolume);

//				getLink().launchers.removeFirst();
//				getLink().launchers.addLast(this);
//				getLink().handlePayload(this, getPayload());
//				payload = null;
//				getLink().checkState();
				return true;
			}
			return false;
		}

		@Override
		public boolean onConfigureBuildTapped(Building build) {
			if (build == this) {
				configure(-1);
				return false;
			}
			if (build.isValid() && build instanceof PayloadCourierPortBuild other && other.dst(this) <= range) {
				if (other == getLink()) {
					configure(-1);
				} else {
					configure(other.pos());
					if (other.getLink() == this) {
						other.configure(-1);
						return true;
					}
				}
				return false;
			}
			return true;
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);

			link = read.i();
			unitID = read.i();
			state = PayloadCourierState.values()[read.b()];
			buildCounter = read.f();
		}

		public void removeCourier() {
			unit.remove();
			courierRemoveEffect.at(unit);
			courierRemoveSound.at(unit, 1, courierRemoveSoundVolume);
		}

		@Override public boolean shouldConsume() {
			return super.shouldConsume() && getLink() != null;
		}

		@Override
		public void updateTile() {
			super.updateTile();

			checkState();

			if (unitID != -1) {
				unit = Groups.unit.getByID(unitID);
				unitID = -1;
			}

			if (!launchers.isEmpty() && (
				launchers.first() == null ||
				!launchers.first().isValid() ||
				launchers.first().efficiency <= 0 ||
				launchers.first().getLink() != this)
			) {
				launchers.removeFirst();
			}

			switch (state) {
				case launching -> {
					moveInPayload(false);

					buildCounter += edelta();

					if (buildCounter > buildTime) {
						if (launch()) buildCounter = 0;
					}
				}
				case awaiting -> {}
				case idle -> moveOutPayload();
			}

		}

		@Override
		public void write(Writes write) {
			super.write(write);

			write.i(link);
			write.i(unit == null ? -1 : unit.id);
			write.b((byte) state.ordinal());
			write.f(buildCounter);
		}
	}

	public enum PayloadCourierState {
		idle,
		awaiting,
		launching,
	}
}
