package sw.world.modules;

import arc.struct.*;
import arc.util.io.*;
import mindustry.world.modules.*;
import sw.world.graph.*;
import sw.world.graph.ForceGraph.*;

public class ForceModule extends BlockModule {
	public ForceGraph graph = new ForceGraph();
	public final Seq<ForceLink> links = new Seq<>();

	public float speed;
	public float ratio = 1;

	public int link = -1;

	@Override
	public void write(Writes write) {
		links.remove(forceLink -> forceLink.l1() == null || forceLink.l2() == null);
		write.f(speed);
		write.f(ratio);
		write.i(link);
		write.s(links.size);
		for (ForceLink forceLink : links) {
			write.i(forceLink.l1);
			write.i(forceLink.l2);
		}
	}
	@Override
	public void read(Reads read) {
		speed = read.f();
		ratio = read.f();
		link = read.i();
		short size = read.s();
		for (int i = 0; i < size; i++) {
			ForceLink forceLink = new ForceLink(read.i(), read.i());
			links.add(forceLink);
			graph.links.addUnique(forceLink);
		}
		graph.addGraph();
	}
}
