package sw.world.meta;

import arc.*;
import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
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
	public boolean hasGas = true;

	/**
	 * Capacity of gas inside a block.
	 */
	public float gasCapacity = 8f;

	/**
	 * Maximum pressure that the block can handle.
	 */
	public float maxPressure = 32f;

	/**
	 * Damage per tick applied to over pressurized builds.
	 */
	public float overpressureDamage = 0.1f;

	/**
	 * List of possible positions whene a building can connect to. If null, everywhere is allowed.
	 */
	public Seq<Point2> connections = new Seq<>();

	public Color barColor = Pal.lancerLaser, barColorTo = Color.white;

	public void addBars(Block block) {
		if (!hasGas) return;
		block.addBar("gas", building -> {
			HasGas b = building.as();
			return new Bar(
				() -> Core.bundle.get("bar.sw-gas", "gas") + ": " + Strings.fixed(b.getGas(), 2),
				() -> Tmp.c1.set(barColor).lerp(barColorTo, Mathf.clamp(b.getGas()/gasCapacity)),
				() -> Mathf.clamp(b.getGas()/gasCapacity)
			);
		});
	}
	public void addStats(Stats stats) {
		if (!hasGas) return;
		stats.add(SWStat.gasCapacity, Strings.fixed(gasCapacity, 2), SWStat.gasUnit);
		stats.add(SWStat.maxPressure, Strings.fixed(maxPressure, 2), SWStat.pressureUnit);
	}
}
