package sw.world.blocks.environment;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import sw.graphics.*;

import java.util.*;

public class Pitfall extends Floor {
	public Color maskColor = SWPal.pitfallMask;

	public Pitfall(String name) {
		super(name);
		solid = true;
		placeableOn = false;
		canShadow = false;
		variants = 0;
		mapColor = null;
		
		drawEdgeIn = drawEdgeOut = false;
	}

	@Override
	public void createIcons(MultiPacker packer) {
		Color mapCol = mapColor;
		mapColor = new Color(0, 0, 0, 1);
		super.createIcons(packer);
		if (mapCol != null) mapColor.set(mapCol);
	}
	
	@Override
	public void drawBase(Tile tile) {
		Mathf.rand.setSeed(tile.pos());
		Draw.rect(variantRegions[variant(tile.x, tile.y)], tile.worldx(), tile.worldy());
		
		Draw.alpha(1f);
		drawOverlay(tile);
	}
	
	@Override
	public void drawNonLayer(Tile tile, CacheLayer layer) {
		Mathf.rand.setSeed(tile.pos());

		Arrays.fill(dirs, 0);
		blenders.clear();
		blended.clear();

		for(int i = 0; i < 8; i++){
			Point2 point = Geometry.d8[i];
			Tile other = tile.nearby(point);
			//special case: empty is, well, empty, so never draw emptiness on top, as that would just be an incorrect black texture
			if(other != null && other.floor().cacheLayer == layer && other.floor().cacheLayer == CacheLayer.water && other.floor() != Blocks.empty){
				if(!blended.getAndSet(other.floor().id)){
					blenders.add(other.floor());
					dirs[i] = other.floorID();
				}
			}
		}

		drawBlended(tile, false);
	}
}
