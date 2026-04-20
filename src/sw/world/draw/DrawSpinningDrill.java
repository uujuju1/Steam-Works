package sw.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import sw.entities.*;
import sw.graphics.*;
import sw.world.blocks.production.RangedDrill.*;

import static mindustry.Vars.*;

public class DrawSpinningDrill extends DrawBlock {
	public TextureRegion
		itemRegion,
		startBoreRegion, boreRegion, endBoreRegion;

	public Axle startAxle, endAxle, middleAxle;

	public float layer = -1;

	public RangedDrillBuild cast(Building build) {
		try {
			return (RangedDrillBuild) build;
		} catch(Exception e) {
			throw new ClassCastException("This drawer requires the block to be a RangedDrill. Use a different drawer.");
		}
	}

	@Override
	public void draw(Building build) {
		if(build.isPayload()) return;

		RangedDrillBuild b = cast(build);

		for(int i = 0; i < b.block.size; i++) {
			float rot = (build.rotdeg() + 90f) % 180f - 90f;
			float spin = build.totalProgress();

			Tile face = b.facing[i];
			if(face != null){
				Point2 p = b.lasers[i];

				float z = Draw.z();
				Draw.z(layer);
				float dst = Math.abs(p.x - face.x) + Math.abs(p.y - face.y);
				float
					dsx = p.x * tilesize,
					dsy = p.y * tilesize,
					dx = face.worldx(),
					dy = face.worldy();

				Draws.palette(startAxle.paletteLight, startAxle.paletteMedium, startAxle.paletteDark);
				Draws.regionCylinder(startAxle.regions[0], dsx, dsy, startAxle.width, startAxle.height, (build.rotation == 1 || build.rotation == 2 ? -1f : 1f) * spin * startAxle.spinScl, build.rotdeg(), startAxle.circular);
				Draws.palette();

				if (build.rotation == 1 || build.rotation == 2) Draw.yscl = -1;
				Draw.rect(startBoreRegion, dsx, dsy, build.rotdeg());
				Draw.yscl = 1;
				if(dst != 0) {
					for(int j = 0; j < dst; j++) {
						float lx = (dx - dsx)/dst * j + dsx, ly = (dy - dsy)/dst * j + dsy;

						Draws.palette(middleAxle.paletteLight, middleAxle.paletteMedium, middleAxle.paletteDark);
						Draws.regionCylinder(middleAxle.regions[0], lx, ly, middleAxle.width, middleAxle.height, spin * middleAxle.spinScl, rot, middleAxle.circular);
						Draws.palette();

						if (build.rotation == 1 || build.rotation == 2) Draw.yscl = -1;
						Draw.rect((j != dst - 1) ? boreRegion : endBoreRegion, lx, ly, build.rotdeg());
						Draw.yscl = 1;
					}
				}
				for(int j : new int[]{-1, 1, 0}) {
					Tmp.v1.trns(30f * j + build.rotdeg(), -4f).add(dx, dy);

					Draws.palette(endAxle.paletteLight, endAxle.paletteMedium, endAxle.paletteDark);
					Draws.regionCylinder(endAxle.regions[0], Tmp.v1.x, Tmp.v1.y, endAxle.width, endAxle.height, (j == 0 ? -1f : 1f) * spin * endAxle.spinScl, rot + 30f * j, endAxle.circular);
					Draws.palette();

					Tmp.v1.trns(30f * j + build.rotdeg(), -4f + endAxle.width * 3f/4f).add(dx, dy);

					Draws.palette(endAxle.paletteLight, endAxle.paletteMedium, endAxle.paletteDark);
					Draws.regionCylinder(endAxle.regions[0], Tmp.v1.x, Tmp.v1.y, endAxle.width/2f, endAxle.height/2f, (j == 0 ? -1f : 1f) * spin * endAxle.spinScl, rot + 30f * j, endAxle.circular);
					Draws.palette();
				}
				Draw.z(z);
			}
		}

		if (b.lastItem != null) {
			Draw.color(b.lastItem.color);
			Draw.rect(itemRegion, b.x, b.y);
			Draw.color();
		}

		Draw.reset();
	}

	@Override
	public void load(Block block) {
		itemRegion = Core.atlas.find(block.name + "-item", "drill-item-" + block.size);

		startBoreRegion = Core.atlas.find(block.name + "-bore-start-top");
		boreRegion = Core.atlas.find(block.name + "-bore-middle-top");
		endBoreRegion = Core.atlas.find(block.name + "-bore-end-top");

		startAxle.load(block.name);
		middleAxle.load(block.name);
		endAxle.load(block.name);

//		startAxle.regions = Core.atlas.find(block.name + startAxle.suffix).split(startAxle.pixelWidth, startAxle.pixelHeight);
//		startAxle.shadowRegion = Core.atlas.find(block.name + startAxle.suffix + "-shadow");

//		endAxle.regions = Core.atlas.find(block.name + endAxle.suffix).split(endAxle.pixelWidth, endAxle.pixelHeight);
//		startAxle.shadowRegion = Core.atlas.find(block.name + startAxle.suffix + "-shadow");

//		middleAxle.regions = Core.atlas.find(block.name + middleAxle.suffix).split(middleAxle.pixelWidth, middleAxle.pixelHeight);
//		startAxle.shadowRegion = Core.atlas.find(block.name + startAxle.suffix + "-shadow");
	}
}
