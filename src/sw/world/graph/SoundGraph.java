package sw.world.graph;

import arc.func.*;
import arc.struct.*;
import sw.world.interfaces.*;

public class SoundGraph extends Graph {
	public Seq<HasSound> builds = new Seq<>(false, 16, HasSound.class);
	public Seq<SoundGroup> groups = new Seq<>(false, 16, SoundGroup.class);

	public void mergeGraph(SoundGraph other) {
		if (builds.size < other.builds.size) other.mergeGraph(this);

		other.removeGraph();
		other.builds.each(b -> addBuild(b));
	}

	public void addBuild(HasSound build) {
		builds.addUnique(build);
		if (build.soundGraph().builds.size == 1) build.soundGraph().removeGraph();
		build.sound().graph = this;
	}
	public void removeBuild(HasSound build) {
		builds.remove(build);
		groups.each(g -> g.builds.remove(build));
	}

	public void reloadGroups() {
		Seq<HasSound> temp = builds.copy();

		while (!temp.isEmpty()) {
			Seq<HasSound> line = floodDir(temp.peek(), temp.peek().rotation(), (last, other) -> other.rotation() == last.rotation() && builds.contains(other) );
			temp.removeAll(line);
			groups.add(new SoundGroup());
			groups.peek().builds.add(line);
		}
	}

	public Seq<HasSound> floodDir(HasSound start, int dir, Boolf2<HasSound, HasSound> cons) {
		Seq<HasSound> temp = Seq.with(start);
		Seq<HasSound> out = Seq.with(start);
		while(!temp.isEmpty()) {
			var c = temp.pop();
			for(int i = 0; i < 2; i++) {
				if (c.nearby((i * 2 + dir) % 4) instanceof HasSound b && cons.get(c, b)) {
					if (!out.contains(b)) temp.addUnique(b);
					out.addUnique(b);
				}
			}
		}

		return out;
	}

	@Override
	public void update() {
		groups.each(g -> {
			float amount = g.medianSound();
			g.builds.each(b -> b.setSound(amount/g.builds.size));
		});
	}

	public static class SoundGroup {
		public Seq<HasSound> builds = new Seq<>(false, 16, HasSound.class);

		public boolean has(HasSound build) {
			return builds.contains(build);
		}

		public float medianSound() {
			return builds.sumf(f -> f.soundIntensity());
		}

		@Override
		public String toString() {
			String s = "[\n";

			for (HasSound b: builds) {
				s = s + b + "\n";
			}
			s = s + "]";

			return s;
		}
	}
}
