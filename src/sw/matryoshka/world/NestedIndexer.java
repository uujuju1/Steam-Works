package sw.matryoshka.world;

import arc.func.*;
import mindustry.ai.*;
import mindustry.game.EventType.*;
import mindustry.world.*;
import sw.matryoshka.*;

public class NestedIndexer extends BlockIndexer {
	public Cons<TilePreChangeEvent> tilePreChange;
	public Cons<TileChangeEvent> tileChange;
	public Cons<TileFloorChangeEvent> tileFloorChange;
	public Cons<WorldLoadEvent> worldLoad;

	private final TilePreChangeEvent tilePreChangeEvent = new TilePreChangeEvent();
	private final TileChangeEvent tileChangeEvent = new TileChangeEvent();
	private final TileFloorChangeEvent tileFloorChangeEvent = new TileFloorChangeEvent();
	private final WorldLoadEvent worldLoadEvent = new WorldLoadEvent();

	@SuppressWarnings("unchecked")
	public NestedIndexer() {
		tilePreChange = (Cons<TilePreChangeEvent>) ExtraReflect.popEvent(TilePreChangeEvent.class);
		tileChange = (Cons<TileChangeEvent>) ExtraReflect.popEvent(TileChangeEvent.class);
		tileFloorChange = (Cons<TileFloorChangeEvent>) ExtraReflect.popEvent(TileFloorChangeEvent.class);
		worldLoad = (Cons<WorldLoadEvent>) ExtraReflect.popEvent(WorldLoadEndEvent.class);
	}

	public void tileChange(Tile tile) {
		tileChangeEvent.tile = tile;
		tileChange.get(tileChangeEvent);
	}

	public void tileFloorChange(Tile tile) {
		tileFloorChangeEvent.tile = tile;
		tileFloorChange.get(tileFloorChangeEvent);
	}

	public void tilePreChange(Tile tile) {
		tilePreChangeEvent.tile = tile;
		tilePreChange.get(tilePreChangeEvent);
	}

	public void worldLoad() {
		worldLoad.get(worldLoadEvent);
	}
}
