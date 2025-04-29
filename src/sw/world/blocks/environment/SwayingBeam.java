package sw.world.blocks.environment;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;

public class SwayingBeam extends Prop {
	public float shadowOffset = -2;
	public float swayScale = 1;
	public float swayMagnitude = 1;
	public float rotationOffset = 0;
	public float baseRotation = 0;

	public Interp reverseCurve = Interp.pow2;

	public TextureRegion[] reverseRegions;

	public SwayingBeam(String name) {
		super(name);
		solid = true;
		destructible = false;
		variants = 3;
		customShadow = true;
	}

	@Override
	public void drawBase(Tile tile) {
		float
			x = tile.worldx(),
			y = tile.worldy(),
			rotation = baseRotation + Mathf.sin(Time.time + Mathf.randomSeed(tile.pos() + 1, 2 * Mathf.pi / swayScale), swayScale, swayMagnitude) + Mathf.randomSeed(tile.pos(), rotationOffset);

		Draw.z(Layer.power - 1);

		TextureRegion shadow = variantShadowRegions[Mathf.randomSeed(tile.pos(), 0, variantShadowRegions.length - 1)];
		if (shadow.found()) {
			Draw.rect(shadow, x + shadowOffset, y + shadowOffset, rotation);
		}

		Draw.z(Layer.power + 1);

		float a = reverseCurve.apply(Mathf.slope(rotation / 360f));
		Draw.alpha(1f);
		Draw.rect(reverseRegions[Mathf.randomSeed(tile.pos(), 0, reverseRegions.length - 1)], x, y, rotation);
		Draw.alpha(1f - a);
		Draw.rect(variantRegions[Mathf.randomSeed(tile.pos(), 0, variantRegions.length - 1)], x, y, rotation);
	}

	@Override
	public void drawShadow(Tile tile) {

	}

	@Override
	public void load() {
		super.load();

		if (variants != 0) {
			reverseRegions = new TextureRegion[variants];
			for (int i = 0; i < variants; i++) {
				reverseRegions[i] = Core.atlas.find(name + "-reverse" + (i + 1));
			}
		}
	}
}
