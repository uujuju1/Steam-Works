package sw.world.blocks.environment;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.graphics.*;
import mindustry.world.*;

public class SWCliff extends Block {
	public float colorMultiplier = 1.5f;
	public boolean useMapColor = true;
	public TextureRegion[] cliffs;

	public static CliffHelper helper;

	public SWCliff(String name){
		super(name);
		breakable = alwaysReplace = false;
		solid = true;
		saveData = true;
		cacheLayer = CacheLayer.walls;
		fillsTile = false;
		hasShadow = false;

		if (helper == null) helper = new CliffHelper(name + "-helper");
	}

	public static void processCliffs() {
		Vars.world.tiles.eachTile(tile -> {
			if (tile.block() instanceof SWCliff && tile.data == 0) {
				for(int i = 0; i < 4; i++) {
					if (tile.nearby(i).block() instanceof CliffHelper) tile.data = (byte) (i + 1);
				}
				if (tile.data == 0) for(int i = 0; i < 4; i++) {
					if (tile.nearby(Geometry.d8edge(i)).block() instanceof CliffHelper) tile.data = (byte) (i + 5);
				}
				for(int i = 0; i < 4; i++) {
					if (
						tile.nearby(i).block() instanceof CliffHelper &&
							tile.nearby((i + 1)% 4).block() instanceof CliffHelper
					) tile.data = (byte) (i + 9);
				}
				if (tile.data == 0) tile.setBlock(Blocks.air);
			}
		});
		Vars.world.tiles.eachTile(tile -> {
			if (tile.block() instanceof CliffHelper) mindustry.gen.Call.setTile(tile, Blocks.air, Team.derelict, 0);
		});
	}
	public static void unProcessCliffs() {
		Vars.world.tiles.eachTile(tile -> {
			if (tile.block() instanceof SWCliff && tile.data != 0) {
				if (tile.data <= 4) {
					tile.nearby(tile.data - 1).setBlock(helper);
				} else if (tile.data <= 8) {
					tile.nearby(Geometry.d8edge(tile.data - 5)).setBlock(helper);
				} else {
					tile.nearby(Geometry.d4(tile.data - 9)).setBlock(helper);
					tile.nearby(Geometry.d4(tile.data - 8)).setBlock(helper);
				}
				tile.data = 0;
			}
		});
	}

	@Override
	public void drawBase(Tile tile) {
		if (tile.data == 0) {
			Draw.color();
			Draw.rect(region, tile.drawx(), tile.drawy());
		} else {
			if (useMapColor) Draw.color(Tmp.c1.set(tile.floor().mapColor).mul(colorMultiplier));
			Draw.rect(cliffs[tile.data - 1], tile.drawx(), tile.drawy());
		}
		Draw.color();
	}

	@Override
	public void load() {
		super.load();
		cliffs = new TextureRegion[12];
		for(int i = 0; i < 12; i++) {
			cliffs[i] = Core.atlas.find(name + "-" + (i + 1), "sw-cliff-" + (i + 1));
		}
	}

	@Override
	public int minimapColor(Tile tile){
		return Tmp.c1.set(tile.floor().mapColor).mul(1.2f).rgba();
	}
}