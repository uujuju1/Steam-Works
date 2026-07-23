package sw.matryoshka.world;

import mindustry.core.*;
import mindustry.game.EventType.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import sw.matryoshka.*;

import static mindustry.Vars.*;

public class NestedWorld extends World {
	public NestedWorld() {
		ExtraReflect.popEvent(TileChangeEvent.class);
		ExtraReflect.popEvent(TileFloorChangeEvent.class);
		ExtraReflect.popEvent(WorldLoadEndEvent.class);

		tileChanges = floorChanges = -1;
	}

	@Override
	public Tiles resize(int width, int height) {
		Tiles res = super.resize(width, height);

		for(int i = 0; i < width * height; i++) {
			res.seti(i, new NestedTile(i % width, i / width, this));
		}

		return res;
	}

	public static class NestedTile extends Tile {
		public NestedWorld world;

		public NestedTile(int x, int y, NestedWorld world) {
			super(x, y);
			this.world = world;
		}

		@Override
		protected void fireChanged() {
			// no
		}

		@Override
		protected void firePreChanged() {
			// no
		}

		public void setFloor(Floor type){
			if(this.floor == type) return;

			var prev = this.floor;
			this.floor = type;

			if(!headless && !world.isGenerating() && !isEditorTile()){
				renderer.blocks.removeFloorIndex(this);
			}

//			recache();
			if(build != null){
				build.onProximityUpdate();
			}
			// nah
//			if(!world.isGenerating() && pathfinder != null && !state.isEditor()){
//				pathfinder.updateTile(this);
//			}

			// no
//			if(!world.isGenerating()){
//				Events.fire(floorChange.set(this, prev, type));
//			}

			if(this.floor != prev){
				this.floor.floorChanged(this);
			}
		}

		public void setOverlay(Block block){
			if(this.overlay == block) return;

			var prev = this.overlay;

			this.overlay = (Floor)block;

//			recache();

//			if(!world.isGenerating()){
//				Events.fire(overlayChange.set(this, prev, this.overlay));
//			}

			if(!world.isGenerating() && build != null){
				build.onProximityUpdate();
			}
		}
	}
}
