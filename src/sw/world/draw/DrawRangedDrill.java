package sw.world.draw;

import arc.graphics.g2d.*;
import arc.math.geom.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import sw.annotations.Annotations.*;
import sw.world.blocks.production.RangedDrill.*;

import static mindustry.Vars.*;

public class DrawRangedDrill extends BlockDrawer {
	public @Load(value = "@loadBlock.name$-item", fallBack = "drill-item-@loadBlock.size$") TextureRegion itemRegion;
	public @Load(value = "@loadBlock.name$-bore-item", fallBack = "drill-item-2") TextureRegion itemBoreRegion;
	
	public @Load("@loadBlock.name$-start-bore") TextureRegion startBoreRegion;
	public @Load("@loadBlock.name$-bore") TextureRegion boreRegion;
	public @Load("@loadBlock.name$-bore-end") TextureRegion endBoreRegion;
	public @Load("@loadBlock.name$-rotator") TextureRegion rotatorRegion;

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

		if (b.rotation == 1 || b.rotation == 2) Draw.yscl = -1;
		Draw.rect(startBoreRegion, b.x, b.y, b.rotdeg());
		Draw.yscl = 1;
		for(int i = 0; i < b.block.size; i++){
			Tile face = b.facing[i];
			if(face != null){
				Point2 p = b.lasers[i];

				Draw.z(Layer.power - 1);
				float dst = Math.abs(p.x - face.x) + Math.abs(p.y - face.y);
				float
					dsx = p.x * tilesize,
					dsy = p.y * tilesize,
					dx = face.worldx(),
					dy = face.worldy();
				Draw.rect(rotatorRegion, dx, dy, b.totalTime);
				if(dst != 0) {
					for(int j = 0; j < dst; j++) {
						float lx = (dx - dsx)/dst * j + dsx, ly = (dy - dsy)/dst * j + dsy;

						Draw.rect(boreRegion, lx, ly, (b.rotdeg() + 90f) % 180f - 90f);
						if (b.lastItem != null) {
							Draw.color(b.lastItem.color);
							Draw.rect(itemBoreRegion, lx, ly);
							Draw.color();
						}
					}
				}

				if (b.rotation == 1 || b.rotation == 2) Draw.yscl = -1;
				Draw.rect(endBoreRegion, dx, dy, b.rotdeg());
				Draw.yscl = 1;
				Draw.reset();
			}
		}

		if (b.lastItem != null) {
			Draw.color(b.lastItem.color);
			Draw.rect(itemRegion, b.x, b.y);
			Draw.color();
		}

		Draw.reset();
	}
}
