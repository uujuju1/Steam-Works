package sw.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.world.draw.*;
import sw.math.*;

public class DrawParallaxParticles extends DrawParticles {
	public float particleHeightMin = 0f;
	public float particleHeightMax = 0f;
	public Interp particleHeightInterp = Interp.smooth;
	public boolean setMin;
	
	public float layer = -1;
	
	@Override
	public void draw(Building build){
		float z = Draw.z();
		Draw.z(layer);
		if(build.warmup() > 0f){
			float a = alpha * build.warmup();
			
			Draw.blend(blending);
			Draw.color(color);
			
			float base = Time.time / particleLife;
			rand.setSeed(build.id);
			for(int i = 0; i < particles; i++){
				float fin = (rand.random(2f) + base) % 1f;
				if(reverse) fin = 1f - fin;
				float fout = 1f - fin;
				float angle = rand.random(360f) + (Time.time / rotateScl) % 360f;
				float len = particleRad * particleInterp.apply(fout);
				
				float baseHeight = !setMin ? particleHeightMin : rand.random(particleHeightMin, particleHeightMax);
				float topHeight = rand.random(baseHeight, particleHeightMax);
				
				Parallax.getParallaxFrom(
					Tmp.v1.trns(angle, len).add(build).add(x, y), Core.camera.position,
					Mathf.map(particleHeightInterp.apply(fin), 0, 1, baseHeight, topHeight)
				);
				
				Draw.alpha(a * (1f - Mathf.curve(fin, 1f - fadeMargin)));
				if(poly){
					Fill.poly(
						Tmp.v1.x,
						Tmp.v1.y,
						sides,
						particleSize * particleSizeInterp.apply(fin) * build.warmup(),
						particleRotation
					);
				}else{
					Fill.circle(
						Tmp.v1.x,
						Tmp.v1.y,
						particleSize * particleSizeInterp.apply(fin) * build.warmup()
					);
				}
			}
			
			Draw.blend();
			Draw.reset();
		}
		Draw.z(z);
	}
}
