package sw.world.modules;

import arc.struct.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.world.modules.*;
import sw.world.graph.*;
import sw.world.interfaces.*;

public class ForceModule extends BlockModule {
	public ForceGraph graph = new ForceGraph();

	public float speed;
	public float resistance;
	public float torque;

	public final Seq<Link> links = new Seq<>();
	public int link = -1;

	@Override
	public void write(Writes write) {
		links.remove(link -> link.l1 == null || link.l2 == null);
		write.f(speed);
		write.f(resistance);
		write.f(torque);
		write.i(link);
		write.s(links.size);
		for (Link link: links) {
			write.i(((Building) link.l1).pos());
			write.i(((Building) link.l2).pos());
		}
	}
	@Override
	public void read(Reads read) {
		speed = read.f();
		resistance = read.f();
		torque = read.f();
		link = read.i();
		short size = read.s();
		for (int i = 0; i < size; i++) {
			Building b1 = Vars.world.build(read.i());
			Building b2 = Vars.world.build(read.i());
			if (b1 != null && b2 != null) new Link((HasForce) b1, (HasForce) b2);
		}
		graph.hasEntity();
	}
}
