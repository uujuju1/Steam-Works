package sw.content;

import mindustry.ai.*;
import mindustry.gen.*;

// TODO, why is this so hardcoded, i can only have 3 custom costs, and even then, there's incompatibility issues
public class SWPathfinds {
	public final int costPrefferingEmpty;

	public SWPathfinds() {
		costPrefferingEmpty = putCost((team, tile) -> 1 +
				(PathTile.health(tile) * 5) +
				(PathTile.nearSolid(tile) ? 2 : 0) +
				(PathTile.nearLiquid(tile) ? 6 : 0) +
				(PathTile.deep(tile) ? 6000 : 0) +
				(PathTile.solid(tile) ? 100000 : 0) +
				(PathTile.damages(tile) ? 30 : 0)
		);
		ControlPathfinder.costTypes.add(getCost(costPrefferingEmpty));
	}

	public int putCost(Pathfinder.PathCost cost) {
		Pathfinder.costTypes.add(cost);
		return Pathfinder.costTypes.size - 1;
	}

	public Pathfinder.PathCost getCost(int id) {
		return Pathfinder.costTypes.get(id);
	}
}
