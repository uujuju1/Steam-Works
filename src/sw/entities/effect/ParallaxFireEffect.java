package sw.entities.effect;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import mindustry.entities.*;
import mindustry.graphics.*;
import sw.math.*;

import static sw.content.SWFx.*;

public class ParallaxFireEffect extends Effect {
	public float minSize = 1f;
	public float maxSize = 3f;
	public float minHeight = 0f;
	public float maxHeight = 5f;
	public float minRadius = 4f;
	public float maxRadius = 8f;

	public int particles = 2;

	public float taper = 1f;

	public Interp taperCurve = Interp.linear;
	public Interp sizeCurve = Interp.slope;
	public Interp heightCurve = Interp.linear;

	@Override
	public void render(EffectContainer e) {
		rand.setSeed(e.id);

		Draw.color(Color.darkGray, Color.gray, e.finpow());
		Angles.randLenVectors(e.id, particles, minRadius, maxRadius * e.finpow(), (x, y) -> {
			Parallax.getParallaxFrom(temp.set(x, y).lerp(Vec2.ZERO, e.fin(taperCurve) * taper).add(e.x, e.y), Core.camera.position, e.fin(heightCurve) * rand.random(minHeight, maxHeight));
			Fill.circle(temp.x, temp.y, rand.random(minSize, maxSize) * Mathf.clamp(e.fin(sizeCurve) * 4f));
		});

		Draw.blend(Blending.additive);
		Angles.randLenVectors(e.id + 1, particles, minRadius, maxRadius * e.finpow(), (x, y) -> {
			float a = rand.random(0f, 0.75f);
			Draw.color(Pal.accent, Pal.turretHeat, e.finpow() * (1f - a) + a);
			Parallax.getParallaxFrom(temp.set(x, y).lerp(Vec2.ZERO, e.fin(taperCurve) * taper).add(e.x, e.y), Core.camera.position, e.fin(heightCurve) * rand.random(minHeight, maxHeight));
			Fill.circle(temp.x, temp.y, rand.random(minSize, maxSize) * Mathf.clamp(e.fin(sizeCurve) * 4f));
		});
		Draw.blend();
	}
}
