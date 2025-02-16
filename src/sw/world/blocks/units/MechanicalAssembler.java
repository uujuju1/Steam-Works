package sw.world.blocks.units;

import arc.*;
import arc.audio.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.io.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.*;
import mindustry.world.blocks.units.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;
import sw.entities.*;
import sw.math.*;
import sw.type.*;
import sw.world.graph.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

import static mindustry.Vars.*;
import static mindustry.type.ItemStack.*;

public class MechanicalAssembler extends Block {
	private final Rect tmp = new Rect();

	public SpinConfig spinConfig = new SpinConfig();


	public Seq<UnitPlan> plans = new Seq<>();

	public int areaSize = 4;


	public float warmupSpeed = 0.014f;


	public Sound stepSound = Sounds.none;
	public float stepSoundVolume = 1f;
	public float stepSoundPitchMin = 0.8f;
	public float stepSoundPitchMax = 1.2f;

	public Sound progressSound = Sounds.none;
	public float progressSoundVolume = 1;


	public Effect stepEffect = Fx.none;

	public Effect progressEffect = Fx.none;
	public float progressEffectChance = 0.1f;


	public float armSpeed = 1;
	public float armHeight = 0.5f;
	public float armExtension = 0;
	public float armLength = 10f, armBaseLength = 10f;
	public Interp armCurve = Interp.linear;


	public TextureRegion armRegion, armBaseRegion;


	public MechanicalAssembler(String name) {
		super(name);
		solid = true;
		destructible = true;
		update = true;
		sync = true;
		rotate = true;
		configurable = true;
		commandable = true;

		consume(new ConsumeItemDynamic((MechanicalAssemblerBuild build) -> {
			if (build.getPlan() == null) return empty;
			return build.getPlan().requirements[(build.getPlan().pos.length - 1) - (build.pos.size - 1)];
		}));

		config(Integer.class, (MechanicalAssemblerBuild build, Integer index) -> {
			build.currentPlan = index;
			build.warmup = build.progress = 0f;
			if (index == -1) {
				build.pos.clear();
			} else {
				build.pos.set(plans.get(index).pos);
				build.pos.reverse();
				build.arm.changePos(build.pos.peek());
			}
		});
		configClear((MechanicalAssemblerBuild build) -> {
			build.currentPlan = -1;
			build.warmup = build.progress = 0f;
			build.pos.clear();
			build.arm.changePos(Tmp.v1.trns(build.rotdeg(), (-tilesize * (areaSize + size) + (armLength + armExtension))/2f));
		});
	}

	public Rect getRect(Rect rect, float x, float y, int rotation) {
		float len = tilesize * (areaSize + size)/2f;

		rect.setCentered(x + Geometry.d4x(rotation) * len, y + Geometry.d4y(rotation) * len, areaSize * tilesize);

		return rect;
	}

	@Override
	public boolean canPlaceOn(Tile tile, Team team, int rotation){
		Rect rect = getRect(Tmp.r1, tile.worldx() + offset, tile.worldy() + offset, rotation);
		return !team.data().buildings.contains(
			b -> b.block instanceof MechanicalAssembler a && a.getRect(Tmp.r2, b.x, b.y, b.rotation).overlaps(rect)
		);
	}

	@Override
	public void drawPlace(int x, int y, int rotation, boolean valid) {
		super.drawPlace(x, y, rotation, valid);
		spinConfig.drawPlace(this, x, y, rotation, valid);

		Drawf.dashRect(valid ? Pal.accent : Pal.remove, getRect(Tmp.r1, x * tilesize + offset, y * tilesize + offset, rotation));
	}

	@Override
	public void init() {
		updateClipRadius(areaSize * tilesize);
		super.init();
	}

	@Override
	public void load() {
		super.load();
		armRegion = Core.atlas.find(name + "-arm", "sw-mechanical-arm");
		armBaseRegion = Core.atlas.find(name + "-arm-base", "sw-mechanical-arm-base");
	}

