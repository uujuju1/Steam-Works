package sw.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import sw.util.*;
import sw.world.interfaces.*;

public class DrawTemperature extends DrawBlock {
	public TextureRegion heatRegion;
	public float lightRadius = 40f;

	@Override
	public void draw(Building build) {
		if (build instanceof HasHeat b) {
			Draw.color(SWDraw.heatPal);
			Draw.alpha(Mathf.clamp(b.fractionNeg()));
			Draw.rect(heatRegion, build.x, build.y, build.block.rotate ? build.rotdeg() : 0f);
		}
		Draw.reset();
	}

	@Override
	public void drawLight(Building build) {
		if (build instanceof HasHeat b) {
			Drawf.light(build.x, build.y, Mathf.maxZero(b.fractionNeg()) * lightRadius, SWDraw.heatPal, Mathf.clamp(b.fractionNeg()));
		}
	}

	@Override
	public void load(Block block) {
		heatRegion = Core.atlas.find(block.name + "-temperature");
	}
}
