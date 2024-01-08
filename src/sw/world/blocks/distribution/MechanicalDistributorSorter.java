package sw.world.blocks.distribution;

import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.blocks.distribution.*;

public class MechanicalDistributorSorter extends Sorter {
	public MechanicalDistributorSorter(String name) {
		super(name);
	}

	public class MechanicalDistributorSorterBuild extends SorterBuild {
		@Override
		public Building getTileTarget(Item item, Building source, boolean flip) {
			if (sortItem == null) {
				for (int i = 0; i < 4; i++) {
					rotation = (rotation + 1) % 4;
					if (front() != null && front().acceptItem(this, item)) return front();
				}
			}
			return super.getTileTarget(item, source, flip);
		}
	}
}
