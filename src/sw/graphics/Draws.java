package sw.graphics;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;

public class Draws {
	public static Color paletteLight, paletteMedium, paletteDark;

	public static void palette() {
		paletteLight = paletteMedium = paletteDark = Color.clear;
	}
	public static void palette(Color light, Color medium, Color dark) {
		paletteLight = light;
		paletteMedium = medium;
		paletteDark = dark;
	}

	public static void regionCylinder(TextureRegion[] regions, float x, float y, float width, float height, float angle, float rotation) {
		int sides = regions.length;
		for (int i = 0; i < sides; i++) {
			float sin1 = Mathf.sin(Mathf.degreesToRadians * (angle + 360f/regions.length * i), 1f, 1f);
			float sin2 = Mathf.sin(Mathf.degreesToRadians * (angle + 360f/regions.length * (i + 1f)), 1f, 1);
			float sin3 = Mathf.sin(Mathf.degreesToRadians * (angle + 360f/regions.length * (i + 0.5f)), 1, 1);

			if (sin3 > 0f) {
				Tmp.c1.set(paletteMedium).lerp(paletteLight, Mathf.clamp(sin3));
			} else {
				Tmp.c1.set(paletteDark).lerp(paletteMedium, Mathf.clamp(sin3 + 1f));
			}

			Draw.mixcol(Tmp.c1, Tmp.c1.a);

			if (sin1 - sin2 >= 0f) {
				Draw.rect(regions[i], x, y + height * (sin1 + sin2)/4f, width, height/2f * (sin1 - sin2), rotation);
			}
		}
	}
	public static void polyCylinder(int sides, float x, float y, float width, float height, float angle, float rotation) {
		for (int i = 0; i < sides; i++) {
			float sin1 = Mathf.sin(Mathf.degreesToRadians * (angle + 360f/sides * i), 1f, 1f);
			float sin2 = Mathf.sin(Mathf.degreesToRadians * (angle + 360f/sides * (i + 1f)), 1f, 1);
			float sin3 = Mathf.sin(Mathf.degreesToRadians * (angle + 360f/sides * (i + 0.5f)), 1, 1);

			if (sin3 > 0f) {
				Tmp.c1.set(paletteMedium).lerp(paletteLight, Mathf.clamp(sin3));
			} else {
				Tmp.c1.set(paletteDark).lerp(paletteMedium, Mathf.clamp(sin3 + 1f));
			}

			Draw.mixcol(Tmp.c1, Tmp.c1.a);

			if (sin1 - sin2 >= 0f) {
				Fill.rect(x, y + height * (sin1 + sin2)/4f, width, height/2f * (sin1 - sin2), rotation);
			}
		}
	}
}
