package sw.type.weather;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.type.*;

public class DustStormWeather extends Weather {
	public Color[] colors = new Color[]{Color.valueOf("BFB9BB"), Color.valueOf("898586"), Color.valueOf("555354")};
	public float sizeMin = 32, sizeMax = 64;
	public float density = 3000, intensity = 1, opacity = 0.5f;
	public float windx = 1, windy = 0.1f;
	public float minAlpha = 1, maxAlpha = 1;
	public float sinSclMin = 50, sinSclMax = 100, sinMagMin = 2, sinMagMax = 20;

	public DustStormWeather(String name) {
		super(name);
	}

	@Override
	public void drawOver(WeatherState state) {
		rand.setSeed(state.id);
		Tmp.r1.setCentered(
			Core.camera.position.x, Core.camera.position.y,
			Core.graphics.getWidth() / Vars.renderer.minScale(),
			Core.graphics.getHeight() / Vars.renderer.minScale()
		);
		Tmp.r1.grow(sizeMax * 1.5f);
		Core.camera.bounds(Tmp.r2);
		int total = Mathf.round(Tmp.r1.area() / density * intensity);

		for(int i = 0; i < total; i++){
			Draw.color(colors[rand.random(0, colors.length - 1)], opacity);
			float scl = rand.random(0.5f, 1f);
			float scl2 = rand.random(0.5f, 1f);
			float size = rand.random(sizeMin, sizeMax);
			size = Mathf.sin(Time.time, rand.random(sinSclMin, sinSclMax), size) * state.opacity;
			float x = (rand.random(0, Vars.world.unitWidth()) + Time.time * windx * scl2);
			float y = (rand.random(0, Vars.world.unitHeight()) + Time.time * windy * scl);
			float alpha = rand.random(minAlpha, maxAlpha);

			x += Mathf.sin(y, rand.random(sinSclMin, sinSclMax), rand.random(sinMagMin, sinMagMax));

			x -= Tmp.r1.x;
			y -= Tmp.r1.y;
			x = Mathf.mod(x, Tmp.r1.width);
			y = Mathf.mod(y, Tmp.r1.height);
			x += Tmp.r1.x;
			y += Tmp.r1.y;

			if(Tmp.r3.setCentered(x, y, sizeMax * 2f).overlaps(Tmp.r2)){
				Draw.alpha(alpha * opacity * state.opacity);
				Fill.circle(
					x, y,
					size * Interp.smooth.apply(
						Mathf.clamp(Mathf.dst(
							Vars.player.x, Vars.player.y, x, y
						) / Mathf.dst(
							0f, 0f, Vars.world.unitWidth()/2f, Vars.world.unitHeight()/2f)
						)
					) * 2f
				);
			}
		}

		Draw.reset();
	}
}
