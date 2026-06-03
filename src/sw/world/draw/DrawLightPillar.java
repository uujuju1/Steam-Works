package sw.world.draw;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.world.draw.*;
import sw.math.*;

public class DrawLightPillar extends DrawBlock {
	public float layer = -1;
	public Blending blending = Blending.normal;
	public Color color = Color.white;
	public float radius = 1f;
	public float alpha = 1f;
	public float x, y;
	public float height;
	public int divisions;

	@Override
	public void draw(Building build) {
		float z = Draw.z();
		if (layer > 0) Draw.z(layer);
		Draw.blend(blending);
		Draw.color(color);
		Draw.alpha(build.warmup() * alpha);
		for (int i = 0; i < divisions; i++) {
			Parallax.getParallaxFrom(Tmp.v1.set(build).add(x, y), Core.camera.position, height / divisions * i * build.warmup());

			Fill.circle(Tmp.v1.x, Tmp.v1.y, radius);
		}
		Draw.blend();
		Draw.z(z);
	}
}
