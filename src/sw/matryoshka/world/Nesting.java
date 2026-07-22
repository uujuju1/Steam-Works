package sw.matryoshka.world;

import mindustry.*;
import mindustry.ai.*;
import mindustry.core.*;
import sw.matryoshka.entities.*;

/**
 * Standard version of a running world separate from the vanilla system. Can be updated via its context.
 */
public class Nesting {
	public World world = new World();

	public BlockIndexer indexer = new BlockIndexer();

	public NestedGroups groups = new NestedGroups();

	public Nesting(int width, int height) {
		world.resize(width, height);
		groups.resize(0, 0, world.unitWidth(), world.unitHeight());
	}

	/**
	 * Builds a context to update this nesting.
	 */
	public NestingContext getContext() {
		var self = this;
		return new NestingContext() {{
			origin = self;

			returnWorld = Vars.world;
		}};
	}
}
