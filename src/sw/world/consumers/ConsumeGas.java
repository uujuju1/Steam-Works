package sw.world.consumers;

import arc.*;
import arc.math.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.ui.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;
import sw.world.interfaces.*;
import sw.world.meta.*;

import static mindustry.Vars.*;

public class ConsumeGas extends Consume {
	/**
	 * Amount consumed per tick or per craft.
	 */
	public float amount = 0;

	/**
	 * Range at which this consumer work.
	 */
	public float minPressure = 0, maxPressure = 8;

	/**
	 * Efficiency mapping based on pressure amount.
	 */
	public float minEfficiency = 1f, maxEfficiency = 1f;

	/**
	 * Consume continuously per tick instead of consuming per craft.
	 */
	public boolean continuous;

	public float baseEfficiency(HasSpin build) {
		if (build.getGasPressure() < minPressure || build.getGasPressure() > maxPressure) return 0f;
		return Mathf.map(build.getGasPressure(), minPressure, maxPressure, minEfficiency, maxEfficiency);
	}

	@Override public void build(Building build, Table table) {
		table.add(new ReqImage(Core.atlas.find("sw-steam"), () -> baseEfficiency(cast(build)) > 0.0001f)).size(iconMed).top().left();
	}

	public HasSpin cast(Building build) {
		try {
			if (!((HasSpin) build).spinConfig().hasSpin) throw new RuntimeException("This block doesn't use the gas system");
			return (HasSpin) build;
		} catch (ClassCastException e) {
			throw new RuntimeException("This block cannot use the gas system!", e);
		}
	}

	@Override public void display(Stats stats) {
		stats.add(SWStat.consumeGas, Strings.fixed(amount * (continuous ? 60f : 1f), 2), continuous ? SWStat.gasSecond : SWStat.gasUnit);
	}

	@Override public float efficiency(Building build) {
		return baseEfficiency(cast(build));
	}
	@Override public float efficiencyMultiplier(Building build) {
		return baseEfficiency(cast(build));
	}

	@Override public void trigger(Building build) {
		if (!continuous) cast(build).spin().subAmount(Math.min(cast(build).getGas(), amount * build.edelta()));
	}

	@Override public void update(Building build) {
		if (continuous) cast(build).spin().subAmount(Math.min(cast(build).getGas(), amount * baseEfficiency(cast(build))));
	}
}
