package sw.world.draw;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import sw.graphics.*;

public class DrawAxles extends DrawBlock {
	public Seq<Axle> axles = new Seq<>();

	public @Nullable Floatf<Building> rotationOverride;

	public DrawAxles(Axle... axles) {
		this.axles.add(axles);
	}

	public DrawAxles(Floatf<Building> rotationOverride, Axle... axles) {
		this.rotationOverride = rotationOverride;
		this.axles.add(axles);
	}

	@Override
	public void draw(Building build) {
		axles.each(axle -> {
			float rot = build.block.rotate ? ((build.rotdeg() + 90f + axle.rotation) % 180f - 90f) : axle.rotation;
			float spin = (rotationOverride != null ? rotationOverride.get(build) : build.totalProgress()) * axle.spinScl;
			float dx = build.x + Angles.trnsx(build.block.rotate ? build.rotdeg() : 0, axle.x, axle.y);
			float dy = build.y + Angles.trnsy(build.block.rotate ? build.rotdeg() : 0, axle.x, axle.y);
			Draws.palette(axle.paletteLight, axle.paletteMedium, axle.paletteDark);
			if (axle.hasSprites) {
				Draws.regionCylinder(axle.regions, dx, dy, axle.width, axle.height, spin, rot);
			} else {
				Draws.polyCylinder(axle.polySides, dx, dy, axle.width, axle.height, spin, rot);
			}
			Draws.palette();
		});
		Draw.reset();
	}
	@Override
	public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
		axles.each(axle -> Draw.rect(axle.iconRegion, plan.drawx(), plan.drawy(), plan.rotation * 90));
	}

	@Override
	public TextureRegion[] icons(Block block) {
		Seq<Axle> tmp = axles.select(b -> b.hasIcon);
		TextureRegion[] out = new TextureRegion[tmp.size];
		for(int i = 0; i < out.length; i++) out[i] = tmp.get(i).iconRegion;
		return out;
	}

	@Override
	public void load(Block block) {
		axles.each(axle -> {
			axle.regions = Core.atlas.find(block.name + axle.suffix).split(axle.pixelWidth, axle.pixelHeight)[0];
			axle.iconRegion = Core.atlas.find(axle.iconOverride == null ? block.name + axle.suffix + "-icon" : axle.iconOverride);
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

		public int polySides = 1;

		public boolean hasSprites = true;
		public boolean hasIcon = true;

		public TextureRegion[] regions;
		public TextureRegion iconRegion;

		public Color paletteLight = Color.clear;
		public Color paletteMedium = Color.clear;
		public Color paletteDark = Color.clear;

		public Axle(String suffix) {
			this.suffix = suffix;
		}
	}
}
