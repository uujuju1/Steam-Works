package sw.world.blocks.environment;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import sw.*;
import sw.content.*;
import sw.world.*;

import static mindustry.Vars.*;

// TODO silly class name
public class GasVent extends Floor {
	public Liquid fluid = Liquids.water;
	public int range = 10;

	public Block parent = Blocks.stone;
	public Effect cloudEffect = SWFx.gasVent;
	public Color cloudEffectColor = fluid.color;
	public float cloudEffectSpacing = 10f;

	public static final Point2[] offsets = {
		new Point2(0, 0),
		new Point2(1, 0),
		new Point2(1, 1),
		new Point2(0, 1),
		new Point2(-1, 1),
		new Point2(-1, 0),
		new Point2(-1, -1),
		new Point2(0, -1),
		new Point2(1, -1),
	};

	static{
		for(var p : offsets){
			p.sub(1, 1);
		}
	}

	public GasVent(String name) {
		super(name);
		variants = 0;
	}

	@Override
	public void drawBase(Tile tile) {
		parent.drawBase(tile);
		if (checkAdjacent(tile)) {
			Mathf.rand.setSeed(tile.pos());
			Draw.rect(variantRegions[Mathf.randomSeed(tile.pos(), 0, Math.max(0, variantRegions.length - 1))], tile.worldx() - tilesize, tile.worldy() - tilesize);
		}
	}

	@Override public boolean updateRender(Tile tile) {
		return checkAdjacent(tile);
	}

	@Override
	public void renderUpdate(UpdateRenderState state) {
		FluidArea area = new FluidArea(state.tile.x, state.tile.y, range, fluid);
		SWVars.fluidAreas.remove(area);

		if(state.tile.nearby(-1, -1).block() == Blocks.air && (state.data += Time.delta) >= cloudEffectSpacing){
			SWVars.fluidAreas.addUnique(area);
			cloudEffect.at(state.tile.x * tilesize - tilesize, state.tile.y * tilesize - tilesize, cloudEffectColor);
			state.data = 0f;
		}
	}

	public boolean checkAdjacent(Tile tile) {
		for (Point2 offset : offsets) {
			Tile other = tile.nearby(offset);
			if (other == null || other.floor() != this) return false;
		}
		return true;
	}
}
