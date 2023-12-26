package sw.world.graph;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import sw.gen.*;
import sw.world.interfaces.*;

public class VibrationGraph {
	public final @Nullable VibrationGraphEntity entity;

	public final Seq<HasVibration> builds = new Seq<>();
	public final Seq<VibrationLink> links = new Seq<>();
	public final Seq<Frequency> frequenci = new Seq<>();

	public VibrationGraph() {
		this(VibrationGraphEntity.create());
	}
	public VibrationGraph(VibrationGraphEntity entity) {
		this.entity = entity;
		entity.graph(this);
		entity.add();
	}

	/**
	 * adds a build with flood
	 */
	public void addBuild(HasVibration build) {
		if (!builds.contains(build)) {
			build.vGraph().removeBuild(build, false);
			build.vibration().graph = this;
			builds.addUnique(build);
			for (VibrationLink forceLink : build.vibration().links) {
				addLink(forceLink);
			}
		}
	}
	/**
	 * removes the build from this graph and adds it (or not) to another graph
	 */
	public void removeBuild(HasVibration build) {
		removeBuild(build, true);
	}
	public void removeBuild(HasVibration build, boolean replace) {
		builds.remove(build);
		if (replace) new VibrationGraph().addBuild(build);
		if (builds.isEmpty()) remove();
	}
	/**
	 * adds a link and it's builds into the graph
	 */
	public void addLink(VibrationLink link) {
		if (!link.valid()) return;
		links.addUnique(link);
		if (link.startBuild() != null) addBuild(link.startBuild());
		if (link.endBuild() != null) addBuild(link.endBuild());
	}
	/**
	 * removes a link and it's builds from the graph
	 */
	public void removeLink(VibrationLink link) {
		links.remove(link);
		removeBuild(link.startBuild(), link.startBuild() != null);
		removeBuild(link.endBuild(), link.endBuild() != null);
	}
/**
 * adds a frequency
 */
	public void addFrequency(Frequency frequency) {
		if (!frequenci.contains(frequency)) {
			frequenci.add(frequency);
		} else {
			frequenci.find(frequency1 -> frequency1.equals(frequency)).set(frequency);
		}
	}

