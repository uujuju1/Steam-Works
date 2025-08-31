package sw.world.meta;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import sw.world.interfaces.*;

public class SpinConfig {
	public boolean hasSpin = true;

	/**
	 * Resistance of rotation speed in rpm/10/sec.
	 */
	public float resistance = 0;
	/**
	 * Extra moment of inertia added to the system.
	 */
	public float inertia = 0;

	/**
	 * Visual max speed, always full if zero. In (rpm/10).
	 * TODO new animation for the bar
	 */
	@Deprecated public float topSpeed;

	/**
	 * List of edges that will connect. One list per rotation. If null, every edge is allowed.
	 */
	public int[][] allowedEdges;
	
	/**
	 * List of blocks that this block will not connect to.
	 * If {@link #connectorAllowList} is true, this block will only connect to the blocks inside the list.
	 */
	public Seq<Block> connectors = new Seq<>();
	public boolean connectorAllowList = false;

	public Color barColor = Pal.accent;

	public void addBars(Block block) {
		if (!hasSpin) return;
		block.addBar("sw-spin", building -> {
			HasSpin b = building.as();
			return new Bar(
				() -> Core.bundle.format("bar.sw-rotation", Strings.autoFixed(b.spinGraph().speed / b.spinGraph().ratios.get(b, 1f) * 10f, 2)),
				() -> barColor,
				() -> 0f
			);
		});
	}
	public void addStats(Stats stats) {
		if (!hasSpin) return;
		if (resistance > 0) stats.add(SWStat.spinResistance, StatValues.number(resistance * 600f, SWStat.spinMinuteSecond));
	}

	public void drawPlace(Block block, int x, int y, int rotation, boolean valid) {
		if (hasSpin) {
			Point2[] edges = Edges.getEdges(block.size);
			if (allowedEdges != null) {
				for(int i : allowedEdges[rotation]) {
					Draw.rect("sw-icon-spin-edge", (x + edges[i].x) * Vars.tilesize, (y + edges[i].y) * Vars.tilesize, 4, 4, 0);
				}
			} else {
				for(Point2 i : edges) {
					Draw.rect("sw-icon-spin-edge", (x + i.x) * Vars.tilesize, (y + i.y) * Vars.tilesize, 4, 4, 0);
				}
			}
		}
	}
}
