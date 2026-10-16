package sw.world.blocks.environment;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.world.*;

public class MultiLayeredTree extends Block {
	public static final Rand rand = new Rand();

	public float rotationRand = 45f;
	public float swayScaleMin = 5f, swayScaleMax = 10, swayMagnitude = 22.5f;

	public MultiLayeredTree(String name, int layers) {
		super(name);
		variants = layers;
		solid = true;
		customShadow = true;
		clipSize = 80f;
	}

	@Override
	protected TextureRegion[] icons() {
		return variantRegions;
	}

	@Override
	public void drawBase(Tile tile) {
		rand.setSeed(tile.pos());

		Draw.alpha(Mathf.clamp(Core.camera.position.dst(tile.drawx(), tile.drawy()) / clipSize));
		for (int i = 0; i < variantRegions.length; i++) {
			float rotation = rand.random(rotationRand) + Mathf.absin(Mathf.degreesToRadians * (Time.time + rand.random(360f)), rand.random(swayScaleMin, swayScaleMax), swayMagnitude);
			Draw.rect(variantShadowRegions[i], tile.drawx(), tile.drawy(), rotation);
			Draw.rect(variantRegions[i], tile.drawx(), tile.drawy(), rotation);
		}
	}

	@Override
	public void drawShadow(Tile tile) {
	}
}
