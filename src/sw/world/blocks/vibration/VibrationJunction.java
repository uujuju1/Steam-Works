package sw.world.blocks.vibration;

import arc.struct.*;
import sw.world.interfaces.*;

public class VibrationJunction extends VibrationDistributor {
	public VibrationJunction(String name) {
		super(name);
		vibrationConfig.linksGraph = false;
	}

	public class VibrationJunctionBuild extends VibrationDistributorBuild {

		@Override
		public Seq<HasVibration> nextBuilds() {
			return Seq.with();
		}

		@Override
		public HasVibration getVibrationDestination(HasVibration source) {
			if(!enabled) return this;

			int dir = (source.relativeTo(tile.x, tile.y) + 4) % 4;
			HasVibration next = nearby(dir) instanceof HasVibration ? (HasVibration) nearby(dir) : null;
			if(next == null || !(next.block() instanceof VibrationJunction)){
				return this;
			}
			return next.getVibrationDestination(this);
		}
	}
}
