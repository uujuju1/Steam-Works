package sw.entities;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import sw.annotations.Annotations.*;
import sw.graphics.*;

public class SegmentedAxle extends Axle {
	private static final Seq<Integer> drawOrder = new Seq<>();
	private static final Color l = new Color(), m = new Color(), d = new Color();

	/**
	 * By default, it's the same as the length of the {@link #regions}'s first array.
	 */
	public int sides = -1;
	/**
	 * List of indexes that segments will be drawn.
	 */
	public int[] segmentSides = new int[0];
	/**
	 * Size of the inner ring.
	 */
	public float minWidth = 4f;
	/**
	 * When true, a regular axle is drawn in the center, with its width set to {@link #minWidth}.
	 */
	public boolean drawInner = true;

	public @Load("@name$-segment-top") TextureRegion topSegmentRegion;
	public @Load("@name$-segment-right") TextureRegion rightSegmentRegion;
	public @Load("@name$-segment-bottom") TextureRegion bottomSegmentRegion;
	public @Load("@name$-segment-left") TextureRegion leftSegmentRegion;

	public SegmentedAxle(String suffix) {
		super(suffix);
	}

	public SegmentedAxle() {
	}

	@Override
	public void draw(float x, float y, float rotation, float angle) {
		Color mixcolor = Draw.getMixColor();
		if (!mixcolor.equals(Color.clear)) {
			l.set(mixcolor).lerp(paletteLight, paletteLight.a).a(1f - ((1f - mixcolor.a) * (1f - paletteLight.a)));
			m.set(mixcolor).lerp(paletteMedium, paletteMedium.a).a(1f - ((1f - mixcolor.a) * (1f - paletteMedium.a)));
			d.set(mixcolor).lerp(paletteDark, paletteDark.a).a(1f - ((1f - mixcolor.a) * (1f - paletteDark.a)));
		} else {
			l.set(paletteLight);
			m.set(paletteMedium);
			d.set(paletteDark);
		}

		drawOrder.clear();
		for(int i : segmentSides) drawOrder.add(i);

		drawOrder.each(i -> -Mathf.sin(Mathf.degreesToRadians * (angle + 360f/sides * (i + 0.5f))) < 0, i -> drawSegment(x, y, rotation - 90f, i * 360f/sides));
		if (drawInner) {
			Draws.palette(l, m, d);
			Draws.regionCylinder(regions[0], x, y, minWidth, height, angle, rotation, false);
			Draws.palette();
		}
		drawOrder.each(i -> -Mathf.sin(Mathf.degreesToRadians * (angle + 360f/sides * (i + 0.5f))) >= 0, i -> drawSegment(x, y, rotation - 90f, i * 360f/sides));
	}

	public void drawSegment(float x, float y, float rotation, float angle) {
		float radius = width / 2f;
		float inRadius = minWidth / 2f;
		float arc = 1f / sides;

		float mod = Mathf.mod(angle, 360);

		float cosMin = Mathf.cos(Mathf.degreesToRadians * mod, 1, 1);
		float cosMiddle = Mathf.cos(Mathf.degreesToRadians * (mod + 180 * arc), 1, 1);
		float cosMax = Mathf.cos(Mathf.degreesToRadians * (mod + 360 * arc), 1, 1);

		float sinMiddle = Mathf.sin(Mathf.degreesToRadians * (mod + 180 * arc), 1, 1);

		// 1 - top
		// 2 - right
		// 3 - bottom
		// 4 - left
		int[] drawOrder = new int[!drawInner ? 3 : 4];

		if (!drawInner) drawOrder[sinMiddle < 0 ? 0 : drawOrder.length - 1] = 3;
		drawOrder[sinMiddle < 0 ? drawOrder.length - 1 : 0] = 1;

		drawOrder[(!drawInner || sinMiddle < 0 ? 1 : 0) + (cosMin < 0 ? 1 : 0)] = 2;
		drawOrder[(!drawInner || sinMiddle < 0 ? 1 : 0) + (cosMin < 0 ? 0 : 1)] = 4;

		for(int i : drawOrder) {
			switch (i) {
				case 1 -> {
					float mx = Angles.trnsx(rotation, radius * cosMin);
					mx += Angles.trnsx(rotation, radius * cosMax);
					mx /= 2f;

					float my = Angles.trnsy(rotation, radius * cosMin);
					my += Angles.trnsy(rotation, radius * cosMax);
					my /= 2f;

					if (cosMiddle > 0f) {
						Tmp.c1.set(m).lerp(l, Mathf.clamp(cosMiddle));
					} else {
						Tmp.c1.set(d).lerp(m, Mathf.clamp(cosMiddle + 1f));
					}
					Draw.mixcol(Tmp.c1, Tmp.c1.a);

					Draw.rect(
						topSegmentRegion,
						x + mx,
						y + my,
						2 * radius * Mathf.sin(Mathf.degreesToRadians * (180 * arc)) * sinMiddle,
						height,
						rotation
					);
				}
				case 2 -> {
					if (sinMiddle > 0f) {
						Tmp.c1.set(m).lerp(d, Mathf.clamp(sinMiddle));
					} else {
						Tmp.c1.set(l).lerp(m, Mathf.clamp(sinMiddle + 1f));
					}
					Draw.mixcol(Tmp.c1, Tmp.c1.a);
					Draw.rect(
						rightSegmentRegion,
						x + Angles.trnsx(rotation, (radius/2 + inRadius/2) * cosMax),
						y + Angles.trnsy(rotation, (radius/2 + inRadius/2) * cosMax),
						(radius - inRadius) * cosMax,
						height,
						rotation
					);
				}
				case 3 -> {
					float mx = Angles.trnsx(rotation, inRadius * cosMin);
					mx += Angles.trnsx(rotation, inRadius * cosMax);
					mx /= 2f;

					float my = Angles.trnsy(rotation, inRadius * cosMin);
					my += Angles.trnsy(rotation, inRadius * cosMax);
					my /= 2f;

					if (cosMiddle > 0f) {
						Tmp.c1.set(m).lerp(d, Mathf.clamp(cosMiddle));
					} else {
						Tmp.c1.set(l).lerp(m, Mathf.clamp(cosMiddle + 1f));
					}
					Draw.mixcol(Tmp.c1, Tmp.c1.a);
					Draw.rect(
						bottomSegmentRegion,
						x + mx,
						y + my,
						2 * inRadius * Mathf.sin(Mathf.degreesToRadians * (180 * arc)) * sinMiddle,
						height,
						rotation
					);
				}
				case 4 -> {
					if (sinMiddle > 0f) {
						Tmp.c1.set(m).lerp(l, Mathf.clamp(sinMiddle));
					} else {
						Tmp.c1.set(d).lerp(m, Mathf.clamp(sinMiddle + 1f));
					}
					Draw.mixcol(Tmp.c1, Tmp.c1.a);
					Draw.rect(
						leftSegmentRegion,
						x + Angles.trnsx(rotation, (radius/2 + inRadius/2) * cosMin),
						y + Angles.trnsy(rotation, (radius/2 + inRadius/2) * cosMin),
						(radius - inRadius) * cosMin,
						height,
						rotation
					);
				}
				default -> {}
			}
		}
	}

	@Override
	public void load(String base) {
		super.load(base);

		if (sides < 0) sides = regions[0].length;
	}
}
