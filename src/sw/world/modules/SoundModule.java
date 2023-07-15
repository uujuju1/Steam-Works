package sw.world.modules;

import arc.util.io.*;
import mindustry.world.modules.*;
import sw.world.graph.*;

public class SoundModule extends BlockModule {
	public SoundGraph graph = new SoundGraph();

	public float amount;

	@Override
	public void write(Writes write) {
		write.f(amount);
	}

	@Override
	public void read(Reads read) {
		amount = read.f();
	}
}
