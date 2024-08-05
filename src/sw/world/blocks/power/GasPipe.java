package sw.world.blocks.power;

import arc.*;
import arc.graphics.g2d.*;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.world.*;
import sw.util.*;
import sw.world.graph.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class GasPipe extends Block {
	public GasConfig gasConfig = new GasConfig();

	public TextureRegion[] regions;

	public GasPipe(String name) {
		super(name);
		update = true;
	}

	@Override
	public void load() {
		super.load();
		regions = SWDraw.getRegions(Core.atlas.find(name + "-tiles"), 4, 4, 32, 32);
	}

	public class GasPipeBuild extends Building implements HasGas {
		public GasModule gas = new GasModule();

		public int tiling = 0;

		@Override
		public boolean connectTo(HasGas other) {
			return other instanceof GasPipeBuild && other.team() == team;
		}

		@Override
		public void draw() {
			Draw.rect(regions[tiling], x, y, 0);
		}

		@Override public GasModule gas() {
			return gas;
		}
		@Override public GasConfig gasConfig() {
			return gasConfig;
		}

		@Override
		public void read(Reads read) {
			super.read(read);
			gas.read(read);
		}

		@Override
		public void onProximityUpdate() {
			super.onProximityUpdate();

			new GasGraph().addBuild(this);
			nextBuilds().each(build -> gasGraph().merge(build.gasGraph(), false));

			tiling = 0;
			for (int i = 0; i < 4; i++) {
				if (nearby(i) instanceof GasPipeBuild && nearby(i).team == team) {
					tiling |= 1 << i;
				}
			}
		}

		@Override
		public void onProximityRemoved() {
			super.onProximityRemoved();
			gasGraph().remove(this, true);
		}

		@Override
		public void write(Writes write) {
			super.write(write);
			gas.write(write);
		}
	}
}
