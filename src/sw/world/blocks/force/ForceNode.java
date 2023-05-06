package sw.world.blocks.force;

import arc.graphics.g2d.*;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import sw.util.*;
import sw.world.graph.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class ForceNode extends Block {
	public ForceConfig forceConfig = new ForceConfig();

	public ForceNode(String name) {
		super(name);
		solid = destructible = true;
		sync = update = true;
		configurable = true;
	}

	@Override public void drawOverlay(float x, float y, int rotation) {
		Drawf.dashCircle(x, y, forceConfig.range, Pal.accent);
	}

	@Override
	public void setStats() {
		super.setStats();
		forceConfig.addStats(stats);
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
			drawBelt(this);
		}
		@Override
		public void drawConfigure() {
			drawOverlay(x, y, 0);
			SWDraw.square(Pal.accent, x, y, block.size * 6f, 0f);
			if (getLink() != null) SWDraw.square(Pal.place, getLink().x, getLink().y, getLink().block.size * 6f, 0f);
			Draw.reset();
		}

		@Override
		public void onProximityAdded() {
			super.onProximityAdded();
			force.graph.floodFill(this).each(b -> graph().add(b));
		}
		@Override
		public void onProximityRemoved() {
			super.onProximityRemoved();
			force().links.each(l -> {
				l.removeS();
			});
		}

		@Override
		public boolean onConfigureBuildTapped(Building other) {
			if (other instanceof HasForce next && tile.dst(other) < forceConfig().range) {
				if ((getLink() != null && getLink() == other) || other == this) {
					new Link(this, next).removeS();
					force().link = -1;
					return false;
				}

				if (next.force().link == pos()) {
					new Link(this, next).removeS();
					next.force().link = -1;
				}

				force().link = other.pos();
				new Link(this, next);
				graph().addGraph(((HasForce) getLink()).graph());
				graph().rotation = 0;
				return false;
			}
			return true;
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			force.read(read);
			graph().floodFill(this).each(b -> graph().add(b));
		}
		@Override
		public void write(Writes write) {
			super.write(write);
			force.write(write);
		}
	}
}
