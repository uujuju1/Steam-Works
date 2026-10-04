package sw.entities.part;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import mindustry.entities.part.*;
import sw.annotations.Annotations.*;
import sw.gen.*;
import sw.graphics.*;

public class AxlePart extends DrawPart {
	public String name;
	public String suffix = "";
	public float x, y;
	public float width, height;
	public int pixelWidth = 1, pixelHeight = 1;
	public float rotation;
	public int sides = -1;
	public float layer = -1, layerOffset;
	
	public Color lightTint = SWPal.axleLight;
	public Color mediumTint = SWPal.axleMedium;
	public Color darkTint = SWPal.axleDark;

	public Color colorFrom = Color.white;
	public Color colorTo = Color.white;
	
	public PartProgress progress = PartProgress.reload;
	public PartProgress colorProgress = PartProgress.reload;

	public Seq<PartMove> moves = new Seq<>();
	
	public @Load(value = "@name$", splits = true, width = "pixelWidth", height = "pixelHeight") TextureRegion[][] regions;
	
	@Override
	public void draw(PartParams params) {
		float z = Draw.z();
		Draw.z((layer > 0 ? layer : z) + layerOffset);

		float dx = params.x + Angles.trnsx(params.rotation - 90, x, y);
		float dy = params.y + Angles.trnsy(params.rotation - 90, x, y);
		float dr = params.rotation + rotation - 90f;

		Draw.color(colorFrom, colorTo, colorProgress.getClamp(params));

		for (PartMove move : moves) {
			float moveProgress = move.progress.get(params);
			dx += Angles.trnsx(params.rotation - 90, move.x * moveProgress, move.y * moveProgress);
			dy += Angles.trnsy(params.rotation - 90, move.x * moveProgress, move.y * moveProgress);
			dr += move.rot * moveProgress;
		}

		Draws.palette(lightTint, mediumTint, darkTint);
		Draws.regionCylinder(regions[0], dx, dy, width, height, progress.get(params), dr, false);
		Draws.palette();
		
		Draw.reset();
		Draw.z(z);
	}

	@Override
	public void load(String name) {
		if (this.name == null) this.name = name == null ? suffix : name + suffix;
		SWContentRegionRegistry.load(this);
		sides = regions[0].length;
	}
}
