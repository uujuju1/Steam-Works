package sw.world.blocks.production;

import arc.func.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class WallGenericCrafter extends WallCrafter {
	public ItemStack[] outputItems = new ItemStack[]{};
	public LiquidStack[] outputLiquids = new LiquidStack[]{};

	public WallGenericCrafter(String name) {
		super(name);
	}

	float getEfficiency(int tx, int ty, int rotation, @Nullable Cons<Tile> ctile, @Nullable Intc2 cpos){
		float eff = 0f;
		int cornerX = tx - (size-1)/2, cornerY = ty - (size-1)/2, s = size;

		for(int i = 0; i < size; i++){
			int rx = 0, ry = 0;

			switch(rotation){
				case 0 -> {
					rx = cornerX + s;
					ry = cornerY + i;
				}
				case 1 -> {
					rx = cornerX + i;
					ry = cornerY + s;
				}
				case 2 -> {
					rx = cornerX - 1;
					ry = cornerY + i;
				}
				case 3 -> {
					rx = cornerX + i;
					ry = cornerY - 1;
				}
			}

			if(cpos != null){
				cpos.get(rx, ry);
			}

			Tile other = world.tile(rx, ry);
			if(other != null && other.solid()){
				float at = other.block().attributes.get(attribute);
				eff += at;
				if(at > 0 && ctile != null){
					ctile.get(other);
				}
			}
		}
		return eff;
	}

	@Override
	public void setStats() {
		super.setStats();
		stats.remove(Stat.output);
		if(outputItems != null) stats.add(Stat.output, StatValues.items(60f / drillTime * size, outputItems));
		if(outputLiquids != null) stats.add(Stat.output, StatValues.liquids(1f, outputLiquids));
	}

	public class WallGenericCrafterBuild extends WallCrafterBuild {
		@Override
		public void updateTile() {
			boolean cons = shouldConsume();

			warmup = Mathf.approachDelta(warmup, Mathf.num(efficiency > 0), 1f / 40f);
			float dx = Geometry.d4x(rotation) * 0.5f, dy = Geometry.d4y(rotation) * 0.5f;

			float eff = getEfficiency(tile.x, tile.y, rotation, dest -> {
				if(wasVisible && cons && time >= drillTime){
					updateEffect.at(
						dest.worldx() + Mathf.range(3f) - dx * tilesize,
						dest.worldy() + Mathf.range(3f) - dy * tilesize,
						dest.block().mapColor
					);
				}
			}, null);

			lastEfficiency = eff * timeScale * efficiency;

			if (cons) for (LiquidStack liquidStack : outputLiquids) {
				liquids.add(liquidStack.liquid, liquidStack.amount * edelta());
			}

			if(cons && (time += edelta() * eff) >= drillTime){
				for (ItemStack itemStack : outputItems) items.add(itemStack.item, itemStack.amount);
				time %= drillTime;
			}

			totalTime += edelta() * warmup;

			if(timer(timerDump, dumpTime)) dump();
			for (LiquidStack liquidStack : outputLiquids) dumpLiquid(liquidStack.liquid);
		}
	}
}
