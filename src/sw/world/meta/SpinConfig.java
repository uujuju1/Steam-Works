package sw.world.meta;

import arc.*;
import arc.graphics.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import sw.math.*;
import sw.world.interfaces.*;

public class SpinConfig {
	public boolean hasSpin = true;

	public float resistance = 0;

	/**
	 * List of possible positions whene a building can connect to from 0 to 3 based on rotation.
	 */
	public Seq<Point2>[] connections = new Seq[]{
		BlockGeometry.full1,
		BlockGeometry.full1,
		BlockGeometry.full1,
		BlockGeometry.full1
	};

	public Color barColor = Pal.lancerLaser;

	public void addBars(Block block) {
		if (!hasSpin) return;
		block.addBar("gas", building -> {
			HasSpin b = building.as();
			return new Bar(
				() -> Core.bundle.get("bar.sw-rotation", "Speed") + ": " + Strings.autoFixed(b.spinGraph().speed, 2),
				() -> barColor,
				() -> 1
			);
		});
	}
	public void addStats(Stats stats) {
		if (!hasSpin) return;
//		stats.add(SWStat.gasCapacity, Strings.fixed(gasCapacity, 2), SWStat.gasUnit);
//		stats.add(SWStat.maxPressure, Strings.fixed(maxPressure, 2), SWStat.pressureUnit);
	}
}
