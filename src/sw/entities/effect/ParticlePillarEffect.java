package sw.entities.effect;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.entities.*;
import mindustry.graphics.*;
import sw.math.*;

import static sw.content.SWFx.*;

public class ParticlePillarEffect extends Effect {
	public Color color1 = Color.white, color2 = Pal.darkerGray;
	public float alpha = 0.2f;
	public float radius = 8f;
	public float baseHeight, heightMin = 1f, heightMax = 5f;
	public float sizeMin = 1f, sizeMax = 2f;
	public int particles = 10;
	public Interp heightInterp = Interp.pow2In;
	public Interp sizeInterp = Interp.pow2In;
	public Interp alphaInterp = Interp.slope;

	@Override
	public void render(EffectContainer e) {
		rand.setSeed(e.id);

		Angles.randLenVectors(e.id, particles, radius, (x, y) ->  {
			Parallax.getParallaxFrom(
				temp.set(e.x + x, e.y + y),
				Core.camera.position,
				baseHeight + rand.random(heightMin, heightMax) * e.fin(heightInterp)
			);

			Draw.color(color1, color2, rand.random(1f));
			Draw.alpha(e.fin(alphaInterp) * alpha);
			Fill.circle(temp.x, temp.y, rand.random(sizeMin, sizeMax) * e.fin(sizeInterp));
		});
	}
}
