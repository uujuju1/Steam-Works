package sw.world.graph;

import arc.struct.*;
import arc.util.*;
import mindustry.*;
import sw.world.interfaces.*;

public class VibrationGraph extends Graph {
	public final Seq<HasVibration> builds = new Seq<>(false, 16, HasVibration.class);
	public final Seq<VibrationLink> links = new Seq<>(false, 16, VibrationLink.class);
	public FloatSeq frequencies = new FloatSeq();
	public boolean read = false;

	public void add(HasVibration b) {
		builds.addUnique(b);
		b.vibration().links.each(links::addUnique);
		b.vibration().graph = this;
		addGraph();
	}
	public void remove(HasVibration b) {
		builds.remove(b);
		if (builds.isEmpty()) removeGraph();
	}
	public void mergeGraph(VibrationGraph other) {
		if (other.builds.size > builds.size) {
			other.mergeGraph(this);
			return;
		}
		other.removeGraph();
		other.builds.each(this::add);
		other.links.each(links::addUnique);
	}
	public void updateBuilds() {
		Seq<HasVibration> build = this.builds.copy();

		builds.each(b -> {
			b.vibration().graph = new VibrationGraph();
			b.vGraph().add(b);
		});
		build.each(b -> {
			flood(b).each(links -> b.vGraph().mergeGraph(links.vGraph()));
		});
		builds.clear();
	}

	public Seq<HasVibration> flood(HasVibration start) {
		Seq<HasVibration> out = Seq.with(start);
		Seq<HasVibration> temp = Seq.with(start);

		while (!temp.isEmpty()) {
			HasVibration init = temp.pop();
			init.vibration().links.each(c -> {
				if (c.other(init) != null && !out.contains(c.other(init))) {
					out.add(c.other(init));
					temp.add(c.other(init));
				}
			});
		}

		return out;
	}

	public float resistance() {
		return builds.sumf(b -> b.vConfig().resistance);
	}

	@Override
	public void update() {
		if (read) {
			updateBuilds();
			read = false;
		}
		FloatSeq f = new FloatSeq();
		for (int i = 0; i < frequencies.size; i++) if (frequencies.get(i) > 0) f.add(frequencies.get(i));
		frequencies = f;

		for (int i = 0; i < frequencies.size; i++) frequencies.incr(i, -Time.delta * resistance());
	}

	public static class VibrationLink {
		public int link1, link2;

		public VibrationLink(int link1, int link2) {
			this.link1 = link1;
			this.link2 = link2;
		}

		public @Nullable HasVibration link1() {
			return (HasVibration) Vars.world.build(link1);
		}
		public @Nullable HasVibration link2() {
			return (HasVibration) Vars.world.build(link2);
		}

		public boolean has (HasVibration link) {
			return link == link1() || link == link2();
		}
		public @Nullable HasVibration other(HasVibration link) {
			return has(link) ? (link == link2() ? link1() : link2()) : null;
		}

		@Override
		public boolean equals(Object obj) {
			return obj instanceof VibrationLink vibrationLink &&
				       (vibrationLink.link1 == link1 || vibrationLink.link1 == link2) &&
				       (vibrationLink.link2 == link1 || vibrationLink.link2 == link2);
		}
		@Override
		public String toString() {
			return "Link:( (" + link1().tileX() + ", " + link1().tileY() + ") (" + link2().tileX() + ", " + link2().tileY() + ") )";
		}
	}
}
