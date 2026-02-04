package sw.world.blocks.power;

import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.entities.units.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import sw.world.graph.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class AxleBlock extends Block {
	public SpinConfig spinConfig;

	public DrawBlock drawer = new DrawDefault();

	public AxleBlock(String name) {
		super(name);
		update = true;
		rotate = true;
		drawArrow = false;
		group = BlockGroup.power;
	}

	@Override
	public boolean canReplace(Block other) {
		return super.canReplace(other) || other instanceof AxleBlock;
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

	public class AxleBlockBuild extends Building implements HasSpin {
		public SpinModule spin;
		
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
			if (spinConfig != null) spinConfig.drawPlace(block, tileX(), tileY(), rotation, true);
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
		public void read(Reads read, byte revision) {
			super.read(read, revision);

			if (spinConfig != null) (spin == null ? new SpinModule() : spin).read(read);
		}

		@Override public float totalProgress() {
			return getRotation();
		}
		
		@Override
		public void write(Writes write) {
			super.write(write);

			if (spinConfig != null) spin.write(write);
		}
	}
}
