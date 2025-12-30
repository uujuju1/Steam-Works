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
import sw.entities.*;
import sw.graphics.*;

public class DrawAxles extends DrawBlock {
	public @Nullable String iconName;
	
	public Seq<Axle> axles = new Seq<>();

	public @Nullable Floatf<Building> rotationOverride;

	public float layer = -1;

	public DrawAxles(Axle... axles) {
		this.axles.add(axles);
	}

	public DrawAxles(Floatf<Building> rotationOverride, Axle... axles) {
		this.rotationOverride = rotationOverride;
		this.axles.add(axles);
	}

	public DrawAxles(Floatf<Building> rotationOverride, float layer, Axle... axles) {
		this.rotationOverride = rotationOverride;
		this.layer = layer;
		this.axles.add(axles);
	}

	@Override
	public void draw(Building build) {
		float z = Draw.z();
		if (layer > 0) Draw.z(layer);
		axles.each(axle -> {
			float rot = build.block.rotate ? ((build.rotdeg() + 90f + axle.rotation) % 180f - 90f) : axle.rotation;
			float spin = (rotationOverride != null ? rotationOverride.get(build) : build.totalProgress()) * axle.spinScl;
			float dx = build.x + Angles.trnsx(build.block.rotate ? build.rotdeg() : 0, axle.x, axle.y);
			float dy = build.y + Angles.trnsy(build.block.rotate ? build.rotdeg() : 0, axle.x, axle.y);

			Draws.palette(axle.paletteLight, axle.paletteMedium, axle.paletteDark);
			if (axle.hasSprites) {
				Draws.regionCylinder(axle.regions, dx, dy, axle.width, axle.height, spin, rot, axle.circular);
			} else {
				Draws.polyCylinder(axle.polySides, dx, dy, axle.width, axle.height, spin, rot, axle.circular);
			}
			Draws.palette();

			if (axle.circular) Draw.rect(axle.shadowRegion, dx, dy, axle.width, axle.height, rot);
		});
		Draw.reset();
		Draw.z(z);
	}
	@Override
	public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
		axles.each(axle -> axle.hasIcon, axle -> Draw.rect(axle.iconRegion, plan.drawx(), plan.drawy(), plan.rotation * 90));
	}

	@Override
	public TextureRegion[] icons(Block block) {
		if (iconName != null) {
			return new TextureRegion[]{Core.atlas.find(iconName)};
		}
		
		Seq<Axle> tmp = axles.select(b -> b.hasIcon);
		TextureRegion[] out = new TextureRegion[tmp.size];
		for(int i = 0; i < out.length; i++) out[i] = tmp.get(i).iconRegion;
		return out;
	}

	@Override
	public void load(Block block) {
		axles.each(axle -> {
			if (axle.hasSprites) axle.regions = Core.atlas.find(block.name + axle.suffix).split(axle.pixelWidth, axle.pixelHeight)[0];
			if (axle.hasIcon) axle.iconRegion = Core.atlas.find(axle.iconOverride == null ? block.name + axle.suffix + "-icon" : axle.iconOverride);
			if (axle.circular) axle.shadowRegion = Core.atlas.find(block.name + axle.suffix + "-shadow");
		});
	}
}
