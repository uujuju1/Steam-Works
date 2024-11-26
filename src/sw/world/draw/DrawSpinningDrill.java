package sw.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import sw.util.*;
import sw.world.blocks.production.RangedDrill.*;
import sw.world.draw.DrawAxles.*;

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

				SWDraw.rotatingRects(startAxle.regions, dsx, dsy, startAxle.width, startAxle.height, build.rotdeg(), (build.rotation == 1 || build.rotation == 2 ? -1f : 1f) * spin * startAxle.spinScl);
				Draw.rect(startAxle.shadowRegion, dsx, dsy, rot);
				if (build.rotation == 1 || build.rotation == 2) Draw.yscl = -1;
				Draw.rect(startBoreRegion, dsx, dsy, build.rotdeg());
				Draw.yscl = 1;
				if(dst != 0) {
					for(int j = 0; j < dst; j++) {
						float lx = (dx - dsx)/dst * j + dsx, ly = (dy - dsy)/dst * j + dsy;

						SWDraw.rotatingRects(middleAxle.regions, lx, ly, middleAxle.width, middleAxle.height, rot, spin * middleAxle.spinScl);
						Draw.rect(middleAxle.shadowRegion, lx, ly, rot);
						if (build.rotation == 1 || build.rotation == 2) Draw.yscl = -1;
						Draw.rect((j != dst - 1) ? boreRegion : endBoreRegion, lx, ly, build.rotdeg());
						Draw.yscl = 1;
					}
				}
				for(int j : new int[]{-1, 1, 0}) {
					Tmp.v1.trns(30f * j + build.rotdeg(), -4f).add(dx, dy);
					SWDraw.rotatingRects(endAxle.regions, Tmp.v1.x, Tmp.v1.y, endAxle.width, endAxle.height, rot + 30f * j, (j == 0 ? -1f : 1f) * spin * endAxle.spinScl);
					Draw.rect(endAxle.shadowRegion, Tmp.v1.x, Tmp.v1.y, endAxle.width, endAxle.height, rot + 30f * j);

					Tmp.v1.trns(30f * j + build.rotdeg(), -4f + endAxle.width * 3f/4f).add(dx, dy);
					SWDraw.rotatingRects(endAxle.regions, Tmp.v1.x, Tmp.v1.y, endAxle.width/2f, endAxle.height/2f, rot + 30f * j, (j == 0 ? -1f : 1f) * spin * endAxle.spinScl);
					Draw.rect(endAxle.shadowRegion, Tmp.v1.x, Tmp.v1.y, endAxle.width/2f, endAxle.height/2f, rot + 30f * j);
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

	@Override public TextureRegion[] icons(Block block) {
		return new TextureRegion[]{};
	}

	@Override
	public void load(Block block) {
		itemRegion = Core.atlas.find(block.name + "-item", "drill-item-" + block.size);

		startBoreRegion = Core.atlas.find(block.name + "-bore-start-top");
		boreRegion = Core.atlas.find(block.name + "-bore-middle-top");
		endBoreRegion = Core.atlas.find(block.name + "-bore-end-top");

		startAxle.regions = Core.atlas.find(block.name + startAxle.suffix).split(startAxle.pixelWidth, startAxle.pixelHeight)[0];
		startAxle.shadowRegion = Core.atlas.find(block.name + startAxle.suffix + "-shadow");
		endAxle.regions = Core.atlas.find(block.name + endAxle.suffix).split(endAxle.pixelWidth, endAxle.pixelHeight)[0];
		endAxle.shadowRegion = Core.atlas.find(block.name + endAxle.suffix + "-shadow");
		middleAxle.regions = Core.atlas.find(block.name + middleAxle.suffix).split(middleAxle.pixelWidth, middleAxle.pixelHeight)[0];
		middleAxle.shadowRegion = Core.atlas.find(block.name + middleAxle.suffix + "-shadow");
	}
}