	@Override
	public void setBars() {
		super.setBars();
		spinConfig.addBars(this);

		addBar("progress", (MechanicalAssemblerBuild e) ->
			new Bar(
				"bar.progress",
				Pal.ammo,
				() -> e.getPlan() == null ? 0 : ((e.getPlan().pos.length - 1) - (e.pos.size - 1)) / (e.getPlan().pos.length - 1f)
			)
		);

		addBar("units", build -> {
			MechanicalAssemblerBuild e = build.as();
			return new Bar(
				() -> e.getPlan() == null ?
					"[lightgray]" + Iconc.cancel :
					Core.bundle.format(
						"bar.unitcap",
						Fonts.getUnicodeStr(e.getPlan().unit.name),
		        e.team.data().countType(e.getPlan().unit),
		        Units.getStringCap(e.team)
		      ),
		    () -> Pal.power,
		    () -> e.getPlan() == null ? 0f : (float)e.team.data().countType(e.getPlan().unit) / Units.getCap(e.team)
			);
		});
	}

	@Override
	public void setStats() {
		super.setStats();
		spinConfig.addStats(stats);

		stats.add(Stat.output, table -> {
			table.row();

			plans.each(plan -> {
				table.table(Styles.grayPanel, planTable -> {
					if(plan.unit.isBanned()){
						planTable.image(Icon.cancel).color(Pal.remove).size(40);
						return;
					}

					if(plan.unit.unlockedNow()){
						planTable.image(plan.unit.uiIcon).size(40).pad(10).left().scaling(Scaling.fit);

						planTable.table(info -> {
							info.add(plan.unit.localizedName).left();
							info.row();
							info.add(
								Strings.autoFixed(plan.stepTime * plan.pos.length / 60, 1) + " " + Core.bundle.get("unit.seconds")
							).color(Color.lightGray);
            }).left();

						planTable.pane(Styles.smallPane, req -> {
							for(int i = 0; i < plan.requirements.length; i++) {
								ItemStack[] requirement = plan.requirements[i];

								if (plan.unit instanceof SWUnitType type && type.wrecks > 0) {
									Stack icon = new Stack();

									if (i >= type.wrecks) {
										icon.add(new Image(type.uiIcon).setScaling(Scaling.fit));
									} else {
										for(int j = 0; j < i; j++) {
											icon.add(new Image(type.wreckRegions[j]).setScaling(Scaling.fit));
										}
									}

									req.add(icon).height(40).pad(10).left().growX();
								} else {
									req.image(plan.unit.uiIcon).height(40).pad(10).left().scaling(Scaling.fit).growX();
								}

								for(ItemStack stack : requirement) {
									req.add(new ItemDisplay(stack.item, stack.amount, false)).pad(5).right();
								}

								req.row();
              }
            }).growX().right().pad(10).height(60);
					}else{
						planTable.image(Icon.lock).color(Pal.darkerGray).size(40);
					}
				}).growX().pad(5);
				table.row();
			});
		});
	}

	public static class UnitPlan {
		public UnitType unit;
		public ItemStack[][] requirements;
		public float stepTime = 60f;
		public Vec2[] pos;
	}

	public class MechanicalAssemblerBuild extends Building implements HasSpin {
		public SpinModule spin = new SpinModule();

		public Arm arm = new Arm().reset(rotdeg(), (-tilesize * (areaSize + size) + (armLength + armExtension))/2f);

		public int currentPlan = -1;

		public Seq<Vec2> pos = new Seq<>();

		public @Nullable Vec2 commandPos;

		public boolean invalid = false;

		public float warmup, invalidWarmup;
		public float progress;

		@Override
		public boolean acceptItem(Building source, Item item){
			return
				getPlan() != null && items.get(item) < getMaximumAccepted(item) &&
				Structs.contains(getPlan().requirements, req ->
					Structs.contains(req, stack -> stack.item == item)
				);
		}

