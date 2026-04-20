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

			axle.draw(dx, dy, rot, spin);
		});
		Draw.reset();
		Draw.z(z);
	}
	@Override
	public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
		axles.each(axle -> {
			float rot = block.rotate ? ((plan.rotation * 90f + 90f + axle.rotation) % 180f - 90f) : axle.rotation;
			float spin = Time.time * axle.spinScl / 2f;
			float dx = plan.drawx() + Angles.trnsx(block.rotate ? plan.rotation * 90f : 0f, axle.x, axle.y);
			float dy = plan.drawy() + Angles.trnsy(block.rotate ? plan.rotation * 90f : 0f, axle.x, axle.y);

			axle.draw(dx, dy, rot, spin);
		});
	}

	@Override
	public TextureRegion[] icons(Block block) {
		return new TextureRegion[]{Core.atlas.find(iconName)};
	}

	@Override
	public void load(Block block) {
		if (iconName == null) iconName = block.name + "-axle-icon";
		axles.each(axle -> axle.load(block.name));
	}
}
