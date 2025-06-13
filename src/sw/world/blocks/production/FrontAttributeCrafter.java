package sw.world.blocks.production;

import arc.math.geom.*;
import mindustry.game.*;
import mindustry.world.*;
import mindustry.world.meta.*;

public class FrontAttributeCrafter extends SWGenericCrafter {
	public Attribute frontAttribute = Attribute.heat;
	
	public FrontAttributeCrafter(String name) {
		super(name);
	}
	
	@Override
	public boolean canPlaceOn(Tile tile, Team team, int rotation) {
		boolean efficiency = baseEfficiency + sumAttribute(attribute, tile.x, tile.y) >= minEfficiency &&
		baseEfficiency + sumAttribute(frontAttribute, tile.x + Geometry.d4x(rotation) * size, tile.y + Geometry.d4y(rotation) * size) >= minEfficiency;
		return efficiency || !hasAttribute;
	}
	
	public class FrontAttributeCrafterBuild extends SWGenericCrafterBuild {
		@Override
		public void onProximityUpdate() {
			super.onProximityUpdate();
			
			attrsum = sumAttribute(attribute, tile.x, tile.y) + sumAttribute(frontAttribute, tile.x + Geometry.d4x(rotation) * size, tile.y + Geometry.d4y(rotation) * size);
		}
	}
}
