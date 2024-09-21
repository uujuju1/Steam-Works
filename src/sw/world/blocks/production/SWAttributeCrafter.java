package sw.world.blocks.production;

import arc.*;
import arc.math.geom.*;
import mindustry.*;
import mindustry.game.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;
import mindustry.world.meta.*;

public class SWAttributeCrafter extends SWGenericCrafter {
	public Rect attributeRect = new Rect(0, 0, 2, 2);
	public Attribute attribute = Attribute.heat;
	public float baseEfficiency = 1f;
	public float boostScale = 1f;
	public float maxBoost = 1f;
	public float minEfficiency = -1f;
	public float displayEfficiencyScale = 1f;
	public boolean displayEfficiency = true;
	public boolean scaleLiquidConsumption = false;

	public SWAttributeCrafter(String name) {
		super(name);
	}

	@Override
	public boolean canPlaceOn(Tile tile, Team team, int rotation){
		//make sure there's enough efficiency at this location
		return baseEfficiency + tile.getLinkedTilesAs(this, tempTiles).sumf(other -> other.floor().attributes.get(attribute)) >= minEfficiency;
	}

	@Override
	public void drawPlace(int x, int y, int rotation, boolean valid){
		super.drawPlace(x, y, rotation, valid);

		if(!displayEfficiency) return;

		drawPlaceText(Core.bundle.format("bar.efficiency",
			(int)((baseEfficiency + Math.min(maxBoost, boostScale * sumAttribute(attribute, x, y))) * 100f)), x, y, valid);
	}

	@Override
	public void setBars(){
		super.setBars();

		if(!displayEfficiency) return;

		addBar("efficiency", (AttributeCrafter.AttributeCrafterBuild entity) ->
			                     new Bar(
				                     () -> Core.bundle.format("bar.efficiency", (int)(entity.efficiencyMultiplier() * 100 * displayEfficiencyScale)),
				                     () -> Pal.lightOrange,
				                     entity::efficiencyMultiplier));
	}

	@Override
	public void setStats(){
		super.setStats();

		stats.add(baseEfficiency <= 0.0001f ? Stat.tiles : Stat.affinities, attribute, floating, boostScale * size * size, !displayEfficiency);
	}

	@Override
	public float sumAttribute(Attribute attr, int x, int y) {
		float attrSum = 0;
		for(int i = 0; i < attributeRect.width; i++) {
			for(int j = 0; j < attributeRect.height; j++) {
				attrSum += Vars.world.tile(x, y).nearby((int) (i + attributeRect.x), (int) (j + attributeRect.y)).floor().attributes.get(attr);
			}
		}
		return attrSum;
	}

	public class SWAttributeCrafterBuild extends SWGenericCrafterBuild {
		public float attrsum;

		@Override
		public float getProgressIncrease(float base){
			return super.getProgressIncrease(base) * efficiencyMultiplier();
		}

		public float efficiencyMultiplier(){
			return baseEfficiency + Math.min(maxBoost, boostScale * attrsum) + attribute.env();
		}

		@Override
		public float efficiencyScale(){
			return scaleLiquidConsumption ? efficiencyMultiplier() * super.efficiencyScale() : super.efficiencyScale();
		}

		@Override
		public void pickedUp(){
			attrsum = 0f;
			warmup = 0f;
		}

		@Override
		public void updateTile(){
			super.updateTile();

			attrsum = sumAttribute(attribute, tile.x, tile.y);
		}
	}
}
