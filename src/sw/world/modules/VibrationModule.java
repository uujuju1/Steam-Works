package sw.world.modules;

import arc.util.io.*;
import mindustry.world.modules.*;
import sw.world.graph.*;

public class VibrationModule extends BlockModule {
	public VibrationGraph graph = new VibrationGraph();
	public int link = -1;

	@Override
	public void read(Reads read) {
		link = read.i();
	}
	@Override
	public void write(Writes write) {
		write.i(link);
	}
}
