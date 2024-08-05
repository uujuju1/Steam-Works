package sw.world.meta;

import arc.*;
import arc.graphics.*;
import arc.math.*;
import arc.util.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import sw.world.interfaces.*;

public class GasConfig {
	/**
	 * Indicates that a block can interact with other gas system blocks.
	 */
	public boolean hasGas = false;

	/**
	 * Capacity of gas inside a block.
	 */
	public float gasCapacity = 8f;

	public Color barColor = Pal.lancerLaser, barColorTo = Pal.turretHeat;

	public void addBars(Block block) {
		block.addBar("gas", building -> {
			HasGas b = building.as();
			return new Bar(
				() -> Core.bundle.get("bar.sw-gas", "gas") + ": " + b.getGas(),
				() -> Tmp.c1.set(barColor).lerp(barColorTo, Mathf.clamp(b.getGas()/gasCapacity)),
				() -> Mathf.clamp(b.getGas()/gasCapacity)
			);
		});
	}
	public void addStats(Stats stats) {

	}
}
