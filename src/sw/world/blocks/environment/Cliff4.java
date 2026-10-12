package sw.world.blocks.environment;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.world.*;
import sw.annotations.*;

public class Cliff4 extends Block {
	public Color tint = Color.white;
	public float colorMultiplier = 1.5f;
	public boolean useMapColor = true;
	public @Annotations.Load(value = "@name$-#0$", lengths = {12}) TextureRegion[] cliffs = new TextureRegion[12];
	public @Annotations.Load(value = "@name$-edge-#0$", lengths = {2}) TextureRegion[] edges = new TextureRegion[2];

	public Cliff4(String name){
		super(name);
		breakable = alwaysReplace = false;
		solid = true;
		saveData = true;
		cacheLayer = CacheLayer.walls;
		fillsTile = false;
		hasShadow = false;
	}

	public void process() {
		Vars.world.tiles.eachTile(tile -> {
			if (tile.block() == this && tile.data == 0) {
				for(int i = 0; i < 4; i++) {
					if (tile.nearby(i).block() != this) tile.data = (byte) (i + 1);
				}
				if (tile.data == 0) for(int i = 0; i < 4; i++) {
					if (tile.nearby(Geometry.d8edge(i)).block() != this) tile.data = (byte) (i + 5);
				}
				for(int i = 0; i < 4; i++) {
					if (
						tile.nearby(i).block() != this &&
						tile.nearby((i + 1) % 4).block() != this
					) tile.data = (byte) (i + 9);
				}
			}
		});

		Vars.world.tiles.eachTile(tile -> {
			if (tile.block() == this && tile.data == 0) {
				tile.setBlock(Blocks.air);
			}
		});
	}

	@Override
	public void drawBase(Tile tile) {
		if (tile.data == 0) {
			Draw.color();
			Draw.rect(fullIcon, tile.drawx(), tile.drawy());
		} else {
			Draw.color(tint);
			if (useMapColor) Draw.color(Tmp.c1.set(tile.floor().mapColor).mul(colorMultiplier).mul(tint));
			Draw.rect(cliffs[tile.data - 1], tile.drawx(), tile.drawy());

			if (tile.data <= 4) {
				Draw.rect(
					edges[Mathf.num(tile.data > 2)],
					tile.drawx() + Angles.trnsx(tile.data * 90f, Vars.tilesize),
					tile.drawy() + Angles.trnsy(tile.data * 90f, Vars.tilesize),
					tile.data * 90f
				);
				Draw.rect(
					edges[Mathf.num(tile.data > 2)],
					tile.drawx() + Angles.trnsx(tile.data * 90f + 180, Vars.tilesize),
					tile.drawy() + Angles.trnsy(tile.data * 90f + 180, Vars.tilesize),
					tile.data * 90f + 180
				);
			} else if (tile.data <= 8) {
				Draw.rect(
					edges[Mathf.num((tile.data) % 4 >= 2)],
					tile.drawx() + Angles.trnsx(tile.data * 90f - 90, Vars.tilesize),
					tile.drawy() + Angles.trnsy(tile.data * 90f - 90, Vars.tilesize),
					tile.data * 90f - 90
				);
				Draw.rect(
					edges[Mathf.num((tile.data - 1) % 4 >= 2)],
					tile.drawx() + Angles.trnsx(tile.data * 90f, Vars.tilesize),
					tile.drawy() + Angles.trnsy(tile.data * 90f, Vars.tilesize),
					tile.data * 90f
				);
			} else {
				Draw.rect(
					edges[Mathf.num((tile.data) % 4 >= 2)],
					tile.drawx() + Angles.trnsx(tile.data * 90f + 90, Vars.tilesize),
					tile.drawy() + Angles.trnsy(tile.data * 90f + 90, Vars.tilesize),
					tile.data * 90f + 90
				);
				Draw.rect(
					edges[Mathf.num((tile.data - 1) % 4 >= 2)],
					tile.drawx() + Angles.trnsx(tile.data * 90f + 180, Vars.tilesize),
					tile.drawy() + Angles.trnsy(tile.data * 90f + 180, Vars.tilesize),
					tile.data * 90f + 180
				);
			}
		}
		Draw.color();
	}

	@Override
	public int minimapColor(Tile tile){
		return Tmp.c1.set(tile.floor().mapColor).mul(1.2f).rgba();
	}
}