package sw.world.modules;

import arc.util.io.*;
import mindustry.world.modules.*;
import sw.world.graph.*;

/**
 * A class holding a graph.
 * @apiNote Only put stuff here if it is local to a building, for things that are synced across the graph, put them inside the graph
 */
public class GasModule extends BlockModule {
	public float amount = 0;

	public GasGraph graph = new GasGraph();

	public void addAmount(float value) {
		amount += value;
	}

	@Override
	public void read(Reads read) {
		amount = read.f();
	}

	public void setAmount(float value) {
		amount = value;
	}

	public void subAmount(float value) {
		amount -= value;
	}

	@Override
	public void write(Writes write) {
		write.f(amount);
	}
}
