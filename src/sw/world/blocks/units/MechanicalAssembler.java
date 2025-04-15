package sw.world.blocks.units;

import arc.*;
import arc.audio.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.scene.ui.layout.Stack;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.io.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.*;
import mindustry.world.consumers.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import sw.entities.*;
import sw.type.*;
import sw.world.graph.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

import java.util.*;

import static mindustry.Vars.*;
import static mindustry.type.ItemStack.*;

public class MechanicalAssembler extends Block {
	private static final Rect tmp = new Rect();

	public SpinConfig spinConfig = new SpinConfig();

	public Seq<MechanicalAssemblerPlan> plans = new Seq<>();

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
	public float armStartingOffset = 0;

	public DrawBlock drawer = new DrawDefault();

	public static Seq<ObjectMap.Entry<ItemStack[], Vec2>> tmpEntries = new Seq<>();

	public MechanicalAssembler(String name) {
		super(name);
		solid = true;
		destructible = true;
		update = true;
		sync = true;
		rotate = true;
		configurable = true;
		commandable = true;
		saveConfig = true;

		consume(new ConsumeItemDynamic((MechanicalAssemblerBuild build) -> {
			if (build.currentStep != null) return build.currentStep.key;
			return empty;
		}));

		config(Integer.class, (MechanicalAssemblerBuild build, Integer index) -> {
			build.currentPlan = index;
			build.warmup = build.progress = 0f;
			build.progressCounter = 0;
			build.requiredSteps.clear();

			if (index != -1) {
				build.getPlan().steps.each((req, pos) -> build.requiredSteps.add(new ObjectMap.Entry<>() {{
					key = req;
					value = pos;
				}}));

				build.pick();
				if (build.currentStep != null) build.arm.changePos(build.currentStep.value);

				build.team.data().buildings.each(
					b -> b instanceof AssemblerArm.AssemblerArmBuild other && other.link == build,
					b -> {
					  ((AssemblerArm.AssemblerArmBuild) b).pick();
					  if (((AssemblerArm.AssemblerArmBuild) b).currentStep != null) ((AssemblerArm.AssemblerArmBuild) b).arm.changePos(((AssemblerArm.AssemblerArmBuild) b).currentStep.value);
					}
				);
			} else {
				build.arm.reset(build.rotdeg(), armStartingOffset);
				build.team.data().buildings.each(
					b -> b instanceof AssemblerArm.AssemblerArmBuild other && other.link == build,
					b -> {
						((AssemblerArm.AssemblerArmBuild) b).currentStep = null;
						((AssemblerArm.AssemblerArmBuild) b).arm.reset(b.rotdeg(), ((AssemblerArm) b.block).armStartingOffset);
					}
				);
			}
		});
		configClear((MechanicalAssemblerBuild build) -> {
			build.currentPlan = -1;
			build.warmup = build.progress = 0f;
			build.progressCounter = 0;
			build.requiredSteps.clear();
			build.currentStep = null;
			build.arm.reset(build.rotdeg(), armStartingOffset);

			build.team.data().buildings.each(
				b -> b instanceof AssemblerArm.AssemblerArmBuild other && other.link == build,
				b -> {
					((AssemblerArm.AssemblerArmBuild) b).currentStep = null;
					((AssemblerArm.AssemblerArmBuild) b).arm.reset(b.rotdeg(), ((AssemblerArm) b.block).armStartingOffset);
				}
			);
		});
	}

	@Override
	public boolean canPlaceOn(Tile tile, Team team, int rotation) {
		Rect rect = getRect(Tmp.r1, tile.worldx() + offset, tile.worldy() + offset, rotation);
		return !team.data().buildings.contains(
			b -> b.block instanceof MechanicalAssembler a && a.getRect(Tmp.r2, b.x, b.y, b.rotation).overlaps(rect)
		);
	}

	@Override
	public void drawPlace(int x, int y, int rotation, boolean valid) {
		super.drawPlace(x, y, rotation, valid);
		Drawf.dashRect(valid ? Pal.accent : Pal.remove, getRect(Tmp.r1, x * tilesize + offset, y * tilesize + offset, rotation));
	}

