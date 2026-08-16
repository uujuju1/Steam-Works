package sw.world.draw;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.world.draw.*;

public class DrawParticlesDirectional extends DrawParticles {
	public float rotation;
	public int seedOffset = 0;

	@Override
	public void draw(Building build){
		if(build.warmup() > 0f){
			float a = alpha * build.warmup();

			Draw.blend(blending);
			Draw.color(color);

			float base = Time.time / particleLife;
			rand.setSeed(build.id + seedOffset);
			for(int i = 0; i < particles; i++){
				float fin = (rand.random(2f) + base) % 1f;
				if(reverse) fin = 1f - fin;
				float fout = 1f - fin;
				float angle = build.rotdeg() + rotation;
				float len = particleRad * particleInterp.apply(fout);
				float offsetY = rand.range(particleRad);

				Draw.alpha(a * (1f - Mathf.curve(fin, 1f - fadeMargin)));
				if(poly){
					Fill.poly(
						build.x + x + Angles.trnsx(angle, -particleRad + len * 2f, offsetY),
						build.y + y + Angles.trnsy(angle, -particleRad + len * 2f, offsetY),
						sides,
						particleSize * particleSizeInterp.apply(fin) * build.warmup(),
						particleRotation
					);
				}else{
					Fill.circle(
						build.x + x + Angles.trnsx(angle, -particleRad + len * 2f, offsetY),
						build.y + y + Angles.trnsy(angle, -particleRad + len * 2f, offsetY),
						particleSize * particleSizeInterp.apply(fin) * build.warmup()
					);
				}
			}

			Draw.blend();
			Draw.reset();
		}
	}
}
