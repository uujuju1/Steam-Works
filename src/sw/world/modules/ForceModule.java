package sw.world.modules;

import arc.struct.*;
import arc.util.io.*;
import mindustry.world.modules.*;
import sw.world.graph.*;

public class ForceModule extends BlockModule {
	public ForceGraph graph = new ForceGraph();

	public float speed;
	public float ratio = 1;

	public final Seq<Link> links = new Seq<>();
	public int link = -1;

	@Override
	public void write(Writes write) {
		links.remove(link -> link.l1() == null || link.l2() == null);
		write.f(speed);
		write.f(ratio);
		write.i(link);
		write.s(links.size);
		for (Link link: links) {
			write.i(link.l1);
			write.i(link.l2);
		}
	}
	@Override
	public void read(Reads read) {
		speed = read.f();
		ratio = read.f();
		link = read.i();
		short size = read.s();
		for (int i = 0; i < size; i++) {
			Link link = new Link(read.i(), read.i());
			links.add(link);
			graph.links.add(link);
		}
		graph.checkEntity();
	}
}