	@Override
	public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
		spinConfig.drawPlace(this, plan.x, plan.y, plan.rotation, true);
		drawer.drawPlan(this, plan, list);
	}

	@Override
	public void drawPlanConfigTop(BuildPlan plan, Eachable<BuildPlan> list) {
		drawer.drawPlan(this, plan, list);
	}

	public Rect getRect(Rect rect, float x, float y, int rotation) {
		float len = tilesize * (areaSize + size)/2f;

		rect.setCentered(x + Geometry.d4x(rotation) * len, y + Geometry.d4y(rotation) * len, areaSize * tilesize);

		return rect;
	}

	@Override public void getRegionsToOutline(Seq<TextureRegion> out) {
		drawer.getRegionsToOutline(this, out);
	}

	@Override protected TextureRegion[] icons() {
		return drawer.finalIcons(this);
	}

	@Override
	public void init() {
		updateClipRadius(areaSize * tilesize);
		super.init();
	}

	@Override
	public void load() {
		super.load();
		drawer.load(this);
	}

	@Override
	public void setBars() {
		super.setBars();
		spinConfig.addBars(this);

		addBar("progress", (MechanicalAssemblerBuild e) ->
			new Bar(
				"bar.progress",
				Pal.ammo,
				() -> e.getPlan() == null ? 0 : e.progressCounter/(e.getPlan().steps.size - 1f)
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

					if(plan.unit.unlockedNow()) {
						planTable.image(plan.unit.uiIcon).size(40).pad(10).left().scaling(Scaling.fit);

						planTable.table(info -> {
							info.add(plan.unit.localizedName).left();
							info.row();
							info.add(
								Strings.autoFixed(plan.stepTime * plan.steps.size / 60, 1) + " " + Core.bundle.get("unit.seconds")
							).color(Color.lightGray);
            }).left();

						planTable.pane(Styles.smallPane, req -> {
//							for(int i = 0; i < plan.requirements.length; i++) {
//								ItemStack[] requirement = plan.requirements[i];
//
//								if (plan.unit instanceof SWUnitType type && type.wrecks > 0) {
//									Stack icon = new Stack();
//
//									if (i >= type.wrecks) {
//										icon.add(new Image(type.uiIcon).setScaling(Scaling.fit));
//									} else {
//										for(int j = 0; j < i; j++) {
//											icon.add(new Image(type.wreckRegions[j]).setScaling(Scaling.fit));
//										}
//									}
//
//									req.add(icon).height(40).pad(10).left().growX();
//								} else {
//									req.image(plan.unit.uiIcon).height(40).pad(10).left().scaling(Scaling.fit).growX();
//								}
//
//								for(ItemStack stack : requirement) {
//									req.add(new ItemDisplay(stack.item, stack.amount, false)).pad(5).right();
//								}
//
//								req.row();
//              }
							plan.steps.each((requirement, pos) -> {

								if (plan.unit instanceof SWUnitType type && type.wrecks > 0) {
									Stack icon = new Stack();

									if (plan.steps.keys().toSeq().indexOf(requirement) >= type.wrecks) {
										icon.add(new Image(type.uiIcon).setScaling(Scaling.fit));
									} else {
										for(int j = 0; j < plan.steps.keys().toSeq().indexOf(requirement); j++) {
											icon.add(new Image(type.wreckRegions[j]).setScaling(Scaling.fit));
										}
									}

									req.add(icon).height(40).pad(10).left().growX();
								} else {
									req.image(plan.unit.uiIcon).height(40).pad(10).left().scaling(Scaling.fit).growX();
								}

								for(ItemStack stack : requirement) {
//									req.add(new ItemDisplay(stack.item, stack.amount, false)).pad(5).right();
									req.add("dont forget to fix this :3");
								}

								req.row();
              });
            }).growX().right().pad(10).height(60);
					} else {
						planTable.image(Icon.lock).color(Pal.darkerGray).size(40);
					}
				}).growX().pad(5);
				table.row();
			});
		});
	}

	public static class MechanicalAssemblerPlan {
		public UnitType unit;
		public ItemStack[][] requirements;
		public float stepTime = 60f;
		public Vec2[] pos;

		public ObjectMap<ItemStack[], Vec2> steps = new ObjectMap<>();
	}

	public class MechanicalAssemblerBuild extends Building implements HasSpin, HasArm {
		public SpinModule spin = new SpinModule();

		public Arm arm = new Arm().reset(rotdeg(), armStartingOffset);

		public int currentPlan = -1;

		public int progressCounter = 0;

		public Seq<ObjectMap.Entry<ItemStack[], Vec2>> requiredSteps = new Seq<>();
		public ObjectMap.Entry<ItemStack[], Vec2> currentStep;

		public @Nullable Vec2 commandPos;

		public boolean invalid = false;

		public float warmup, invalidWarmup;
		public float progress;

		@Override
		public boolean acceptItem(Building source, Item item) {
			return
				getPlan() != null && currentStep != null &&
				Structs.contains(currentStep.key, stack -> stack.item == item) &&
				items.get(item) < Structs.find(currentStep.key, stack -> stack.item == item).amount * 2;
		}

		@Override public Arm arm() {
			return arm;
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

		@Override public Integer config() {
			return currentPlan;
		}

		@Override
		public void draw() {
			drawer.draw(this);
			Draw.z(Layer.blockUnder);

			if (getPlan() != null && getPlan().unit instanceof SWUnitType type && type.wrecks > 0) {
				for (int i = 0; i < Math.min(type.wrecks, progressCounter); i++) {
					Draw.rect(
						type.wreckRegions[i],
						x + Angles.trnsx(rotdeg(), tilesize * (areaSize + size)/2f),
						y + Angles.trnsy(rotdeg(), tilesize * (areaSize + size)/2f)
					);
				}
			}

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
		@Override public void drawLight() {
			drawer.drawLight(this);
		}
		@Override public void drawSelect() {
			spinConfig.drawPlace(block, tileX(), tileY(), rotation, true);
		}

		@Override public float getArmTime() {
			return arm.startPos.dst(arm.targetPos) / armSpeed;
		}

		@Override public Vec2 getCommandPosition() {
			return commandPos;
		}

		public @Nullable MechanicalAssemblerPlan getPlan() {
			return currentPlan == -1 ? null : plans.get(currentPlan);
		}

		@Override public void onCommand(Vec2 target) {
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

		public void pick() {
			if (getPlan() != null) {
				currentStep = requiredSteps.pop();
			} else {
				currentStep = null;
			}
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

			progressCounter = read.i();

			if (getPlan() != null) {
				tmpEntries.clear();
				getPlan().steps.each((req, pos) -> tmpEntries.add(new ObjectMap.Entry<>() {{
					key = req;
					value = pos;
				}}));

				byte size = read.b();
				for (int i = 0; i < size; i++) {
					int index = read.i();
					if (index != -1) requiredSteps.add(tmpEntries.get(index));
				}

				int index = read.i();
				if (index != -1) currentStep = tmpEntries.get(index);
			}

			arm.startPos = TypeIO.readVec2(read);
			arm.targetPos = TypeIO.readVec2(read);
			arm.time = read.f();
		}

		@Override
		public boolean shouldConsume() {
			return
				getPlan() != null && super.shouldConsume() &&
				(!requiredSteps.isEmpty() || progressCounter == getPlan().steps.size - 1) &&
				!invalid && team.data().countType(getPlan().unit) < Units.getCap(team);
		}

		@Override public SpinModule spin() {
			return spin;
		}
		@Override public SpinConfig spinConfig() {
			return spinConfig;
		}

		@Override public float totalProgress() {
			return spinGraph().rotation / spinGraph().ratios.get(this, 1);
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
				if (progressCounter == getPlan().steps.size - 1) {
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
					progressCounter = 0;

					getPlan().steps.each((req, pos) -> requiredSteps.add(new ObjectMap.Entry<>() {{
						key = req;
						value = pos;
					}}));

					team.data().buildings.each(
						b -> b instanceof AssemblerArm.AssemblerArmBuild other && other.link == this,
						b -> {
							((AssemblerArm.AssemblerArmBuild) b).pick();
							if (((AssemblerArm.AssemblerArmBuild) b).currentStep != null) ((AssemblerArm.AssemblerArmBuild) b).arm.changePos(((AssemblerArm.AssemblerArmBuild) b).currentStep.value);
						}
					);
				} else {
					progressCounter++;
				}
				pick();
				if (currentStep != null) arm.changePos(currentStep.value);

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
					(getPlan().unit.naval && collisions.overlapsTile(rect, EntityCollisions::waterSolid)) ||
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

			write.i(progressCounter);

			if (getPlan() != null) {
				tmpEntries.clear();
				getPlan().steps.each((req, pos) -> tmpEntries.add(new ObjectMap.Entry<>() {{
					key = req;
					value = pos;
				}}));

				write.b(requiredSteps.size);
				for (ObjectMap.Entry<ItemStack[], Vec2> entry : requiredSteps) {
					write.i(tmpEntries.indexOf(val -> Arrays.equals(entry.key, val.key) && val.value.equals(entry.value)));
				}

				write.i(tmpEntries.indexOf(val -> Arrays.equals(currentStep.key, val.key) && val.value.equals(currentStep.value)));
			}

			TypeIO.writeVec2(write, arm.startPos);
			TypeIO.writeVec2(write, arm.targetPos);
			write.f(arm.time);
		}
	}
}
