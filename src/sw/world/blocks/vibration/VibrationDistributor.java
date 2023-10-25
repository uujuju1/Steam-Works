package sw.world.blocks.vibration;

import arc.struct.*;
import sw.world.interfaces.*;

public class VibrationDistributor extends VibrationWire {
	public VibrationDistributor(String name) {
		super(name);
	}

	public class VibrationDistributorBuild extends VibrationWireBuild {
		@Override
		public Seq<HasVibration> getVibrationLinks() {
			Seq<HasVibration> links = new Seq<>();
			for (HasVibration build : proximity.copy().removeAll(b -> !(b instanceof HasVibration)).map(b -> (HasVibration) b)) links.add(build);
			return links;
		}
	}
}
