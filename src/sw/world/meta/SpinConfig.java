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
import sw.math.*;
import sw.world.interfaces.*;

public class SpinConfig {
	public boolean hasSpin = true;

	/**
	 * Resistance of rotation speed in rpm/10/sec.
	 */
	public float resistance = 0;

	/**
	 * Visual max speed, always full if zero. In (rpm/10).
	 */
	public float topSpeed;

	/**
	 * List of possible positions whene a building can connect to from 0 to 3 based on rotation.
	 */
	public Seq<Point2>[] connections = new Seq[]{
		BlockGeometry.full1,
		BlockGeometry.full1,
		BlockGeometry.full1,
		BlockGeometry.full1
	};

	/**
	 * List of edges that will connect. One list per rotation. If null, every edge is allowed.
	 */
	public int[][] allowedEdges;

	public Color barColor = Pal.accent;

	public void addBars(Block block) {
		if (!hasSpin) return;
		block.addBar("gas", building -> {
			HasSpin b = building.as();
			return new Bar(
				() -> Core.bundle.format("bar.sw-rotation", Strings.autoFixed(b.spinGraph().speed * b.spinSection().ratio * 10f, 2)),
				() -> barColor,
				() -> topSpeed > 0 ? Mathf.clamp((b.spinGraph().speed * b.spinSection().ratio) / topSpeed) : 1f
			);
		});
	}
	public void addStats(Stats stats) {
		if (!hasSpin) return;
		if (resistance > 0) stats.add(SWStat.spinResistance, StatValues.number(resistance * 600f, SWStat.spinMinuteSecond));
	}
}
