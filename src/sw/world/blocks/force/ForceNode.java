package sw.world.blocks.force;

import arc.graphics.g2d.*;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import sw.util.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class ForceNode extends Block {
	public ForceConfig forceConfig = new ForceConfig();

	public ForceNode(String name) {
		super(name);
		solid = destructible = true;
		sync = update = true;
	}

	@Override
	public void setStats() {
		super.setStats();
		forceConfig.addStats(stats);
	}

	@Override public void drawOverlay(float x, float y, int rotation) {
		if (forceConfig.outputsForce) Drawf.dashCircle(x, y, forceConfig.range, Pal.accent);
	}

	@Override
	public void init() {
		super.init();
		configurable = forceConfig.outputsForce;
	}

	public class ForceNodeBuild extends Building implements HasForce {
		ForceModule force = new ForceModule();

		public ForceModule force() {
			return force;
		}
		public ForceConfig fConfig() {return forceConfig;}

		@Override
		public void draw() {
			super.draw();
			drawBelt();
		}
		@Override
		public void drawConfigure() {
			drawOverlay(x, y, 0);
			SWDraw.square(Pal.accent, x, y, block.size * 6f, 0f);
			if (getForceLink() != null) SWDraw.square(Pal.place, getForceLink().x(), getForceLink().y(), getForceLink().block().size * 6f, 0f);
			Draw.reset();
		}

		@Override
		public void onProximityAdded() {
			super.onProximityAdded();
			fGraph().addBuild(this);
		}
		@Override
		public void onProximityRemoved() {
			super.onProximityRemoved();
			fGraph().removeBuild(this, false);
		}

		@Override public boolean onConfigureBuildTapped(Building other) {
			return configureForceLink(other);
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			force.read(read);
		}
		@Override
		public void write(Writes write) {
			super.write(write);
			force.write(write);
		}
	}
}
