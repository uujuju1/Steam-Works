package sw.world.draw;

import arc.graphics.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class DrawLight extends DrawBlock {
	public Color lightColor;
	public float lightRadius;

	public DrawLight(Color lightColor, float lightRadius) {
		this.lightColor = lightColor;
		this.lightRadius = lightRadius;
	}

	@Override
	public void drawLight(Building build) {
		Drawf.light(build.x, build.y, build.block.lightRadius * build.warmup(), build.block.lightColor, build.block.lightColor.a);
	}

	@Override
	public void load(Block block) {
		super.load(block);

		block.emitLight = true;
		block.lightColor = lightColor;
		block.lightRadius = lightRadius;
		block.updateClipRadius(lightRadius);
	}
}