		@Override
		public void buildConfiguration(Table table) {
			ItemSelection.buildTable(
				table,
				plans.map(p -> p.unit).retainAll(u -> u.unlockedNow() && !u.isBanned()),
				() -> currentPlan == -1 ? null : getPlan().unit,
				u -> configure(plans.indexOf(plan -> plan.unit == u))
			);
		}

		@Override
		public Integer config() {
			return currentPlan;
		}

		@Override
		public void draw() {
			Draw.rect(region, x, y);
			Draw.z(Layer.blockUnder);

			if (getPlan() != null && getPlan().unit instanceof SWUnitType type && type.wrecks > 0) {
				for (int i = 0; i < Math.min(type.wrecks, (getPlan().pos.length - 1) - (pos.size - 1)); i++) {
					Draw.rect(
						type.wreckRegions[i],
						x + Angles.trnsx(rotdeg(), tilesize * (areaSize + size)/2f),
						y + Angles.trnsy(rotdeg(), tilesize * (areaSize + size)/2f)
					);
				}
			}

			drawArm();

			if (getPlan() != null) {
				Draw.z(Layer.buildBeam);

				Draw.mixcol(Pal.accent, Pal.breakInvalid, invalidWarmup);

				Draw.rect(
					getPlan().unit.fullIcon,
					x + Angles.trnsx(rotdeg(), tilesize * (areaSize + size)/2f),
					y + Angles.trnsy(rotdeg(), tilesize * (areaSize + size)/2f)
				);

				float len = Vars.tilesize * (areaSize)/2f;

				Drawf.dashRectBasic(
					x - len + Angles.trnsx(rotdeg(), tilesize * (areaSize + size)/2f),
					y - len + Angles.trnsy(rotdeg(), tilesize * (areaSize + size)/2f),
					len * 2f,
					len * 2f
				);

				Draw.reset();
			}
			Draw.z(Layer.block);
		}

		public void drawArm() {
			Draw.z(Layer.groundUnit + 1);

			Tmp.v1.set(arm.startPos).lerp(arm.targetPos, armCurve.apply(Mathf.clamp(arm.time/getArmTime())));

			float sx = x + Tmp.v1.x + Angles.trnsx(rotdeg(), tilesize * (areaSize + size)/2f);
			float sy = y + Tmp.v1.y + Angles.trnsy(rotdeg(), tilesize * (areaSize + size)/2f);
			float tx = x, ty = y;

			Tmp.v3.set(sx, sy);
			Tmp.v1.set(tx, ty).sub(Tmp.v3);

			InverseKinematics.solve(armLength, armBaseLength, Tmp.v1, true, Tmp.v2);

			Tmp.v2.add(Tmp.v3);
			Tmp.v1.add(Tmp.v3);
			Tmp.v4.set(Parallax.getParallaxFrom(Tmp.v2, Core.camera.position, armHeight)).sub(Tmp.v3).setLength(armExtension);

			Lines.stroke(armBaseRegion.height/4f);
			Lines.line(armBaseRegion, Tmp.v1.x, Tmp.v1.y, Tmp.v2.x, Tmp.v2.y, false);
			Lines.stroke(armRegion.height/4f);
			Lines.line(armRegion, Tmp.v2.x + Tmp.v4.x, Tmp.v2.y + Tmp.v4.y, Tmp.v3.x, Tmp.v3.y, false);

			Draw.reset();
		}

		public float getArmTime() {
			return arm.startPos.dst(arm.targetPos) / armSpeed;
		}

		@Override
		public Vec2 getCommandPosition(){
			return commandPos;
		}

		public @Nullable UnitPlan getPlan() {
			return currentPlan == -1 ? null : plans.get(currentPlan);
		}

		@Override
		public void onCommand(Vec2 target){
			commandPos = target;
		}

