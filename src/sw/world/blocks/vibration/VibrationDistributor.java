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

		@Override
		public void onProximityRemoved() {
			super.onProximityRemoved();
			vibration().links.each(link -> {
				if (link.valid()) {
					removeVibrationLink(link.other(this));
				} else {
					vibration().links.remove(link);
					vGraph().remove(this);
					vGraph().delete(link.other(this));
					vGraph().links.remove(link);
				}
			});
			vGraph().delete(this);
		}
		@Override
		public void onProximityUpdate() {
			super.onProximityUpdate();
			vibration().links.each(link -> {
				if (link.link2() != getVibrationLink() && link.valid()) removeVibrationLink(link.other(this));
				if (!link.valid()) {
					vibration().links.remove(link);
					vGraph().remove(this);
					vGraph().delete(link.other(this));
					vGraph().links.remove(link);
				}
			});
			for (HasVibration build : proximity.copy().filter(b -> b instanceof HasVibration).map(b -> (HasVibration) b)) {
				createVibrationLink(build);
			}
			if (getVibrationLink() != null) createVibrationLink(getVibrationLink());
		}
	}
}
