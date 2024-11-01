package sw.world.blocks.power;

import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import sw.world.graph.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class ShaftJunction extends Block {
	public SpinConfig spinConfig = new SpinConfig();

	public ShaftJunction(String name) {
		super(name);
	}

	@Override
	public boolean canReplace(Block other) {
		return super.canReplace(other) || (other instanceof GasPipe pipe && pipe.junctionReplacement == this);
	}

	@Override public TextureRegion getPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
		return region;
	}

	public class SpinJunctionBuild extends Building implements HasSpin {
		public SpinModule spin = new SpinModule();

		@Override public boolean connectTo(HasSpin other) {
			return false;
		}

		@Override
		public void draw() {
			Draw.rect(region, x, y, 0);
		}

		@Override
		public HasSpin getSpinGraphDestination(HasSpin from) {
			Point2 p = new Point2(size + 1, 0).rotate((relativeTo((Building) from) + 2) % 4);
			return from.nearby(p.x, p.y) instanceof HasSpin a ? a : null;
		}

		@Override
		public void onProximityUpdate() {
			super.onProximityUpdate();

			new SpinGraph().mergeFlood(this);
		}

		@Override
		public void onProximityRemoved() {
			super.onProximityRemoved();
			spinGraph().remove(this, true);
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			spin.read(read);
		}

		@Override public SpinModule spin() {
			return spin;
		}
		@Override public SpinConfig spinConfig() {
			return spinConfig;
		}

		@Override
		public void write(Writes write) {
			super.write(write);
			spin.write(write);
		}
	}
}