		@Override
		public void onProximityUpdate() {
			super.onProximityUpdate();

			new SpinGraph().mergeFlood(this);
		}
		@Override
		public void onProximityRemoved() {
			super.onProximityRemoved();

			spinGraph().remove(this, true);
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			spin.read(read);

			currentPlan = read.i();

			warmup = read.f();
			invalidWarmup = read.f();
			progress = read.f();

			commandPos = TypeIO.readVecNullable(read);

			byte size = read.b();
			for(int i = 0; i < size; i++) {
				pos.add(TypeIO.readVec2(read));
			}

			arm.startPos = TypeIO.readVec2(read);
			arm.targetPos = TypeIO.readVec2(read);
			arm.time = read.f();
		}

		@Override
		public boolean shouldConsume() {
			return
				getPlan() != null &&
				super.shouldConsume() &&
				!invalid && team.data().countType(getPlan().unit) < Units.getCap(team);
		}

		@Override public SpinModule spin() {
			return spin;
		}
		@Override public SpinConfig spinConfig() {
			return spinConfig;
		}

		@Override
		public float totalProgress() {
			return spinGraph().rotation * spinSection().ratio;
		}

		@Override
		public void updateTile() {
			Rect rect = getRect(tmp, x, y, rotation);

			if (efficiency > 0) {
				warmup = Mathf.approachDelta(warmup, 1f, warmupSpeed);

				arm.time += edelta();

				if (arm.time > getArmTime() && getPlan() != null) {
					progress += getProgressIncrease(getPlan().stepTime);

					Tmp.v1.set(arm.targetPos).add(rect.x + rect.width/2f, rect.y + rect.height/2f);
					Vars.control.sound.loop(progressSound, Tmp.v1, progressSoundVolume * warmup);
					if (Mathf.chanceDelta(progressEffectChance)) progressEffect.at(Tmp.v1);
				}
			} else {
				warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
			}

			if (getPlan() != null && progress >= 1f) {
				if (pos.size == 1) {
					if (!Vars.net.client()) {
						Unit u = getPlan().unit.create(team);
						u.set(
							rect.x + rect.width/2f,
							rect.y + rect.height/2f
						);
						u.rotation = 90f;
						if (u.isCommandable()) u.command().commandPosition(commandPos);
						u.add();
					}

					pos.set(getPlan().pos);
					pos.reverse();
					arm.changePos(pos.peek());
				} else {
					pos.pop();
					arm.changePos(pos.peek());
				}
				stepEffect.at(
					rect.x + rect.width/2f,
					rect.y + rect.height/2f
				);
				stepSound.at(x + arm.targetPos.x, y + arm.targetPos.y, Mathf.random(stepSoundPitchMin, stepSoundPitchMax), stepSoundVolume);
				progress %= 1f;
				consume();
			}

			if (
				getPlan() != null &&
				(
					(!getPlan().unit.flying && collisions.overlapsTile(rect, EntityCollisions::solid)) ||
					Units.anyEntities(
						rect.x, rect.y, rect.width, rect.height, unit ->
						!unit.spawnedByCore &&(
							(unit.type.allowLegStep && getPlan().unit.allowLegStep) ||
							(getPlan().unit.flying && unit.isFlying()) ||
							(!getPlan().unit.flying && unit.isGrounded())
						)
					)
				)
			) {
				invalidWarmup = Mathf.approachDelta(invalidWarmup, 1f, warmupSpeed);
				invalid = true;
			} else {
				invalidWarmup = Mathf.approachDelta(invalidWarmup, 0f, warmupSpeed);
				invalid = false;
			}
		}

		@Override
		public void write(Writes write) {
			super.write(write);
			spin.write(write);

			write.i(currentPlan);

			write.f(warmup);
			write.f(invalidWarmup);
			write.f(progress);

			TypeIO.writeVecNullable(write, commandPos);

			write.b(pos.size);
			for(Vec2 vec : pos) {
				TypeIO.writeVec2(write, vec);
			}

			TypeIO.writeVec2(write, arm.startPos);
			TypeIO.writeVec2(write, arm.targetPos);
			write.f(arm.time);
		}
	}
}
