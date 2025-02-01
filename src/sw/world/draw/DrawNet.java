package sw.world.draw;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.draw.*;
import sw.math.*;

public class DrawNet extends DrawBlock {
	public float layer = Layer.blockOver;

	public float radius = 10f;
	public float height = 8f;
	public float rotation = 45f;
	public float sides = 8f;

	public Color netColor = Color.white, coverColor = Color.white;

	protected final Seq<Vec2> basePoints = new Seq<>();

	@Override
	public void draw(Building build) {
		float z = Draw.z();
		Draw.z(layer);
		float x = build.x();
		float y = build.y();

		basePoints.clear();

		for(int i = 0; i < sides; i++) {
			basePoints.add(new Vec2().trns(360/sides * i + rotation, radius));
		}

		Fill.polyBegin();
		basePoints.each(p -> {
			Vec2 prev = basePoints.get(Mathf.mod(basePoints.indexOf(p) - 1, basePoints.size));
			Vec2 next = basePoints.get(Mathf.mod(basePoints.indexOf(p) + 1, basePoints.size));

			float x4 = Parallax.getParallaxFrom(next.x + x, Core.camera.position.x, height * build.warmup());
			float y4 = Parallax.getParallaxFrom(next.y + y, Core.camera.position.y, height * build.warmup());

			Draw.color(netColor);

			if (Math.abs(p.dst(prev) - p.dst(next)) < p.dst(prev)) {
				Fill.quad(
					Parallax.getParallaxFrom(p.x + x, Core.camera.position.x, height * build.warmup()),
					Parallax.getParallaxFrom(p.y + y, Core.camera.position.y, height * build.warmup()),
					p.x + x, p.y + y,
					next.x + x, next.y + y,
					x4, y4
				);
			}
			Fill.polyPoint(x4, y4);
		});

		Draw.color(coverColor);
		Fill.polyEnd();
		Draw.reset();
		Draw.z(z);
	}
}
