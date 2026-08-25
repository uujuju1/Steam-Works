package sw.world.draw;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class DrawParticlesDirectional extends DrawParticles {
	public int seedOffset = 0;

	public float rotation;

	public float particleRadY = -1;

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
				float offsetY = rand.range(particleRadY);

				Draw.alpha(a * (1f - Mathf.curve(fin, 1f - fadeMargin)));
				if(poly){
					Fill.poly(
						build.x + Angles.trnsx(angle, x - particleRad + len * 2f, y + offsetY),
						build.y + Angles.trnsy(angle, x - particleRad + len * 2f, y + offsetY),
						sides,
						particleSize * particleSizeInterp.apply(fin) * build.warmup(),
						particleRotation
					);
				}else{
					Fill.circle(
						build.x + Angles.trnsx(angle, x - particleRad + len * 2f, y + offsetY),
						build.y + Angles.trnsy(angle, x - particleRad + len * 2f, y + offsetY),
						particleSize * particleSizeInterp.apply(fin) * build.warmup()
					);
				}
			}

			Draw.blend();
			Draw.reset();
		}
	}

	@Override
	public void load(Block block) {
		if (particleRadY < 0) particleRadY = particleRad;
	}
}
