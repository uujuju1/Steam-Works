package sw.world.draw;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.draw.*;

public class DrawFire extends DrawBlock {
	public int lineParticles = 20;
	public float
		lineLength = 5f,
		lineLife = 15f,
		lineAlpha = 0.6f;
	public Color
		lineColor1 = Pal.accent,
		lineColor2 = Pal.turretHeat;

	public int circleParticles = 20;
	public float
		circleRadius = 5f,
		circleLife = 60f,
		circleAlpha = 0.6f;
	public Color
		circleColor1 = Pal.turretHeat,
		circleColor2 = Pal.accent;

	public float particleRadius = 10f;
	public Blending blend = Blending.additive;

	@Override
	public void draw(Building build) {
		if (build.warmup() <= 0) return;

		Draw.blend(blend);

		float base = (Time.time / lineLife);
		rand.setSeed(build.id);
		for(int i = 0; i < lineParticles; i++){
			float
				fin = (rand.random(1f) + base) % 1f, fout = 1f - fin,
				angle = rand.random(360f),
				len = ((particleRadius * 2) * Interp.pow2Out.apply(fin)) - particleRadius;
			Draw.color(lineColor1, lineColor2, fin);
			Draw.alpha(lineAlpha);
			Lines.lineAngle(
				build.x + Angles.trnsx(angle, len),
				build.y + Angles.trnsy(angle, len),
				angle,
				lineLength * fout * build.warmup()
			);
		}
		base = (Time.time / circleLife);
		rand.setSeed(build.id * 2L);

		for(int i = 0; i < circleParticles; i++){
			float
				fin = (rand.random(1f) + base) % 1f, fout = 1f - fin,
				angle = rand.random(360f),
				len = ((particleRadius * 2) * Interp.pow2Out.apply(fin)) - particleRadius;
			Draw.color(circleColor1, circleColor2, fin);
			Draw.alpha(circleAlpha);
			Fill.circle(
				build.x + Angles.trnsx(angle, len),
				build.y + Angles.trnsy(angle, len),
				circleRadius * Interp.sine.apply(fout * 2f) * build.warmup()
			);
		}

		Draw.blend();
		Draw.reset();
	}
}
