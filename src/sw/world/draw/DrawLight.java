package sw.world.draw;

import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.draw.*;

public class DrawLight extends DrawBlock {
	public float lightOpacity;

	public DrawLight(float lightOpacity) {
		this.lightOpacity = lightOpacity;
	}

	@Override
	public void drawLight(Building build) {
		Drawf.light(build.x, build.y, build.block.lightRadius * build.warmup(), build.block.lightColor, lightOpacity);
	}
}
