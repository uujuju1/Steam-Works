package sw.world.blocks.power;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.content.*;
import mindustry.entities.effect.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import sw.content.*;
import sw.util.*;
import sw.world.graph.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class GasPipe extends Block {
	public GasConfig gasConfig = new GasConfig();

	public Block junctionReplacement;

	public TextureRegion[] regions;

	public GasPipe(String name) {
		super(name);
		update = true;
		var a = this;
		destroyEffect = new MultiEffect(
			new WrapEffect(Fx.dynamicExplosion, Color.white, 1f),
			new WrapEffect(SWFx.fragment, Color.white, 1f) {
				@Override
				public void create(float x, float y, float rotation, Color color, Object data) {
					effect.create(x, y, this.rotation, this.color, a);
				}
			}
		);
	}

	@Override
	public boolean canReplace(Block other) {
		return super.canReplace(other) || other == junctionReplacement;
	}

	@Override
	public TextureRegion getPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
		var current = new Object() {
			public int tiling;
		};
		for (int i = 0; i < 4; i++) {
			int finalI = i;
			list.each(other -> {
				if (new Point2(plan.x, plan.y).add(Geometry.d4(finalI)).equals(other.x, other.y)) {
					if (other.block instanceof GasPipe) current.tiling |= 1 << finalI;
				}
			});
		}
		return regions[current.tiling];
	}

	@Override
	public Block getReplacement(BuildPlan req, Seq<BuildPlan> plans) {
		return (junctionReplacement != null && req.tile().block() == this && req.tile().build.team == req.build().team()) ? junctionReplacement : this;
	}

	@Override
	public void load() {
		super.load();
		if (Core.atlas.find(name + "-tiles").found()) {
			regions = SWDraw.getRegions(Core.atlas.find(name + "-tiles"), 4, 4, 32, 32);
		} else {
			regions = new TextureRegion[16];
			for (int i = 0; i < regions.length; i++) {
				regions[i] = Core.atlas.find("error");
			}
		}
	}

	@Override
	public void setBars() {
		super.setBars();
		gasConfig.addBars(this);
	}

	@Override
	public void setStats() {
		super.setStats();
		gasConfig.addStats(stats);
	}

	public class GasPipeBuild extends Building implements HasGas {
		public GasModule gas = new GasModule();

		public int tiling = 0;

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
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			gas.read(read);
		}

		@Override
		public void onGasGraphUpdate() {
			tiling = 0;
			for (int i = 0; i < 4; i++) {
				if (nearby(i) instanceof HasGas gas && HasGas.connects(this, gas.getGasDestination(this))) {
					tiling |= 1 << i;
				}
			}
		}

		@Override
		public void onProximityUpdate() {
			super.onProximityUpdate();

			new GasGraph().addBuild(this);
			nextBuilds().each(build -> gasGraph().merge(build.gasGraph(), false));
			onGasGraphUpdate();
		}

		@Override
		public void onProximityRemoved() {
			super.onProximityRemoved();
			gasGraph().remove(this, true);
		}

		@Override
		public void updateTile() {
			updateGas();
		}

		@Override
		public void write(Writes write) {
			super.write(write);
			gas.write(write);
		}
	}
}
