package sw.world.blocks.power;

import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.*;
import mindustry.entities.units.*;
import sw.world.interfaces.*;

public class GasJunction extends GasPipe {
	public GasJunction(String name) {
		super(name);
	}

	@Override public TextureRegion getPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
		return region;
	}

	public class GasJunctionBuild extends GasPipeBuild {
		@Override public boolean acceptsGas(HasGas from, float amount) {
			return false;
		}

		@Override public boolean connectTo(HasGas other) {
			return false;
		}

		@Override
		public void draw() {
			Draw.rect(region, x, y, 0);
		}

		@Override public HasGas getGasDestination(HasGas from) {
			return (nearby(from.relativeTo(tile)) instanceof HasGas other && !(other instanceof GasJunctionBuild)) ? other : this;
		}

		@Override public Seq<HasGas> nextBuilds() {
			return Seq.with();
		}

		@Override public boolean outputsGas(HasGas to, float amount) {
			return false;
		}

		@Override
		public void updateGas() {
			gas.setAmount(0);
			// has to be based on proximity cause nextBuilds will return an empty seq
			Seq<HasGas> next = proximity.select(b -> b instanceof HasGas).as();
			for(HasGas other : next) gas.addAmount(other.gas().amount);
			gas.amount /= next.size;
			super.updateGas();
		}
	}
}
