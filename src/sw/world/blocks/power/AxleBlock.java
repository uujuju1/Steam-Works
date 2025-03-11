package sw.world.blocks.power;

import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import sw.world.graph.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class AxleBlock extends Block {
	public SpinConfig spinConfig = new SpinConfig();

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
	public void drawPlace(int x, int y, int rotation, boolean valid) {
		super.drawPlace(x, y, rotation, valid);
		spinConfig.drawPlace(this, x, y, rotation, valid);
	}

	@Override
	public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
		drawer.drawPlan(this, plan, list);
	}

	@Override
	public void getRegionsToOutline(Seq<TextureRegion> out){
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
		spinConfig.addBars(this);
	}

	@Override
	public void setStats() {
		super.setStats();
		spinConfig.addStats(stats);
	}

	public class AxleBlockBuild extends Building implements HasSpin {
		public SpinModule spin = new SpinModule();

		@Override public void draw() {
			drawer.draw(this);
		}
		@Override public void drawLight() {
			drawer.drawLight(this);
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
		public boolean outputsSpin() {
			return true;
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			spin.read(read);
		}

		@Override public SpinModule spin() {
			return spin;
		}
		@Override public SpinConfig spinConfig() {
			return spinConfig;
		}

		@Override public float totalProgress() {
			return getRotation();
		}

		@Override
		public void write(Writes write) {
			super.write(write);
			spin.write(write);
		}
	}
}
