package sw.content;

import sw.entities.*;
import sw.graphics.*;

public class Axles {
	public static Axle quarterBlock, halfBlock, block, doubleBlock, tripleBlock;

	public static void load() {
		quarterBlock = new Axle() {{
			name = "sw-axle-8-14";

			pixelWidth = 2;
			pixelHeight = 1;

			width = 2f;
			height = 3.5f;

			paletteLight = SWPal.axleLight;
			paletteMedium = SWPal.axleMedium;
			paletteDark = SWPal.axleDark;
		}};
		halfBlock = new Axle() {{
			name = "sw-axle-16-14";

			pixelWidth = 4;
			pixelHeight = 1;

			width = 4f;
			height = 3.5f;

			paletteLight = SWPal.axleLight;
			paletteMedium = SWPal.axleMedium;
			paletteDark = SWPal.axleDark;
		}};
		block = new Axle() {{
			name = "sw-axle-32-14";

			pixelWidth = 8;
			pixelHeight = 1;

			width = 8f;
			height = 3.5f;

			paletteLight = SWPal.axleLight;
			paletteMedium = SWPal.axleMedium;
			paletteDark = SWPal.axleDark;
		}};
		doubleBlock = new Axle() {{
			name = "sw-axle-64-14";

			pixelWidth = 16;
			pixelHeight = 1;

			width = 16f;
			height = 3.5f;

			paletteLight = SWPal.axleLight;
			paletteMedium = SWPal.axleMedium;
			paletteDark = SWPal.axleDark;
		}};
		tripleBlock = new Axle() {{
			name = "sw-axle-96-14";

			pixelWidth = 24;
			pixelHeight = 1;

			width = 24f;
			height = 3.5f;

			paletteLight = SWPal.axleLight;
			paletteMedium = SWPal.axleMedium;
			paletteDark = SWPal.axleDark;
		}};
	}
}
