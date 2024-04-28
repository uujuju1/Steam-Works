package sw.world.consumers;

import mindustry.gen.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;
import sw.world.interfaces.*;
import sw.world.meta.*;

public class ConsumeTension extends Consume {
	public float min, max;
	boolean staticTension = false;

	public ConsumeTension(float min, float max) {
		this.min = min;
		this.max = max;
	}

	public HasTension cast(Building build) {
		try {
			return build.as();
		} catch (Exception e) {
			throw new IllegalArgumentException("Use an appropriate block, dummy", e);
		}
	}

	@Override public void display(Stats stats) {
		stats.add(SWStat.requiredTension, "@ to @ @", min, max, SWStat.tensionUnits.localized());
	}

	@Override public float efficiency(Building build) {
		return (min <= pull(cast(build)) && pull(cast(build)) <= max) ? 1f : 0f;
	}
	@Override public float efficiencyMultiplier(Building build) {
		return (min <= pull(cast(build)) && pull(cast(build)) <= max) ? 1f : 0f;
	}

	public float pull(HasTension build) {
		return staticTension ? build.tensionGraph().getStaticTension() : build.tensionGraph().getPull();
	}

	public Consume staticTension() {
		staticTension = true;
		return this;
	}
}
