package sw.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.*;
import mindustry.entities.part.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import sw.world.interfaces.*;

public class DrawParts extends DrawBlock {
	public boolean vanillaOutlines = false;

	public String name = "";

	public Seq<DrawPart> parts = new Seq<>();

	public TextureRegion previewRegion;

	@Override
	public void draw(Building build) {
		if(parts.size > 0) {

			/*
			* warmup = warmup
			* reload = progress
			* smoothReload = totalProgress
			* heat = efficiency
			* recoil = graph rotation (spin exclusive)
			* charge = between 0 and 1 based on graph speed (spin exclusive)
			*/
			DrawPart.PartParams params = DrawPart.params.set(
				build.warmup(),
				build.progress(),
				build.totalProgress(),
				build.efficiency * build.efficiencyScale(),
				build instanceof HasSpin spin ? (spin.spinGraph().rotation * spin.spinGraph().ratios.get(spin, 1f)) : 0f,
				build instanceof HasSpin spin ? (spin.spinGraph().speed / spin.spinGraph().builds.max(HasSpin::getTargetSpeed).getTargetSpeed()) : 0f,
				build.x,
				build.y,
				build.rotation * 90f + 90f
			);

			parts.each(part -> part.draw(params));
		}
	}

	@Override
	public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
		if (previewRegion.found()) Draw.rect(previewRegion, plan.drawx(), plan.drawy(), plan.rotation * 90f);
	}

	@Override
	public void getRegionsToOutline(Block block, Seq<TextureRegion> out) {
		if (vanillaOutlines) parts.each(p -> p.getOutlines(out));
	}

	@Override
	public TextureRegion[] icons(Block block) {
		if (previewRegion.found()) return new TextureRegion[]{previewRegion};
		return new TextureRegion[]{};
	}

	@Override
	public void load(Block block) {
		parts.each(p -> p.load(block.name + name));
		previewRegion = Core.atlas.find(block.name + name + "-preview");
	}
}
