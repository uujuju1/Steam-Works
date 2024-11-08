package sw.world.draw;

import arc.*;
import arc.func.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import sw.util.*;

public class DrawAxles extends DrawBlock {
	public Seq<Axle> axles = new Seq<>();

	public @Nullable Floatf<Building> rotationOverride;

	public DrawAxles(Axle... axles) {
		this.axles.add(axles);
	}

	@Override
	public void draw(Building build) {
		axles.each(axle -> {
			float rot = build.block.rotate ? ((build.rotdeg() + 90f + axle.rotation) % 180f - 90f) : axle.rotation;
			float spin = (rotationOverride != null ? rotationOverride.get(build) : build.totalProgress()) * axle.spinScl;
			float dx = build.x + Angles.trnsx(build.block.rotate ? build.rotdeg() : 0, axle.x, axle.y);
			float dy = build.y + Angles.trnsy(build.block.rotate ? build.rotdeg() : 0, axle.x, axle.y);
			SWDraw.rotatingRects(axle.regions, dx, dy, axle.width, axle.height, rot, spin);
			Draw.rect(axle.shadowRegion, dx, dy, rot);
		});
	}
	@Override
	public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
		axles.each(axle -> {
			float rot = block.rotate ? ((plan.rotation * 90 + 90f + axle.rotation) % 180f - 90f) : 0;
			float dx = plan.drawx() + Angles.trnsx(plan.rotation * 90f, axle.x, axle.y);
			float dy = plan.drawy() + Angles.trnsy(plan.rotation * 90f, axle.x, axle.y);
			SWDraw.rotatingRects(axle.regions, dx, dy, axle.width, axle.height, rot, 0);
			Draw.rect(axle.shadowRegion, dx, dy, rot);
		});
	}

	@Override
	public TextureRegion[] icons(Block block) {
		TextureRegion[] out = new TextureRegion[axles.size];
		for(int i = 0; i < out.length; i++) out[i] = axles.get(i).iconRegion;
		return out;
	}

	@Override
	public void load(Block block) {
		axles.each(axle -> {
			axle.regions = Core.atlas.find(block.name + axle.suffix).split(axle.pixelWidth, axle.pixelHeight)[0];
			axle.shadowRegion = Core.atlas.find(block.name + axle.suffix + "-shadow");
			if (axle.iconOverride == null) {
				axle.iconRegion = Core.atlas.find(block.name + axle.suffix + "-icon");
			} else {
				axle.iconRegion = Core.atlas.find(axle.iconOverride);
			}
		});
	}

	public static class Axle {
		public String suffix;
		public @Nullable String iconOverride;

		public float x, y, rotation;

		public float width = 8f, height = 8f;

		public float spinScl = 1;

		public int pixelWidth = 32;
		public int pixelHeight = 32;

		public TextureRegion[] regions;
		public TextureRegion shadowRegion, iconRegion;

		public Axle(String suffix) {
			this.suffix = suffix;
		}
	}
}
