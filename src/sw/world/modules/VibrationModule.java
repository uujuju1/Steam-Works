package sw.world.modules;

import arc.struct.*;
import arc.util.io.*;
import mindustry.world.modules.*;
import sw.world.graph.*;
import sw.world.graph.VibrationGraphDeprecated.*;

public class VibrationModule extends BlockModule {
	public VibrationGraphDeprecated graph = new VibrationGraphDeprecated();
	public Seq<VibrationLink> links = new Seq<>();
	public int link = -1;

	@Override
	public void read(Reads read) {
		link = read.i();
		short size = read.s();
		for (int i = 0; i < size; i++) {
			VibrationLink vlink = new VibrationLink(read.i(), read.i());
			links.add(vlink);
			graph.links.addUnique(vlink);
		}
	}

	@Override
	public void write(Writes write) {
		write.i(link);
		write.s(links.size);
		for (VibrationLink vlink : links) {
			write.i(vlink.link1);
			write.i(vlink.link2);
		}
	}
}
