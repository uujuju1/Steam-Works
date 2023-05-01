package sw.world.blocks.production;

import arc.*;
import arc.graphics.g2d.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.blocks.production.*;
import mindustry.world.meta.*;
import sw.*;
import sw.util.*;
import sw.world.graph.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class SWGenericCrafter extends GenericCrafter implements HeatBlockI {
	public ForceConfig forceConfig = new ForceConfig();
	HeatConfig heatConfig = SWVars.baseConfig.copy();

	public float outputSpeed = 0f, outputForce = 0f, outputHeat = -1f;

/**
 * makes so that it can spin continuously for a certain amount before stopping
 */
	public boolean clampRotation = false;
	public float maxRotation = 120f;

	public SWGenericCrafter(String name) {
		super(name);
		configurable = true;
	}

	@Override public void drawOverlay(float x, float y, int rotation) {
		Drawf.dashCircle(x, y, forceConfig.range, Pal.accent);
	}
	@Override public HeatConfig heatConfig() {return heatConfig;}

	@Override
	public void setStats() {
		super.setStats();
		heatStats(stats);
		forceConfig.addStats(stats);
		if (outputHeat >= 0) stats.add(SWStat.outputHeat, outputHeat, StatUnit.degrees);
	}
	@Override
	public void setBars() {
		super.setBars();
		addBar("heat", (SWGenericCrafterBuild entity) -> new Bar(Core.bundle.get("bar.heat"), Pal.accent, entity::fraction));
	}

	public class SWGenericCrafterBuild extends GenericCrafterBuild implements HasHeat, HasForce {
		public float rotation = 0;
		HeatModule heat = new HeatModule();
		ForceModule force = new ForceModule();

		@Override public HeatModule heat() {
			return heat;
		}
		@Override public HeatBlockI type() {
			return (HeatBlockI) block;
		}

		@Override public ForceModule force() {
			return force;
		}
		@Override public ForceConfig forceConfig() {
			return forceConfig;
		}

		@Override
		public void updateTile() {
			if (efficiency > 0 && outputHeat >= 0) heat().addHeat(outputHeat * efficiencyScale() * Time.delta);
			rotation += speed() * Time.delta;
			if (Math.abs(rotation) > maxRotation) {
				rotation = (maxRotation + 1f) * (rotation > 0 ? 1f : -1f);
				efficiency = 0f;
			}
			super.updateTile();
			updateHeat(this);
		}

		@Override
		public void craft() {
			super.craft();
			force().speed = outputSpeed;
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
			heat.read(read);
			rotation = read.f();
			graph().floodFill(this).each(b -> graph().add(b));
		}
		@Override
		public void write(Writes write) {
			super.write(write);
			force.write(write);
			heat.write(write);
			write.f(rotation);
		}
	}
}
