package sw.world.modules;

import arc.struct.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.world.modules.*;
import sw.world.graph.*;

public class ForceModule extends BlockModule {
	public ForceGraph graph = new ForceGraph();

	public float speed;
	public float resistance;
	public float strength;

	public final Seq<Link> links = new Seq<>();
	public int link = -1;

	@Override
	public void write(Writes write) {
		links.remove(link -> link.l1 == null || link.l2 == null);
		write.s(links.size);
		for (int i = 0; i < links.size; i++) {
			write.i(links.get(i).l1.pos());
			write.i(links.get(i).l2.pos());
		}
		write.f(speed);
		write.f(resistance);
		write.f(strength);
		write.i(link);
	}
	@Override
	public void read(Reads read) {
		links.clear();
		short s = read.s();
		for (int i = 0; i < s; i++) links.add(new Link(Vars.world.build(read.i()), Vars.world.build(read.i())));
		links.remove(link -> link.l1 == null || link.l2 == null);
		speed = read.f();
		resistance = read.f();
		strength = read.f();
		link = read.i();
	}

	public static class Link {
		public Building l1, l2;

		public Link(Building l1, Building l2) {
			this.l1 = l1;
			this.l2 = l2;
		}

		@Override public boolean equals(Object obj) {
			return obj instanceof Link link && link.l1 == l1 && link.l2 == l2;
		}
		@Override public String toString() {
			return "link1: " + l1 + "; link2: " + l2;
		}

		public void write(Writes write) {
			write.i(l1.pos());
			write.i(l2.pos());
		}
		public void read(Reads read) {
			l1 = Vars.world.build(read.i());
			l1 = Vars.world.build(read.i());
		}
	}
}
