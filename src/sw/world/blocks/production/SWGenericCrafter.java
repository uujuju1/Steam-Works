package sw.world.blocks.production;

import arc.audio.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.world.blocks.production.*;
import sw.world.consumers.*;
import sw.world.graph.*;
import sw.world.interfaces.*;
import sw.world.meta.*;
import sw.world.modules.*;

public class SWGenericCrafter extends GenericCrafter {
	public GasConfig gasConfig = new GasConfig();

	public float outputGas = -1;
	public boolean outputGasContinuous = false;

	public Sound craftSound = Sounds.none;
	public float craftSoundVolume = 1f;

	public SWGenericCrafter(String name) {
		super(name);
	}

	public void consumeGas(float gas, float minP, float maxP, float minE, float maxE, boolean cont) {
		consumeGas(new ConsumeGas() {{
			amount = gas;
			minPressure = minP;
			maxPressure = maxP;
			minEfficiency = minE;
			maxEfficiency = maxE;
			continuous = cont;
		}});
	}
	public void consumeGas(ConsumeGas consumer) {
		consume(consumer);
	}

	@Override
	public void setBars() {
		super.setBars();
		gasConfig.addBars(this);
	}

	@Override
	public void setStats() {
		super.setStats();
		gasConfig.addStats(stats);
		if (outputGas > 0) stats.add(SWStat.outputGas, Strings.fixed(outputGas * (outputGasContinuous ? 60f : 1f), 2), outputGasContinuous ? SWStat.gasSecond : SWStat.gasUnit);
	}

	public class SWGenericCrafterBuild extends GenericCrafterBuild implements HasGas {
		public GasModule gas = new GasModule();

		@Override
		public void craft() {
			super.craft();
			craftSound.at(x, y, 1f, craftSoundVolume);
			if (!outputGasContinuous) {
				gas().addAmount(outputGas);
			}
		}

		@Override public GasModule gas() {
			return gas;
		}
		@Override public GasConfig gasConfig() {
			return gasConfig;
		}

		@Override
		public void read(Reads read, byte revision) {
			super.read(read, revision);
			gas.read(read);
		}

		@Override
		public boolean shouldConsume() {
			return super.shouldConsume() && (outputGas <= 0 || getGas() <= gasConfig.gasCapacity);
		}

		@Override
		public void onProximityUpdate() {
			super.onProximityUpdate();

			new GasGraph().addBuild(this);
			nextBuilds().each(build -> gasGraph().merge(build.gasGraph(), false));
		}

		@Override
		public void onProximityRemoved() {
			super.onProximityRemoved();
			gasGraph().remove(this, true);
		}

		@Override
		public void updateTile() {
			updateGas();
			super.updateTile();
			if (efficiency > 0 && outputGasContinuous) {
				gas.addAmount(outputGas * Time.delta);
			}
		}

		@Override
		public void write(Writes write) {
			super.write(write);
			gas.write(write);
		}
	}
}
