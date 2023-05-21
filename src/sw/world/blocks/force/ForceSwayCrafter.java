package sw.world.blocks.force;

import arc.graphics.g2d.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.blocks.production.*;
import sw.util.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class ForceSwayCrafter extends GenericCrafter {
	public float outputSpeed;
	public float swayScl = 3f;
	public ForceConfig forceConfig = new ForceConfig();

	public ForceSwayCrafter(String name) {
		super(name);
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

	public class ForceSwayCrafterBuild extends GenericCrafterBuild implements HasForce {
		public float rotation = 0;
		ForceModule force = new ForceModule();

		@Override public ForceModule force() {
			return force;
		}
		@Override public ForceConfig forceConfig() {
			return forceConfig;
		}

		@Override
		public void updateTile() {
			super.updateTile();
			if (efficiency > 0) {
				rotation += Time.delta;
				force().speed = Tmp.v1.trns(rotation / swayScl, outputSpeed).x;
			}
		}

		@Override
		public void draw() {
			super.draw();
			drawBelt();
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
			force().links.each(graph().links::remove);
			unLinkGraph();
		}

		@Override public boolean onConfigureBuildTapped(Building other) {
			return configureBuildTap(other);
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			force.read(read);
			rotation = read.f();
			graph().floodFill(this).each(b -> graph().add(b));
		}
		@Override
		public void write(Writes write) {
			super.write(write);
			force.write(write);
			write.f(rotation);
		}
	}
}
