package sw.type.weather;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.weather.*;

import static mindustry.Vars.*;

public class EmberParticleWeather extends ParticleWeather {
	public Color lightColor = Color.white.cpy();
	public float lightOpacity = 1f;
	public float lightRadiusMin = 60f, lightRadiusMax = 80f;
	
	public EmberParticleWeather(String name) {
		super(name);
	}
	
	@Override
	public void drawOver(WeatherState state) {
		rand.setSeed(0);
		Tmp.r1.setCentered(Core.camera.position.x, Core.camera.position.y, Core.graphics.getWidth() / renderer.minScale(), Core.graphics.getHeight() / renderer.minScale());
		Tmp.r1.grow(sizeMax * 1.5f);
		Core.camera.bounds(Tmp.r2);
		int total = (int)(Tmp.r1.area() / density * state.intensity);
		
		for(int i = 0; i < total; i++){
			float scl = rand.random(0.5f, 1f);
			float scl2 = rand.random(0.5f, 1f);
			float size = rand.random(sizeMin, sizeMax);
			float lightRadius = rand.random(lightRadiusMin, lightRadiusMax);
			float x = (rand.random(0f, 80000f) + Time.time * (useWindVector ? state.windVector.x : 1f) * xspeed * scl2);
			float y = (rand.random(0f, 80000f) + Time.time * (useWindVector ? state.windVector.y : 1f) * yspeed * scl);
			float alpha = rand.random(minAlpha, maxAlpha);
			float rotation = randomParticleRotation ? rand.random(0f, 360f) : 0f;
			
			x += Mathf.sin(y, rand.random(sinSclMin, sinSclMax), rand.random(sinMagMin, sinMagMax));
			
			x -= Tmp.r1.x;
			y -= Tmp.r1.y;
			x = Mathf.mod(x, Tmp.r1.width);
			y = Mathf.mod(y, Tmp.r1.height);
			x += Tmp.r1.x;
			y += Tmp.r1.y;
			
			if(Tmp.r3.setCentered(x, y, size + lightRadius).overlaps(Tmp.r2)){
				Draw.color(color, alpha * state.opacity * color.a);
				Draw.rect(region, x, y, size, size, rotation);
				Drawf.light(x, y, lightRadius, lightColor, lightOpacity * state.opacity);
			}
		}
		
		Draw.reset();
	}
}
