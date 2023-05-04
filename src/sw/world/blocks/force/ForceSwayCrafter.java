package sw.world.blocks.force;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.blocks.production.*;
import sw.util.*;
import sw.world.graph.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class ForceSwayCrafter extends GenericCrafter {
	public float outputSpeed, outputForce;
	public float swayScl = 3f;
	public ForceConfig forceConfig = new ForceConfig();

	public ForceSwayCrafter(String name) {
		super(name);
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
		public void craft() {
			super.craft();
			rotation += Time.delta;
			force().speed = Mathf.sinDeg(rotation/swayScl) * outputSpeed;
			force().strength = outputForce;
		}

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
			force().graph.removeBuild(this);
		}

		@Override
		public boolean onConfigureBuildTapped(Building other) {
			if (other instanceof HasForce next && tile.dst(other) < forceConfig().range) {
				if ((other == this || getLink() == other) && getLink() != null) {
					graph().removeBuild(other);
					graph().remove(other);
					graph().links.remove(new Link(this, other).removeS());
					force().link = -1;
					return false;
				}

				if (next.force().link == pos()) {
					graph().links.remove(new Link(this, other).removeS());
					next.force().link = -1;
					graph().removeBuild(other);
					graph().remove(other);
				}

				force().link = other.pos();
				graph().links.addUnique(new Link(this, other).addS());
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
