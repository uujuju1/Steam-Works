package sw.world.modules;

import arc.util.io.*;
import mindustry.*;
import mindustry.world.modules.*;
import sw.world.graph.*;
import sw.world.graph.FluidGraph.*;

public class FluidModule extends BlockModule {
	public FluidGraph graph = new FluidGraph();
	public FluidTank tank = new FluidTank();
	
	public float[] liquids = new float[Vars.content.liquids().size];
	
	public void addAmount(int liquid, float amount) {
		liquids[liquid] += amount;
	}
	
	@Override
	public void read(Reads read) {
		byte size = read.b();
		
		for (int i = 0; i < size; i++) {
			float value = read.f();
			if (i < Vars.content.liquids().size) {
				liquids[i] = value;
			}
		}
	}
	
	public void removeAmount(int liquid, float amount) {
		liquids[liquid] += amount;
	}
	
	public void setAmount(int liquid, float amount) {
		liquids[liquid] = amount;
	}
	
	@Override
	public void write(Writes write) {
		write.b(liquids.length);
		
		for (float value : liquids) {
			write.f(value);
		}
	}
}
