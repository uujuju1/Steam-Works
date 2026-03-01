package sw.entities;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.util.*;
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

	@Deprecated public int polySides = 1;

	@Deprecated public boolean hasSprites = true;
	@Deprecated public boolean hasIcon = true;

	public boolean circular = false;

	public TextureRegion[] regions;
	@Deprecated public TextureRegion iconRegion;
	public TextureRegion shadowRegion;

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

	public void load(String base) {
		String trueName = name == null ? (base == null ? "" : base) + suffix : name;

		if (hasSprites) regions = Core.atlas.find(trueName).split(pixelWidth, pixelHeight)[0];
		shadowRegion = Core.atlas.find(trueName + "-shadow");
		iconRegion = Core.atlas.find("error");
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