package sw.world.blocks.environment;

import arc.*;
import arc.func.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.entities.*;
import mindustry.world.*;
import sw.content.*;
import sw.util.*;
import sw.world.*;
import sw.world.MultiShape.*;

public class GasVent extends MultiOverlayFloor {
	public Intf<Tile> density = tile -> 0;
	public int maxDensity = 0;

	public Effect smokeEffect = SWFx.gasVent;
	public float smokeEffectInterval = 20f;

	public TextureRegion[] densityVariants;

	public GasVent(String name) {
		super(name);
		hasShadow = false;
		multiShapeBuild = GasVentBuild::new;
	}

	@Override
	public void load() {
		super.load();
		if (maxDensity > 0) {
			densityVariants = SWDraw.getRegions(Core.atlas.find(name + "-tiles"), variants, maxDensity, 32);
		} else densityVariants = variantRegions;
	}

	@Override
	public void drawBase(Tile tile) {
		if (maxDensity > 0) {
			Draw.rect(
				densityVariants[Mathf.randomSeed(tile.pos(), 0, Math.max(0, variantRegions.length - 1)) + variants * Math.min(maxDensity - 1, density.get(tile))],
				tile.worldx(),
				tile.worldy()
			);
		} else if(variants > 0) {
			Draw.rect(variantRegions[Mathf.randomSeed(tile.pos(), 0, Math.max(0, variantRegions.length - 1))], tile.worldx(), tile.worldy());
		} else {
			Draw.rect(region, tile.worldx(), tile.worldy());
		}
	}

	public class GasVentBuild extends MultiShapeBuild {
		public float effectTime = 0;

		@Override
		public void update(MultiShape shape) {
			effectTime += Time.delta;
			if (effectTime > smokeEffectInterval) {
				smokeEffect.at(shape.centerX(), shape.centerY(), 0, shape);
				effectTime %= 1f;
			}
		}
	}
}
