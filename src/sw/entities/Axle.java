package sw.entities;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.util.*;
import sw.graphics.*;

public class Axle {
	public String suffix;
	public @Nullable String iconOverride;

	public float x, y, rotation, angle;

	public float width = 8f, height = 8f;

	public float spinScl = 1;

	public int pixelWidth = 32;
	public int pixelHeight = 32;

	public int polySides = 1;

	public boolean hasSprites = true;
	public boolean hasIcon = true;

	public boolean circular = false;

	public TextureRegion[] regions;
	public TextureRegion iconRegion;
	public TextureRegion shadowRegion;

	public Color paletteLight = SWPal.axleMedium;
	public Color paletteMedium = SWPal.axleMedium;
	public Color paletteDark = SWPal.axleMedium;

	public Axle(String suffix) {
		this.suffix = suffix;
	}
}