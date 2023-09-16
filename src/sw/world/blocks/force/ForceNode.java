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
		public ForceConfig forceConfig() {return forceConfig;}

		@Override
		public void draw() {
			super.draw();
			drawBelt();
		}
		@Override
		public void drawConfigure() {
			drawOverlay(x, y, 0);
			SWDraw.square(Pal.accent, x, y, block.size * 6f, 0f);
			if (getLink() != null) SWDraw.square(Pal.place, getLink().x(), getLink().y(), getLink().block().size * 6f, 0f);
			Draw.reset();
		}

		@Override
		public void onProximityAdded() {
			super.onProximityAdded();
			force.graph.flood(this).each(b -> graph().add(b));
		}
		@Override
		public void onProximityRemoved() {
			super.onProximityRemoved();
			unLink();
			graph().softRemove(this);
		}

		@Override public boolean onConfigureBuildTapped(Building other) {
			return configureBuildTap(other);
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
