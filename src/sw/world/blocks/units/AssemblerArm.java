package sw.world.blocks.units;

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
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.io.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.consumers.*;
import mindustry.world.draw.*;
import sw.entities.*;
import sw.world.graph.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

import static mindustry.Vars.*;
import static mindustry.type.ItemStack.*;

public class AssemblerArm extends Block {
	private static final Rect tmp = new Rect();

	public SpinConfig spinConfig;

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

	public float armStartingOffset = 0;

	public DrawBlock drawer = new DrawDefault();

	public float armSpeed = 1;

	public AssemblerArm(String name) {
		super(name);
		solid = true;
		destructible = true;
		update = true;
		sync = true;
		rotate = true;

		consume(new ConsumeItemDynamic(AssemblerArmBuild::getRecipe));
	}

	@Override
	public boolean canPlaceOn(Tile tile, Team team, int rotation) {
		return getLink(tile, team, rotation) != null;
	}

	@Override
	public void drawPlace(int x, int y, int rotation, boolean valid) {
		super.drawPlace(x, y, rotation, valid);

		Vars.player.team().data().buildings.each(b -> {
			if (b.block instanceof MechanicalAssembler a) Drawf.dashRect(Pal.heal, a.getRect(Tmp.r2, b.x, b.y, b.rotation));
		});

		Drawf.dashRect(valid ? Pal.accent : Pal.remove, getRect(Tmp.r1, x * tilesize + offset, y * tilesize + offset, rotation));
	}

	@Override
	public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
		if (spinConfig != null) spinConfig.drawPlace(this, plan.x, plan.y, plan.rotation, true);
		drawer.drawPlan(this, plan, list);
	}

	@Override
	public void drawPlanConfigTop(BuildPlan plan, Eachable<BuildPlan> list) {
		drawer.drawPlan(this, plan, list);
	}

	public @Nullable MechanicalAssembler.MechanicalAssemblerBuild getLink(Tile tile, Team team, int rotation) {
		Rect rect = getRect(Tmp.r1, tile.worldx() + offset, tile.worldy() + offset, rotation);
		Building out = team.data().buildings.find(
			b -> b.block instanceof MechanicalAssembler a && a.getRect(Tmp.r2, b.x, b.y, b.rotation).equals(rect)
		);

		return out == null ? null : out.as();
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
	public void load() {
		super.load();
		drawer.load(this);
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
	}

	public class AssemblerArmBuild extends Building implements HasSpin, HasArm {
		public SpinModule spin;

		public Arm arm = new Arm().reset(rotdeg(), armStartingOffset);

		public MechanicalAssembler.MechanicalAssemblerBuild link;

		public ObjectMap.Entry<ItemStack[], Vec2> currentStep;

		public float warmup, progress;

		@Override
		public boolean acceptItem(Building source, Item item) {
			return
				link != null && link.getPlan() != null && currentStep != null &&
				Structs.contains(currentStep.key, stack -> stack.item == item) &&
				items.get(item) < Structs.find(currentStep.key, stack -> stack.item == item).amount * 2;
		}

		@Override public Arm arm() {
			return arm;
		}
		
		@Override
		public Building create(Block block, Team team) {
			if (spinConfig != null) spin = new SpinModule();
			return super.create(block, team);
		}

		@Override public void draw() {
			drawer.draw(this);
		}
		@Override public void drawLight() {
			drawer.drawLight(this);
		}
		@Override public void drawSelect() {
			if (spin != null) spinConfig.drawPlace(block, tileX(), tileY(), rotation, true);
		}

		@Override
		public float getArmTime() {
			return arm.startPos.dst(arm.targetPos) / armSpeed;
		}

		public ItemStack[] getRecipe() {
//			f (link == null || link.getPlan() == null) return empty;
//			for (int i = 0; i < link.getPlan().pos.length; i++) {
//				if (link.getPlan().pos[i].equals(arm.targetPos)) return link.getPlan().requirements[i];
//			}i
			return empty;
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

			if (link != null && currentStep != null) link.requiredSteps.add(currentStep);
		}

		public void pick() {
			if (
				link != null && link.getPlan() != null &&
				link.requiredSteps.size > 1
			) {
				currentStep = link.requiredSteps.pop();
			} else {
				currentStep = null;
			}
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);

			if (spinConfig != null) (spin == null ? new SpinModule() : spin).read(read);

			warmup = read.f();
			progress = read.f();

			boolean hasStep = read.bool();
			if (hasStep) {
				int len = read.i();
				ItemStack[] stack = new ItemStack[len];
				for(int i = 0; i < len; i++) {
					stack[i] =  TypeIO.readItems(read);
				}

				currentStep = new ObjectMap.Entry<>() {{
					key = stack;
					value = TypeIO.readVec2(read);
				}};
			}

			arm.startPos = TypeIO.readVec2(read);
			arm.targetPos = TypeIO.readVec2(read);
			arm.time = read.f();
		}

		@Override
		public boolean shouldConsume() {
			if (link == null || link.getPlan() == null) return false;

			return
				super.shouldConsume() &&
				currentStep != null &&
				!link.invalid && team.data().countType(link.getPlan().unit) < Units.getCap(team);
		}

		@Override
		public float totalProgress() {
			return spinGraph().rotation / spinGraph().ratios.get(this, 1);
		}

		@Override
		public void updateTile() {
			Rect rect = getRect(tmp, x, y, rotation);

			if (link == null || !link.isValid()) {
				link = getLink(tile, team, rotation);

				if (link != null && currentStep == null) pick();
				return;
			}

			if (efficiency > 0) {
				warmup = Mathf.approachDelta(warmup, 1f, warmupSpeed);

				arm.time += edelta();

				if (arm.time > getArmTime() && link.getPlan() != null) {
					progress += getProgressIncrease(link.getPlan().stepTime);

					Tmp.v1.set(arm.targetPos).add(rect.x + rect.width/2f, rect.y + rect.height/2f);
					Vars.control.sound.loop(progressSound, Tmp.v1, progressSoundVolume * warmup);
					if (Mathf.chanceDelta(progressEffectChance)) progressEffect.at(Tmp.v1);
				}
			} else {
				warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
			}

			if (link.getPlan() != null && progress >= 1f) {
				link.progressCounter++;
				currentStep = null;
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
		}

		@Override
		public void write(Writes write) {
			super.write(write);

			if (spinConfig != null) spin.write(write);

			write.f(warmup);
			write.f(progress);

			write.bool(currentStep != null);
			if (currentStep != null) {
				write.i(currentStep.key.length);
				for(ItemStack stack : currentStep.key) {
					TypeIO.writeItems(write, stack);
				}

				TypeIO.writeVec2(write, currentStep.value);
			}

			TypeIO.writeVec2(write, arm.startPos);
			TypeIO.writeVec2(write, arm.targetPos);
			write.f(arm.time);
		}
	}
}
