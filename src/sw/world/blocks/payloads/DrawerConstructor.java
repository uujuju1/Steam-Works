package sw.world.blocks.payloads;

import arc.audio.*;
import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.entities.units.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.draw.*;
import sw.world.graph.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

/**
 * Constructor with drawer compatibility and other things.
 */
public class DrawerConstructor extends Constructor {
	public SpinConfig spinConfig;

	public Sound buildSound = Sounds.none;

	public DrawBlock drawer = new DrawDefault();

	public DrawerConstructor(String name) {
		super(name);
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

	@Override
	public void getRegionsToOutline(Seq<TextureRegion> out) {
		drawer.getRegionsToOutline(this, out);
	}

	@Override
	public TextureRegion[] icons(){
		return drawer.finalIcons(this);
	}

	@Override
	public void init() {
		super.init();
		if (spinConfig != null) spinConfig.init(this);
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

	public class DrawerConstructorBuild extends ConstructorBuild implements HasSpin {
		public SpinModule spin;

		@Override
		public Building create(Block block, Team team) {
			if (spinConfig != null) spin = new SpinModule();
			return super.create(block, team);
		}

		@Override public void draw() {
			drawer.draw(this);

			Draw.z(Layer.blockOver);

			if (payload != null) {
				updatePayload();

				payload.draw();
			}
		}
		@Override public void drawLight() {
			drawer.drawLight(this);
		}
		@Override public void drawSelect() {
			super.drawSelect();
			if (spinConfig != null) spinConfig.drawPlace(block, tileX(), tileY(), rotation, true);
		}

		@Override
		public void moveOutPayload() {
			if (outputsPayload) super.moveOutPayload();
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

		@Override public float progress() {
			return recipe() == null ? 0f : progress / recipe().buildTime;
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);

			if (spinConfig != null) (spin == null ? new SpinModule() : spin).read(read);
		}

		@Override
		public Block recipe() {
			return filter.size == 1 ? filter.first() : super.recipe();
		}

		@Override public float totalProgress() {
			return time;
		}

		@Override
		public void updateTile() {
			if (payload == null && efficiency > 0 && recipe() != null && progress + buildSpeed * edelta() > recipe().buildTime) buildSound.at(this, 1f, 1f);

			super.updateTile();
		}

		@Override public float warmup() {
			return heat;
		}

		@Override
		public void write(Writes write) {
			super.write(write);

			if (spinConfig != null) spin.write(write);
		}
	}
}
