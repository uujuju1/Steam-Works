package sw.world.blocks.force;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.blocks.production.*;
import sw.util.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class ForceDrill extends Drill {
	public ForceConfig forceConfig = new ForceConfig();

	public float rotatorOffset = 0f;

	public ForceDrill(String name) {
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

	public class ForceDrillBuild extends DrillBuild implements HasForce {
		ForceModule force = new ForceModule();

		public ForceModule force() {
			return force;
		}
		public ForceConfig forceConfig() {return forceConfig;}

		@Override
		public void draw() {
			float s = 0.3f;
			float ts = 0.6f;

			Draw.rect(region, x, y);
			Draw.z(Layer.blockCracks);
			drawDefaultCracks();

			Draw.z(Layer.blockAfterCracks);
			if(drawRim){
				Draw.color(heatColor);
				Draw.alpha(warmup * ts * (1f - s + Mathf.absin(Time.time, 3f, s)));
				Draw.blend(Blending.additive);
				Draw.rect(rimRegion, x, y);
				Draw.blend();
				Draw.color();
			}

			Tmp.v1.trns(timeDrilled * -rotateSpeed/2f, rotatorOffset);
			if(drawSpinSprite){
				Drawf.spinSprite(rotatorRegion, Tmp.v1.x + x, Tmp.v1.y + y, timeDrilled * rotateSpeed);
				Drawf.spinSprite(rotatorRegion, -Tmp.v1.x + x, -Tmp.v1.y + y, timeDrilled * rotateSpeed);
			}else{
				Draw.rect(rotatorRegion, Tmp.v1.x + x, Tmp.v1.y + y, timeDrilled * rotateSpeed);
				Draw.rect(rotatorRegion, -Tmp.v1.x + x, -Tmp.v1.y + y, timeDrilled * rotateSpeed);
			}

			Draw.rect(topRegion, x, y);

			if(dominantItem != null && drawMineItem){
				Draw.color(dominantItem.color);
				Draw.rect(itemRegion, x, y);
				Draw.color();
			}
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
			force.graph.flood(this).each(b -> graph().add(b));
		}
		@Override
		public void onProximityRemoved() {
			super.onProximityRemoved();
			forceUnLink();
			graph().softRemove(this);
			graph().links.removeAll(force().links);
		}

		@Override public boolean onConfigureBuildTapped(Building other) {
			return configureForceLink(other);
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			force.read(read);
			graph().flood(this).each(build -> {
				graph().merge(build.graph());
			});
		}
		@Override
		public void write(Writes write) {
			super.write(write);
			force.write(write);
		}
	}
}
