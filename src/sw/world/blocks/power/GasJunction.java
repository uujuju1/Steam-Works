package sw.world.blocks.power;

import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.world.*;
import sw.world.interfaces.*;

public class GasJunction extends GasPipe {
	public GasJunction(String name) {
		super(name);
	}

	@Override
	public boolean canReplace(Block other) {
		return super.canReplace(other) || (other instanceof GasPipe pipe && pipe.junctionReplacement == this);
	}

	@Override public TextureRegion getPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
		return region;
	}

	public class SpinJunctionBuild extends GasPipeBuild {
		@Override public boolean acceptsGas(HasSpin from, float amount) {
			return false;
		}

		@Override public boolean connectTo(HasSpin other) {
			return false;
		}

		@Override
		public void draw() {
			Draw.rect(region, x, y, 0);
		}

		@Override public HasSpin getGasDestination(HasSpin from) {
			return (nearby(from.relativeTo(tile)) instanceof HasSpin other && !(other instanceof SpinJunctionBuild)) ? other : this;
		}

		@Override public Seq<HasSpin> nextBuilds() {
			return Seq.with();
		}

		@Override public boolean outputsGas(HasSpin to, float amount) {
			return false;
		}

		@Override
		public void updateGas() {
			gas.setAmount(0);
			// has to be based on proximity cause nextBuilds will return an empty seq
			Seq<HasSpin> next = proximity.select(b -> b instanceof HasSpin).as();
			for(HasSpin other : next) gas.addAmount(other.spin().amount);
			gas.amount /= next.size;
			super.updateGas();
		}
	}
}
