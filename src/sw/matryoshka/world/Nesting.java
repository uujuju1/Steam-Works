package sw.matryoshka.world;

import mindustry.*;
import mindustry.ai.*;
import mindustry.core.*;
import mindustry.gen.*;
import sw.matryoshka.entities.*;

/**
 * Standard version of a running world separate from the vanilla system. Can be updated via its context.
 */
public class Nesting {
	public World world = new World();

	public BlockIndexer indexer = new BlockIndexer();

	public NestedGroups groups = new NestedGroups();

	public float x, y;

	public Nesting(int width, int height) {
		world.resize(width, height);
		world.tiles.fill();
		groups.init();
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

			all = Groups.all;
			build = Groups.build;
			bullet = Groups.bullet;
			draw = Groups.draw;
			fire = Groups.fire;
			label = Groups.label;
			player = Groups.player;
			powerGraph = Groups.powerGraph;
			puddle = Groups.puddle;
			sync = Groups.sync;
			unit = Groups.unit;
			weather = Groups.weather;
		}};
	}
}
