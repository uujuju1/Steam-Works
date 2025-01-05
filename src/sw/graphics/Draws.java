package sw.graphics;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;

public class Draws {
	public static Color paletteLight, paletteMedium, paletteDark;
	private static boolean hasPalette = false;

	public static void palette() {
		paletteLight = paletteMedium = paletteDark = Color.clear;
		hasPalette = false;
	}
	public static void palette(Color light, Color medium, Color dark) {
		paletteLight = light;
		paletteMedium = medium;
		paletteDark = dark;
		hasPalette = true;
	}

	public static void regionCylinder(TextureRegion[] regions, float x, float y, float width, float height, float angle, float rotation) {
		int sides = regions.length;
		for (int i = 0; i < sides; i++) {
			float
				angle1 = angle + (360f/sides * i),
				angle2 = angle + (360f/sides * (i + 1f)),
				angle3 = angle + (360f/sides * (i + 0.5f)),
				mod1 = Mathf.mod(angle1, 360f),
				mod2 = Mathf.mod(angle2, 360f);

			if (!(mod1 >= 180f && mod2 >= 180f)) {
				float
					cos1 = -Mathf.cos(angle1 * Mathf.degreesToRadians),
					cos2 = -Mathf.cos(angle2 * Mathf.degreesToRadians),
					cos3 = -Mathf.cos(angle3 * Mathf.degreesToRadians) + 1f;
				if (mod1 > 180f) {
					cos1 = -1f;
				} else if (mod2 > 180f) {
					cos2 = 1f;
				}

				if (cos3 > 1f) {
					Tmp.c1.set(paletteMedium).lerp(paletteLight, Mathf.clamp(cos3 - 1f));
				} else {
					Tmp.c1.set(paletteDark).lerp(paletteMedium, Mathf.clamp(cos3));
				}

				cos1 = Mathf.map(cos1, -1, 1, y - height / 2, y + height / 2);
				cos2 = Mathf.map(cos2, -1, 1, y - height / 2, y + height / 2);

				if (hasPalette) Draw.mixcol(Tmp.c1, Tmp.c1.a);
				Draw.rect(regions[i], x, (cos1 + cos2) * 0.5f, width, cos2 - cos1, width * 0.5f, y - cos1, rotation);
			}
		}
	}
	public static void polyCylinder(int sides, float x, float y, float width, float height, float angle, float rotation) {
		for (int i = 0; i < sides; i++) {
			float
				angle1 = angle + (360f/sides * i),
				angle2 = angle + (360f/sides * (i + 1f)),
				angle3 = angle + (360f/sides * (i + 0.5f)),
				mod1 = Mathf.mod(angle1, 360f),
				mod2 = Mathf.mod(angle2, 360f);

			if (!(mod1 >= 180f && mod2 >= 180f)) {
				float
					cos1 = -Mathf.cos(angle1 * Mathf.degreesToRadians),
					cos2 = -Mathf.cos(angle2 * Mathf.degreesToRadians),
					cos3 = -Mathf.cos(angle3 * Mathf.degreesToRadians) + 1f;
				if (mod1 > 180f) {
					cos1 = -1f;
				} else if (mod2 > 180f) {
					cos2 = 1f;
				}

				if (cos3 > 1f) {
					Tmp.c1.set(paletteMedium).lerp(paletteLight, Mathf.clamp(cos3 - 1f));
				} else {
					Tmp.c1.set(paletteDark).lerp(paletteMedium, Mathf.clamp(cos3));
				}

				cos1 = Mathf.map(cos1, -1, 1, y - height / 2, y + height / 2);
				cos2 = Mathf.map(cos2, -1, 1, y - height / 2, y + height / 2);

				if (hasPalette) Draw.color(Tmp.c1, Tmp.c1.a);
        Fill.rect(x, (cos1 + cos2) * 0.5f, width, cos2 - cos1, rotation);

			}
		}
	}
}
