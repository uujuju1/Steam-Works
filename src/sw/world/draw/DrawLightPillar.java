package sw.world.draw;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import sw.math.*;

public class DrawLightPillar extends DrawBlock {
	public float layer = -1;
	public Blending blending = Blending.normal;
	public Color color = Color.white;
	public float radius = 1f, radiusTo = -1;
	public float alpha = 1f;
	public float x, y;
	public float height;
	public float heightWeaveScl = 1;
	public float heightWeaveMag = 0f;
	public int divisions = 1;
	public Interp warmupCurve = Interp.smooth;

	@Override
	public void draw(Building build) {
		float z = Draw.z();
		if (layer > 0) Draw.z(layer);
		Draw.blend(blending);
		Draw.color(color);
		Draw.alpha(warmupCurve.apply(build.warmup()) * alpha);
		float height = this.height + Mathf.sin(heightWeaveScl, heightWeaveMag);
		for (int i = 0; i < divisions; i++) {
			Parallax.getParallaxFrom(Tmp.v1.set(build).add(x, y), Core.camera.position, height / divisions * i * warmupCurve.apply(build.warmup()));

			Fill.circle(Tmp.v1.x, Tmp.v1.y, Mathf.lerp(radius, radiusTo, i / (divisions - 1f)));
		}
		Draw.blend();
		Draw.z(z);
	}

	@Override
	public void load(Block block) {
		if (radiusTo < 0) radiusTo = radius;
	}
}
