package sw.entities.part;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import mindustry.entities.part.*;
import sw.entities.*;
import sw.graphics.*;

public class AxlePart extends DrawPart {
	public PartProgress progress = PartProgress.reload;

	public Axle axle = new Axle("-axle");

	public float layer = -1, layerOffset;

	public boolean outline = true;

	@Override
	public void draw(PartParams params) {
		float z = Draw.z();
		if (layer > 0) Draw.z(layer);

		Draw.z(Draw.z() + layerOffset);

		float rot = params.rotation + axle.rotation - 90f;
		float spin = progress.getClamp(params) * axle.spinScl;
		float dx = params.x + Angles.trnsx(rot, axle.x, axle.y);
		float dy = params.y + Angles.trnsy(rot, axle.x, axle.y);

		Draws.palette(axle.paletteLight, axle.paletteMedium, axle.paletteDark);
		if (axle.hasSprites) {
			Draws.regionCylinder(axle.regions, dx, dy, axle.width, axle.height, spin, rot, axle.circular);
		} else {
			Draws.polyCylinder(axle.polySides, dx, dy, axle.width, axle.height, spin, rot, axle.circular);
		}
		Draws.palette();
		if (axle.circular) Draw.rect(axle.shadowRegion, dx, dy, rot);

		Draw.reset();
		Draw.z(z);
	}

	@Override
	public void getOutlines(Seq<TextureRegion> out) {
		if (axle.hasIcon && outline) out.add(axle.iconRegion);
	}

	@Override
	public void load(String name) {
		if (axle.hasSprites) axle.regions = Core.atlas.find(name + axle.suffix).split(axle.pixelWidth, axle.pixelHeight)[0];
		if (axle.hasIcon) axle.iconRegion = Core.atlas.find(axle.iconOverride == null ? name + axle.suffix + "-icon" : axle.iconOverride);
		if (axle.circular) axle.shadowRegion = Core.atlas.find(name + axle.suffix + "-shadow");
	}
}
