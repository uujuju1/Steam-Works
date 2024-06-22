package sw.world.blocks.environment;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;

public class Spike extends TallBlock {
	public ObjectMap<Polygon, Color>[] variantShapes = new ObjectMap[]{new ObjectMap<>()};
	public Color shadowColor = Pal.shadow;

	public Spike(String name) {
		super(name);
	}

	@Override
	public void drawBase(Tile tile) {
		int variant = Mathf.randomSeed(tile.pos(), 0, Math.max(0, variantShapes.length - 1));
		variantShapes[variant].each((poly, color) -> {
			if (color == shadowColor) {
				Draw.z(Layer.power - 1);
			} else {
				Draw.z(Layer.power + 1);
			}
			poly.setPosition(tile.drawx(), tile.drawy());
			Draw.color(color, color.a);
			Fill.poly(poly);
		});
	}

	@Override
	public void init() {
		super.init();
		hasShadow = false;
	}
}
