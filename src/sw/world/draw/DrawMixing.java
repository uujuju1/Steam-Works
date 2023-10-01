package sw.world.draw;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.world.draw.*;

public class DrawMixing extends DrawBlock {
	public float
	particleStroke = 1f,
	particleRad = 1f,
	particleLength = 2f,
	particleLife = 60f,

	circleStroke = 1f,
	circleRad = 1f,
	circleMag = 1f,
	circleScl = 10f,

	alpha = 1f;

	public int particles = 10;

	public short idOffset = 0;

	public Color
	particleColor = Color.white,
	circleColor = Color.white;

	public Blending blend = Blending.normal;

	@Override
	public void draw(Building build) {
		Draw.blend(blend);
		float si = Mathf.absin(circleScl, circleMag);
		Lines.stroke(circleStroke * build.warmup(), circleColor);
		Draw.alpha(alpha * build.warmup());
		Lines.circle(build.x, build.y, (circleRad + si) * build.warmup());

		float base = (Time.time / particleLife);
		rand.setSeed(build.id + idOffset);

		Lines.stroke(particleStroke * build.warmup() * build.warmup(), particleColor);
		Draw.alpha(alpha * build.warmup());
		for(int i = 0; i < particles; i++){
			float
				fin = (rand.random(1f) + base) % 1f, fout = 1f - fin,
				angle = rand.random(360f),
				len = 2 * (particleRad * Interp.sine.apply(fout)) - particleRad;
			Lines.lineAngle(
				build.x + Angles.trnsx(angle, len),
				build.y + Angles.trnsy(angle, len),
				angle,
				particleLength * Interp.sine.apply(fin * 2f)
			);
		}
		Draw.blend();
		Draw.reset();
	}
}
