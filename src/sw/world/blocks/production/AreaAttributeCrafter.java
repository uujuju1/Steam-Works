package sw.world.blocks.production;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import arc.util.pooling.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class AreaAttributeCrafter extends SWGenericCrafter {
	public Rect areaRect;

	public boolean requireVisible;

	protected int updateEfficiencyTimer = timers++;
	protected float updateEfficiencyTime = 60f;

	public AreaAttributeCrafter(String name) {
		super(name);
	}

	@Override
	public boolean canPlaceOn(Tile tile, Team team, int rotation){
		return baseEfficiency + sumAttribute(attribute, tile.x, tile.y) >= minEfficiency;
	}

	@Override
	public void drawPlace(int x, int y, int rotation, boolean valid) {
		drawPotentialLinks(x, y);
		drawOverlay(x * tilesize + offset, y * tilesize + offset, rotation);

		if(!displayEfficiency) return;

		drawPlaceText(Core.bundle.format("bar.efficiency",
			(int)((baseEfficiency + Math.min(maxBoost, boostScale * sumAttribute(attribute, x, y))) * 100f)), x, y, valid);
	}

	@Override
	public float drawPlaceText(String text, int x, int y, Color color, boolean drawLine){
		if(renderer.pixelate) return 0;

		Font font = Fonts.outline;
		GlyphLayout layout = Pools.obtain(GlyphLayout.class, GlyphLayout::new);
		boolean ints = font.usesIntegerPositions();
		font.setUseIntegerPositions(false);
		font.getData().setScale(1f / 4f / Scl.scl(1f));
		layout.setText(font, text);

		float width = layout.width;

		font.setColor(color);
		float dx = (x + areaRect.x) * tilesize + offset, dy = (y + areaRect.y) * tilesize + offset + areaRect.height * tilesize / 2f + 3;
		font.draw(text, dx, dy + layout.height + 1, Align.center);
		dy -= 1f;
		if(drawLine){
			Lines.stroke(2f, Color.darkGray);
			Lines.line(dx - layout.width / 2f - 2f, dy, dx + layout.width / 2f + 1.5f, dy);
			Lines.stroke(1f, color);
			Lines.line(dx - layout.width / 2f - 2f, dy, dx + layout.width / 2f + 1.5f, dy);
		}

		font.setUseIntegerPositions(ints);
		font.setColor(Color.white);
		font.getData().setScale(1f);
		Draw.reset();
		Pools.free(layout);

		return width;
	}

	@Override
	public void drawOverlay(float x, float y, int rotation) {
		if(outputLiquids != null){
			for(int i = 0; i < outputLiquids.length; i++){
				int dir = liquidOutputDirections.length > i ? liquidOutputDirections[i] : -1;

				float scale = 8f / (Math.max(outputLiquids[i].liquid.fullIcon.width, outputLiquids[i].liquid.fullIcon.height) / 4f);

				if(dir != -1){
					Draw.scl(scale);
					Draw.rect(
						outputLiquids[i].liquid.fullIcon,
						x + Geometry.d4x(dir + rotation) * (size * tilesize / 2f + 4),
						y + Geometry.d4y(dir + rotation) * (size * tilesize / 2f + 4)
					);
					Draw.scl();
				}
			}
		}

		if (areaRect.x == 0 && areaRect.y == 0 && areaRect.width == size && areaRect.height == size) return;

		Drawf.dashRect(
			Pal.accent,
			x + (-areaRect.width / 2f + areaRect.x) * Vars.tilesize,
			y + (-areaRect.height / 2f + areaRect.y) * Vars.tilesize,
			areaRect.width * Vars.tilesize,
			areaRect.height * Vars.tilesize
		);
	}

	@Override
	public void init() {
		super.init();

		if (areaRect == null) areaRect = new Rect(0, 0, size, size);
	}

	@Override
	public void setStats(){
		super.setStats();

		stats.remove(baseEfficiency <= 0.0001f ? Stat.tiles : Stat.affinities);
		stats.add(baseEfficiency <= 0.0001f ? Stat.tiles : Stat.affinities, attribute, floating, boostScale * areaRect.area(), !displayEfficiency);
	}

	@Override
	public float sumAttribute(Attribute attr, int x, int y) {
		int offsetX = x - ((int) areaRect.width - 1) / 2 + ((int) areaRect.x), offsetY = y - ((int) areaRect.height - 1) / 2 + ((int) areaRect.y);

		float sum = 0;

		for (int i = offsetX; i < offsetX + areaRect.width; i++) {
			for (int j = offsetY; j < offsetY + areaRect.height; j++) {
				Tile other = Vars.world.tile(i, j);

				if (other != null && (!requireVisible || other.block() == Blocks.air || other.block() == this)) {
					sum += other.floor().attributes.get(attr);
				}
			}
		}

		return sum;
	}

	public class AreaAttributeCrafterBuild extends SWGenericCrafterBuild {
		@Override
		public void updateTile() {
			if (timer(updateEfficiencyTimer, updateEfficiencyTime)) attrsum = sumAttribute(attribute, tileX(), tileY());
			super.updateTile();
		}
	}
}
