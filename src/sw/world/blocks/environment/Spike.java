package sw.world.blocks.environment;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import sw.math.*;

public class Spike extends TallBlock {
	private static final Polygon tmp = new Polygon();

	public float spikeHeight = 5f;

	public ObjectMap<Polygon, Color>[] variantShapes = new ObjectMap[]{new ObjectMap<>()};
	public Color shadowColor = Pal.shadow;

	public Spike(String name) {
		super(name);
	}

	@Override
	public void drawBase(Tile tile) {
		int variant = Mathf.randomSeed(tile.pos(), 0, Math.max(0, variantShapes.length - 1));
		variantShapes[variant].keys().toSeq().sort(poly -> -Mathf.dst(
			(poly.getVertices()[0] + poly.getVertices()[2]) / 2f + tile.drawx(),
			(poly.getVertices()[1] + poly.getVertices()[3]) / 2f + tile.drawy(),
			Core.camera.position.x, Core.camera.position.y
		)).each(poly -> {
			Color color = variantShapes[variant].get(poly, Color.white);
			if (color == shadowColor) {
				Draw.z(Layer.power - 1);
			} else {
				Draw.z(Layer.power + 1);
			}
			float[] ver = new float[poly.getVertices().length + 2];
			System.arraycopy(poly.getVertices(), 0, ver, 0, poly.getVertices().length);
			ver[ver.length - 2] = ver[ver.length - 4] = Parallax.getParallaxFrom(poly.getVertices()[ver.length - 4] + tile.drawx(), Core.camera.position.x, spikeHeight) - tile.drawx();
			ver[ver.length - 1] = ver[ver.length - 3] = Parallax.getParallaxFrom(poly.getVertices()[ver.length - 3] + tile.drawy(), Core.camera.position.y, spikeHeight) - tile.drawy();


			tmp.setVertices(ver);
			tmp.setPosition(tile.drawx(), tile.drawy());
			Draw.color(color, color.a);
			Fill.poly(tmp);
		});
	}

	@Override
	public void init() {
		super.init();
		hasShadow = false;
	}
}