	/**
	 * checks if this frequency exists in any of the frequencies on the graph
	 */
	public boolean hasFrequency(float frequency) {
		for (Frequency f : frequenci) {
			if (f.valid(frequency)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * resistance used by DecayingFrequency
	 */
	public float resistance() {
		return builds.sumf(b -> b.vConfig().resistance);
	}

	/**
	 * method that constantly updates the graph and it's funcions
	 */
	public void update() {
		frequenci.each(frequency -> frequency.update(this));
	}

	/**
	 * add or remove the graph (entity related)
	 */
	public void add() {
		entity.add();
	}
	public void remove() {
		entity.remove();
	}

	/**
	 * io
	 */
	public void read(Reads read) {}
	public void afterRead() {}
	public void write(Writes write) {}

	public static class VibrationLink {
		public int start, end;

		public VibrationLink(int start, int end) {
			this.start = start;
			this.end = end;
		}

		public @Nullable HasVibration startBuild() {
			return (HasVibration) Vars.world.build(start);
		}
		public @Nullable HasVibration endBuild() {
			return (HasVibration) Vars.world.build(end);
		}

		public boolean has(HasVibration link) {
			return link == startBuild() || link == endBuild();
		}
		public @Nullable HasVibration other(HasVibration link) {
			return has(link) ? (link == endBuild() ? startBuild() : endBuild()) : null;
		}
		public boolean valid() {
			return startBuild() != null && endBuild() != null;
		}

		@Override public boolean equals(Object obj) {
			return obj instanceof VibrationLink vibrationLink &&
				       (vibrationLink.start == start || vibrationLink.start == end) &&
				       (vibrationLink.end == end || vibrationLink.end == start);
		}
		@Override public String toString() {
			return "(link1: " + Point2.unpack(start) + "; link2: " + Point2.unpack(end) + ")";
		}
	}

	public static abstract class Frequency {
		abstract void update(VibrationGraph graph);
		abstract boolean valid(float frequency);
		abstract void set(Frequency frequency);

		public Table display() {
			return new Table();
		}
	}
	public static class StaticFrequency extends Frequency {
		public boolean enabled;
		public float min, max;

		public StaticFrequency(boolean enabled, float min, float max) {
			this.enabled = enabled;
			this.min = min;
			this.max = max;
		}
		public StaticFrequency(float min, float max) {
			this(true, min, max);
		}

		@Override public void update(VibrationGraph graph) {
			if (!enabled) graph.frequenci.remove(this);
		}
		@Override public boolean valid(float frequency) {
			return min < frequency && frequency < max;
		}
		@Override public void set(Frequency f) {
			if (f instanceof StaticFrequency frequency) enabled = frequency.enabled;
		}

		@Override
		public Table display() {
			return new Table(stat -> {
				Stack values = new Stack();

				values.add(new Table(o -> {
					o.add("").size(200, 0);
				}));
				values.add(new Table(o -> {
					o.left();
					o.add(min + "");
				}));
				values.add(new Table(o -> {
					o.right();
					o.add(max + "");
				}));
				stat.table(Styles.black3, table -> {
					table.add("Static Frequency").color(Pal.accent).row();
					table.add(values).row();
					table.rect((x, y, w, h) -> {
						Draw.color(Color.gray);
						Fill.rect(x + 100, y, 200, 5);
						for (int i = 0; i < 11; i++) {
							if (i == 0 || i == 10) {
								Draw.color(Pal.accent);
							} else {
								Draw.color(Color.gray);
							}
							Fill.rect(x + 10 + 18 * i, y, 5, 20);
						}
					}).size(200, 10);
				}).margin(5);
			});
		}

		@Override
		public boolean equals(Object obj) {
			return obj instanceof StaticFrequency frequency &&
				       min == frequency.min &&
				       max == frequency.max;
		}
		@Override public String toString() {
			return "(StaticFrequency, enabled: " + enabled + ", min: " + min + ", frequency: " + max + ")";
		}
	}
	public static class DecayingFrequency extends Frequency {
		public float decay;
		public float min, max;

		public DecayingFrequency(float min, float max, float decay) {
			this.min = min;
			this.max = max;
			this.decay = decay;
		}

		@Override
		public void update(VibrationGraph graph) {
			decay -= graph.resistance() * Time.delta;
			if (decay < 0) graph.frequenci.remove(this);
		}
		@Override boolean valid(float frequency) {
			return min + decay < frequency && frequency < max + decay;
		}
		@Override public void set(Frequency f) {
			if (f instanceof DecayingFrequency frequency) decay = frequency.decay;
		}

		@Override
		public Table display() {
			return new Table(stat -> {
				Stack values = new Stack();

				values.add(new Table(o -> o.add("").size(200, 0)));
				values.add(new Table(o -> o.add(decay + "+")));
				values.add(new Table(o -> {
					o.left();
					o.add(min + "");
				}));
				values.add(new Table(o -> {
					o.right();
					o.add(max + "");
				}));
				stat.table(Styles.black3, table -> {
					table.add("Decaying Frequency").color(Pal.accent).row();
					table.add(values).row();
					table.rect((x, y, w, h) -> {
						Draw.color(Color.gray);
						Fill.rect(x + 100, y, 200, 5);
						for (int i = 0; i < 11; i++) {
							if (i == 0 || i == 10) {
								Draw.color(Pal.accent);
							} else {
								Draw.color(Color.gray);
							}
							Fill.rect(x + 10 + 18 * i, y, 5, 20);
						}
					}).size(200, 10);
				}).margin(5);
			});
		}

		@Override public boolean equals(Object obj) {
			return obj instanceof DecayingFrequency frequency && min == frequency.min && max == frequency.max;
		}
		@Override public String toString() {
			return "(DecayingFrequency" +
				       ", min: " + (min + decay) + " from: " + min +
				       ", max: " + (max + decay) + ", from: " + max +
				       ", decay: " + decay + ")";
		}
	}
}
