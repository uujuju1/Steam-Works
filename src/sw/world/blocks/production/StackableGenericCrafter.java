package sw.world.blocks.production;

import arc.math.*;
import arc.math.geom.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;
import mindustry.world.meta.*;

public class StackableGenericCrafter extends GenericCrafter {
	public Effect updateEffectStatic = Fx.none;

	public boolean[] connectEdge = new boolean[]{true, true, true, true};

	public float boost = 0.25f;

	public StackableGenericCrafter(String name) {
		super(name);
	}

	@Override
	public void setStats() {
		super.setStats();

		stats.add(Stat.boostEffect, "stat.sw-boostperbuild.format", boost * 100f);
	}

	public class StackableGenericCrafterBuild extends GenericCrafterBuild {
		@Override
		public float edelta() {
			return super.edelta() * getEfficiency();
		}

		@Override
		public float efficiencyScale() {
			return getEfficiency();
		}

		public float getEfficiency() {
			Point2[] edges = Edges.getEdges(size);
			int offset = Mathf.floor((size - 1f)/2f);

			float eff = 1;
			for(int i = 0; i < 4; i++) {
				Building other = null;

				if (connectEdge[i]) {
					for (int pos = 0; pos < size; pos++) {
						Building nearby = nearby(
							edges[Mathf.mod(pos - offset + i * size, edges.length)].x,
							edges[Mathf.mod(pos - offset + i * size, edges.length)].y
						);

						if (pos == 0) {
							other = nearby;

							if (other == null) break;
						} else if (nearby == other) {
							if (pos == size - 1) eff += boost;
						} else break;
					}
				}
			}
			return eff;
		}

		@Override
		public void updateTile() {
			if (efficiency > 0) {

				progress += getProgressIncrease(craftTime);
				warmup = Mathf.approachDelta(warmup, warmupTarget(), warmupSpeed);

				//continuously output based on efficiency
				if (outputLiquids != null) {
					float inc = getProgressIncrease(1f);
					for (var output : outputLiquids) {
						handleLiquid(this, output.liquid, Math.min(output.amount * inc, liquidCapacity - liquids.get(output.liquid)));
					}
				}

				if (wasVisible && Mathf.chanceDelta(updateEffectChance)) {
					updateEffect.at(x + Mathf.range(size * 4f), y + Mathf.range(size * 4));
					updateEffectStatic.at(x, y);
				}
			} else {
				warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
			}

			totalProgress += warmup * edelta();

			if (progress >= 1f) craft();

			dumpOutputs();
		}
	}
}
