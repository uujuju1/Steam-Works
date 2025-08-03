package sw.world.modules;

import arc.util.io.*;
import mindustry.world.modules.*;
import sw.world.graph.*;

public class FluidModule extends BlockModule {
	public FluidGraph graph = new FluidGraph();
	
	@Override
	public void read(Reads read) {
		super.read(read);
	}
	
	@Override
	public void write(Writes writes) {
	
	}
}
