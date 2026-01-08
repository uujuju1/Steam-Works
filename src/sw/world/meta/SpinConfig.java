package sw.world.meta;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import sw.ui.elements.*;
import sw.world.interfaces.*;

public class SpinConfig {
	public boolean hasSpin = true;
	
	/**
	 * When true, torque will be ignored if the graph's speed exceeds this block's target speed.
	 */
	public boolean checkSpeed = true;

	/**
	 * Resistance of rotation speed in rpm/10/sec.
	 */
	public float resistance = 0;
	/**
	 * Extra moment of inertia added to the system.
	 */
	public float inertia = 0;

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

	public void addBars(Block block) {
		if (!hasSpin) return;
		block.addBar("sw-spin", building -> {
			HasSpin b = building.as();
			return new RotationBar(
				() -> Core.bundle.format("bar.sw-rotation", Strings.autoFixed(b.getSpeed() * 10f, 2)),
				b::getRotation
			);
		});
	}
	public void addStats(Stats stats) {
		if (!hasSpin) return;
		if (resistance > 0) stats.add(SWStat.spinResistance, StatValues.number(resistance * 600f, SWStat.force));
		if (inertia > 0) stats.add(SWStat.weight, StatValues.number(inertia, SWStat.mass));
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
