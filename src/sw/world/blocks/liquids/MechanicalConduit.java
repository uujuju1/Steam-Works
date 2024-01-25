package sw.world.blocks.liquids;

import arc.*;
import arc.graphics.g2d.*;
import mindustry.world.blocks.liquid.*;
import sw.util.*;

public class MechanicalConduit extends ArmoredConduit {
	public TextureRegion[] regions;

	public MechanicalConduit(String name) {
		super(name);
	}

	@Override public TextureRegion[] icons() {
		return new TextureRegion[]{region};
	}

	@Override
	public void load() {
		super.load();
		regions = SWDraw.getRegions(Core.atlas.find(name + "-tiles"), 4, 4, 32);
	}

	public class MechanicalConduitBuild extends ArmoredConduitBuild {
		public int tiling = 0;

		@Override
		public void draw() {
			Draw.rect(bottomRegion, x, y, 0);
			if (liquids().currentAmount() > 0.01f) {
				Draw.color(liquids.current().color);
				Draw.alpha(liquids.currentAmount() / liquidCapacity);
				Draw.rect(liquidRegion, x, y);
				Draw.color();
			}
			Draw.rect(regions[tiling], x, y, 0);
			if (tiling != 0) Draw.rect(topRegion, x, y, rotdeg());
		}

		@Override
		public void onProximityUpdate() {
			super.onProximityUpdate();
			for (int i = 0; i < 4; i++) {
				if (nearby(i) instanceof MechanicalConduitBuild b && (b.front() == this || front() == b)) tiling |= (1 << i);
			}
		}
	}
}
