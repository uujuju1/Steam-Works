package sw.tools;

import arc.func.*;
import arc.graphics.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;

public interface SpriteProcessor {
	Seq<Cons<Pixmap>> adds = new Seq<>();

	void process();

	/**
	 * Creates a copy of a Pixmap grown with padding.
	 */
	default Pixmap grow(Pixmap region, int right, int left, int top, int bottom) {
		Pixmap out = new Pixmap(region.width + left + right, region.height + top + bottom);

		out.draw(region, left, top);

		return out;
	}

	/**
	 * Adds an outline on a Pixmap.
	 * <li> When addRegion == false, only the outline is shown.
	 */
	default Pixmap outline(Pixmap region, int outlineRadius, Color outlineColor, boolean addRegion) {
		Pixmap out = region.copy();
		Pixmap copy = region.copy();

		out.fill(Color.clear);
		for(int i = 0; i < outlineRadius; i++) {
			boolean isEven = i % 2 == 0;
			adds.clear();
			copy.each((x, y) ->  {
				for(Point2 offset : Geometry.d4) {
					if (
						copy.get(x + offset.x, y + offset.y) != 0 &&
							copy.getA(x + offset.x, y + offset.y) != 0 &&
							copy.getA(x, y) == 0
					) {
						adds.add(pixmap -> pixmap.set(x, y, outlineColor));
					}
				}
				if (isEven) for(Point2 offset : Geometry.d8edge) {
					if (
						copy.get(x + offset.x, y + offset.y) != 0 &&
							copy.getA(x + offset.x, y + offset.y) != 0 &&
							copy.getA(x, y) == 0
					) {
						adds.add(pixmap -> pixmap.set(x, y, outlineColor));
					}
				}
			});
			adds.each(c -> {
				c.get(out);
				c.get(copy);
			});
		}
		if (addRegion) out.dispose();
		else copy.dispose();
		return addRegion ? copy : out;
	}
	default Pixmap outline(Pixmap region, int outlineRadius, Color outlineColor) {
		return outline(region, outlineRadius, outlineColor, false);
	}

	/**
	 * Rotates a Pixmap by the specified amount of degrees
	 */
	default Pixmap rotate(Pixmap region, float degrees) {
		Pixmap base = region.copy();
		region.each((x, y) -> {
			Tmp.v1.set(x, y).sub(base.width/2f, base.height/2f).rotate(degrees).add(base.width/2f, base.height/2f);
			region.set(x, y, base.get((int) Tmp.v1.x, (int) Tmp.v1.y));
		});
		base.dispose();
		return region;
	}
}
