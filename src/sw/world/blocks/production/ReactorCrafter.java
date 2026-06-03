package sw.world.blocks.production;

import arc.math.*;
import arc.util.io.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.consumers.*;

/**
 * Converts one item and one fluid into another item and another fluid, with some extras.
 * <p>If the output item is full, the reactor stalls gradually.
 * <p>If the output fluid is full, the reactor blows up violently.
 * <p>If the input fluid is lacking, the reactor melts.
 * <p>Other kinds of consumers can be added as well, but they will function as the input item and when they are not met, the reactor just stops working instantly.
 */
public class ReactorCrafter extends SWGenericCrafter {
	public Liquid meltLiquid;
	public float puddleAmount = 1;
	public float puddleChance = 1;
	public float meltDamage = 1;

	public float blowUpDamage = 1;
	public float blowUpDamageRadius = -1;
	public float unstableTime = 60f;
	public Effect blowUpEffect = Fx.none;

	private Consume[] liquidConsume;

	public ReactorCrafter(String name) {
		super(name);

		ignoreLiquidFullness = true;
	}

	@Override
	public void init() {
		super.init();

		liquidConsume = consumeBuilder.select(consume -> consume instanceof ConsumeLiquid || consume instanceof ConsumeLiquids).toArray(Consume.class);
	}

	@Override
	public void setBars() {
		super.setBars();

		addBar("sw-instability", (ReactorCrafterBuild b) -> new Bar("bar.heat", Pal.lightOrange, () -> b.instability));
	}

	public class ReactorCrafterBuild extends SWGenericCrafterBuild {
		public float instability;

		public void blowUp() {
			if (blowUpDamageRadius > 0) Damage.damage(x, y, blowUpDamageRadius, blowUpDamage);
			blowUpEffect.at(x, y);
			kill();

		}

		@Override
		public float efficiencyMultiplier() {
			float mul = 1f;
			if (outputItems != null) for (ItemStack stack : outputItems) {
				mul *= (float) (itemCapacity - items.get(stack.item)) / itemCapacity;
			}
			return mul * super.efficiencyMultiplier();
		}

		public void melt() {
			if (meltLiquid != null) {
				tile.getLinkedTilesAs(block, tile -> {
					if (Mathf.chance(puddleChance)) Puddles.deposit(tile, meltLiquid, puddleAmount);
				});
			}
			damageContinuous(meltDamage);
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);

			instability = read.f();
		}

		@Override
		public void updateTile() {
			super.updateTile();

			boolean unstable = false;
			if (outputLiquids != null) for (LiquidStack stack : outputLiquids) {
				if (liquids.get(stack.liquid) >= liquidCapacity - 0.001f) unstable = true;
			}

			if (unstable) {
				instability += 1f / unstableTime * delta();
				if (instability > 1) blowUp();
			} else {
				instability = Mathf.approachDelta(instability, 0f, 1f / unstableTime);
			}

			if (liquidConsume != null && efficiency > 0) for (Consume cons : liquidConsume) {
				if (
					(cons instanceof ConsumeLiquid liquidCons && liquids.get(liquidCons.liquid) < 0.001f)
				) melt();
				if (cons instanceof ConsumeLiquids liquidCons) for (LiquidStack stack : liquidCons.liquids) {
					if (liquids.get(stack.liquid) < 0.001f) melt();
				}
			}
		}

		@Override
		public void write(Writes write) {
			super.write(write);

			write.f(instability);
		}
	}
}
