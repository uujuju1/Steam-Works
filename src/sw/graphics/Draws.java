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

	public static void regionCylinder(TextureRegion[] regions, float x, float y, float width, float height, float angle, float rotation, boolean circular) {
		int sides = regions.length;
		for (int i = 0; i < sides; i++) {
			float mod1 = Mathf.mod(angle + 360f/regions.length * i, 360f);
			float mod2 = Mathf.mod(angle + 360f/regions.length * (i + 1), 360f);

			float cos1 = Mathf.cos(Mathf.degreesToRadians * mod1, 1f, 1f);
			float cos2 = Mathf.cos(Mathf.degreesToRadians * mod2, 1f, 1);
			float cos3 = Mathf.cos(Mathf.degreesToRadians * (angle + 360f/regions.length * (i + 0.5f)), 1, 1);

			if (cos3 > 0f) {
				Tmp.c1.set(paletteMedium).lerp(paletteLight, Mathf.clamp(cos3));
			} else {
				Tmp.c1.set(paletteDark).lerp(paletteMedium, Mathf.clamp(cos3 + 1f));
			}

			if (circular) {
				if (mod1 > 180) cos1 = 1f;
				if (mod2 > 180) cos2 = -1f;
			}

			Draw.mixcol(Tmp.c1, Tmp.c1.a);

			if ((cos1 - cos2 >= 0 && !circular) || (!(mod1 >= 180 && mod2 >= 180) && circular)) {
				Tmp.v1.trns(rotation, 0f, height * (cos1 + cos2)/4f);
				Draw.rect(regions[i], x + Tmp.v1.x, y + Tmp.v1.y, width, height * (cos1 - cos2)/2f, rotation);
			}
		}
	}
	public static void polyCylinder(int sides, float x, float y, float width, float height, float angle, float rotation, boolean circular) {
		for (int i = 0; i < sides; i++) {
			float mod1 = Mathf.mod(angle + 360f/sides * i, 360f);
			float mod2 = Mathf.mod(angle + 360f/sides * (i + 1), 360f);

			float cos1 = Mathf.sin(Mathf.degreesToRadians * mod1, 1f, 1f);
			float cos2 = Mathf.sin(Mathf.degreesToRadians * mod2, 1f, 1);
			float cos3 = Mathf.sin(Mathf.degreesToRadians * (angle + 360f/sides * (i + 0.5f)), 1, 1);

			if (cos3 > 0f) {
				Tmp.c1.set(paletteMedium).lerp(paletteLight, Mathf.clamp(cos3));
			} else {
				Tmp.c1.set(paletteDark).lerp(paletteMedium, Mathf.clamp(cos3 + 1f));
			}

			if (circular) {
				if (mod1 > 180) cos1 = 1f;
				if (mod2 > 180) cos2 = -1f;
			}

			Draw.color(Tmp.c1, Tmp.c1.a);

			if ((cos1 - cos2 >= 0 && !circular) || (!(mod1 >= 180 && mod2 >= 180) && circular)) {
				Tmp.v1.trns(rotation, 0f, height * (cos1 + cos2)/4f);
				Fill.rect(x + Tmp.v1.x, y + Tmp.v1.y, width, height/2f * (cos1 - cos2)/2f, rotation);
			}
		}
	}
}
