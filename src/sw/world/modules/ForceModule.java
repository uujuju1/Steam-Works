package sw.world.modules;

import arc.struct.*;
import arc.util.io.*;
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
		write.f(speed);
		write.f(resistance);
		write.f(strength);
		write.i(link);
	}
	@Override
	public void read(Reads read) {
		speed = read.f();
		resistance = read.f();
		strength = read.f();
		link = read.i();
	}
}
