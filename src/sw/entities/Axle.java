package sw.entities;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.util.*;
import sw.annotations.Annotations.*;
import sw.gen.*;
import sw.graphics.*;

public class Axle implements Cloneable {
	public String suffix = "";
	public String name;
	@Deprecated public @Nullable String iconOverride;

	public float x, y, rotation, angle;

	public float width = 8f, height = 8f;

	public float spinScl = 1;

	public int pixelWidth = 32;
	public int pixelHeight = 32;

	public boolean circular = false;

//	public TextureRegion[] regions;
	public @Load(value = "@name$", splits = true, width = "pixelWidth", height = "pixelHeight") TextureRegion[][] regions;
	public @Load("@name$-shadow") TextureRegion shadowRegion;

	public Color paletteLight = SWPal.axleMedium;
	public Color paletteMedium = SWPal.axleMedium;
	public Color paletteDark = SWPal.axleMedium;

	public Axle(String suffix) {
		this.suffix = suffix;
	}

	public Axle() {

	}

	public Axle copy() {
		try {
			return (Axle) clone();
		} catch (CloneNotSupportedException awesome) {
			throw new RuntimeException("exactly how it should...");
		}
	}

	public void draw(float x, float y, float rotation, float angle) {
		Color mixcolor = Draw.getMixColor();
		if (!mixcolor.equals(Color.clear)) {
			Draws.palette(
				mixcolor.cpy().lerp(paletteLight, paletteLight.a).a(1f - ((1f - mixcolor.a) * (1f - paletteLight.a))),
				mixcolor.cpy().lerp(paletteMedium, paletteMedium.a).a(1f - ((1f - mixcolor.a) * (1f - paletteMedium.a))),
				mixcolor.cpy().lerp(paletteDark, paletteDark.a).a(1f - ((1f - mixcolor.a) * (1f - paletteDark.a)))
			);
		} else Draws.palette(paletteLight, paletteMedium, paletteDark);
		Draws.regionCylinder(regions[0], x, y, width, height, angle, rotation, circular);
		Draws.palette();
		Draw.mixcol(mixcolor, mixcolor.a);

		if (circular) Draw.rect(shadowRegion, x, y, width, height, rotation);
	}

	public void load(String base) {
		if (name == null) name = (base == null ? "" : base) + suffix;

		SWContentRegionRegistry.load(this);
//		String trueName = name == null ? (base == null ? "" : base) + suffix : name;

//		if (hasSprites) regions = Core.atlas.find(trueName).split(pixelWidth, pixelHeight)[0];
//		shadowRegion = Core.atlas.find(trueName + "-shadow");
	}

	public Axle position(float x, float y, float rot, float rotScl) {
		Axle other = copy();
		other.x = x;
		other.y = y;
		other.rotation = rot;
		other.spinScl = rotScl;
		return other;
	}
}