package sw.world.blocks.production;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import mindustry.game.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;

import static mindustry.Vars.*;

public class AreaDrill extends Drill {
	public Rect mineRect = new Rect();

	public AreaDrill(String name) {
		super(name);
	}

	@Override public boolean canMine(Tile tile) {
		return super.canMine(tile) && tile.build == null;
	}

	@Override
	public boolean canPlaceOn(Tile tile, Team team, int rotation) {
		for (int i = 0; i < mineRect.width; i++) {
			for (int j = 0; j < mineRect.height; j++) {
				Tile nearby = tile.nearby(
					(int) (mineRect.x + i - mineRect.width/2 + 1),
					(int) (mineRect.y + j - mineRect.height/2 + 1)
				);
				if (nearby != null && canMine(nearby)) return true;
			}
		}
		return false;
	}

	@Override
	protected void countOre(Tile tile){
		returnItem = null;
		returnCount = 0;

		oreCount.clear();
		itemArray.clear();

		for (int i = 0; i < mineRect.width; i++) {
			for (int j = 0; j < mineRect.height; j++) {
				Tile nearby = tile.nearby(
					(int) (mineRect.x + i - mineRect.width/2 + 1),
					(int) (mineRect.y + j - mineRect.height/2 + 1)
				);
				if (nearby != null && !tile.getLinkedTilesAs(this, tempTiles).contains(nearby)) {
					if(canMine(nearby) && nearby.build == null){
						oreCount.increment(getDrop(nearby), 0, 1);
					}
				}
			}
		}

		for(Item item : oreCount.keys()){
			itemArray.add(item);
		}

		itemArray.sort((item1, item2) -> {
			int type = Boolean.compare(!item1.lowPriority, !item2.lowPriority);
			if(type != 0) return type;
			int amounts = Integer.compare(oreCount.get(item1, 0), oreCount.get(item2, 0));
			if(amounts != 0) return amounts;
			return Integer.compare(item1.id, item2.id);
		});

		if(itemArray.size == 0){
			return;
		}

		returnItem = itemArray.peek();
		returnCount = oreCount.get(itemArray.peek(), 0);
	}

	@Override
	public void drawOverlay(float x, float y, int rotation) {
		Draw.alpha(0.5f);
		Drawf.dashRect(
			Pal.accent,
			x + (mineRect.x - mineRect.width/2f) * tilesize,
			y + (mineRect.y - mineRect.height/2f) * tilesize,
			mineRect.width * tilesize,
			mineRect.height * tilesize
		);
	}

	public class AreaDrillBuild extends DrillBuild {
		@Override
		public void drawSelect() {
			super.drawSelect();
			drawOverlay(x, y, rotation);
		}

		@Override
		public void updateTile(){
			countOre(tile);
			if(timer(timerDump, dumpTime)) dump(dominantItem != null && items.has(dominantItem) ? dominantItem : null);

			if(dominantItem == null) {
				lastDrillSpeed = 0f;
				warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
				return;
			}

			timeDrilled += warmup * delta();

			float delay = getDrillTime(dominantItem);

			if(items.total() < itemCapacity && dominantItems > 0 && efficiency > 0){
				float speed = Mathf.lerp(1f, liquidBoostIntensity, optionalEfficiency) * efficiency;

				lastDrillSpeed = (speed * dominantItems * warmup) / delay;
				warmup = Mathf.approachDelta(warmup, speed, warmupSpeed);
				progress += delta() * dominantItems * speed * warmup;

				if(Mathf.chanceDelta(updateEffectChance * warmup)) updateEffect.at(x + Mathf.range(size * 2f), y + Mathf.range(size * 2f));
			}else{
				lastDrillSpeed = 0f;
				warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
				return;
			}

			if(dominantItems > 0 && progress >= delay && items.total() < itemCapacity){
				offload(dominantItem);
				progress %= delay;
				if(wasVisible && Mathf.chanceDelta(updateEffectChance * warmup)) drillEffect.at(x + Mathf.range(drillEffectRnd), y + Mathf.range(drillEffectRnd), dominantItem.color);
			}
		}
	}
}
