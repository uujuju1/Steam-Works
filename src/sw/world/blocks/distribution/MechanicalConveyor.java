package sw.world.blocks.distribution;

import arc.*;
import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.blocks.distribution.*;
import sw.util.*;

import static mindustry.Vars.*;

/**
 * please ignore the mess
 */
public class MechanicalConveyor extends ArmoredConveyor {
	public TextureRegion[] tiles;
	public MechanicalConveyor(String name) {
		super(name);
	}

	@Override
	public void load() {
		super.load();
		tiles = SWDraw.getRegions(Core.atlas.find(name + "-tiles"), 4, 4, 32);
	}

	public class MechanicalConveyorBuild extends ArmoredConveyorBuild {
		public int getIndex() {
			int val = (enabled && clogHeat <= 0.5f ? (int)(((Time.time * speed * 6f * timeScale * efficiency)) % 4) : 0) * 4;
			if (front() instanceof ConveyorBuild && front().rotation == rotation) val++;
			if (back() instanceof ConveyorBuild && back().rotation == rotation) val+=2;
			return val;
		}

		@Override
		public void draw() {
			Draw.z(Layer.block - 0.2f);
			Draw.rect(tiles[getIndex()], x, y, rotdeg());
			Draw.z(Layer.block - 0.1f);
			float layer = Layer.block - 0.1f, wwidth = world.unitWidth(), wheight = world.unitHeight(), scaling = 0.01f;
			for(int i = 0; i < len; i++){
				Item item = ids[i];
				Tmp.v1.trns(rotation * 90, tilesize, 0);
				Tmp.v2.trns(rotation * 90, -tilesize / 2f, xs[i] * tilesize / 2f);

				float
					ix = (x + Tmp.v1.x * ys[i] + Tmp.v2.x),
					iy = (y + Tmp.v1.y * ys[i] + Tmp.v2.y);

				//keep draw position deterministic.
				Draw.z(layer + (ix / wwidth + iy / wheight) * scaling);
				Draw.rect(item.fullIcon, ix, iy, itemSize, itemSize);
			}
		}
	}
}
