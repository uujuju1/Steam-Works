package sw.world.draw;

import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.*;
import mindustry.entities.part.*;
import mindustry.entities.part.DrawPart.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import sw.annotations.Annotations.*;
import sw.world.interfaces.*;

public class DrawParts extends BlockDrawer {
	public boolean vanillaOutlines = false;

	public String name = "";

	public Seq<DrawPart> parts = new Seq<>();

	public @Load("@loadBlock.name$@name$-preview") TextureRegion previewRegion;

	public static PartProgress
	warmup = p -> ((BlockParams) p).warmup,
	progress = p -> ((BlockParams) p).progress,
	totalProgress = p -> ((BlockParams) p).totalProgress,
	efficiency = p -> ((BlockParams) p).efficiency,
	spin = p -> ((BlockParams) p).spin,
	ratio = p -> ((BlockParams) p).ratio,
	speed = p -> ((BlockParams) p).speed;
	
	public static final BlockParams params = new BlockParams();
	
	@Override
	public void draw(Building build) {
		if(parts.size > 0) {
			BlockParams params = (BlockParams) DrawParts.params.set(
				0f, 0f, 0f, 0f, 0f, 0f,
				build.x, build.y, build.rotation * 90f + 90f
			);
			params.warmup = build.warmup();
			params.progress = build.progress();
			params.totalProgress = build.totalProgress();
			params.efficiency = build.efficiency;
			
			params.spin = build instanceof HasSpin spin && spin.spin() != null ? spin.getRotation() : 0f;
			params.ratio = build instanceof HasSpin spin && spin.spin() != null ? spin.getRatio() : 1f;
			params.speed = build instanceof HasSpin spin && spin.spin() != null ? spin.getSpeed() : 1f;

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
		super.load(block);
		
		parts.each(p -> p.load(block.name + name));
	}
	
	public static class BlockParams extends PartParams {
		public float warmup;
		public float progress;
		public float totalProgress;
		public float efficiency;
		
		public float spin;
		public float ratio;
		public float speed;
		
	}
}
