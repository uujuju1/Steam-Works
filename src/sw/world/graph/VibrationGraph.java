package sw.world.graph;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import sw.gen.*;
import sw.world.interfaces.*;

public class VibrationGraph {
	public final @Nullable VibrationGraphEntity entity;

	public final Seq<HasVibration> builds = new Seq<>();
	public boolean changed = false;

	public final Seq<Frequency> frequencies = new Seq<>();

	public VibrationGraph() {
		this(VibrationGraphEntity.create());
	}
	public VibrationGraph(VibrationGraphEntity entity) {
		this.entity = entity;
		entity.graph(this);
		entity.add();
	}

	/**
	 * self-explanatory
	 */
	public void addBuild(HasVibration build) {
		builds.addUnique(build);
		build.vibration().graph = this;
		for (HasVibration next : build.nextBuilds()) {
			if (!builds.contains(next) && next.vibrationConfig().linksGraph) addBuild(next);
		}
		changed = true;
	}
	/**
	 * @param erased when false will put the build in another graph.
	 * Always make erased true when the build will be removed from the game
	 */
	public void removeBuild(HasVibration build, boolean erased) {
		builds.remove(build);
		if (!erased) {
			build.vibration().graph = new VibrationGraph();
			build.vibrationGraph().addBuild(build);
		} else {
			for (HasVibration next : build.nextBuilds()) {
				removeBuild(next, false);
			}
		}
		if (builds.isEmpty()) entity.remove();
		changed = true;
	}

/**
 * adds a frequency
 */
	public void addFrequency(Frequency frequency) {
		if (!frequencies.contains(frequency)) {
			frequencies.add(frequency);
		} else {
			frequencies.find(frequency1 -> frequency1.equals(frequency)).set(frequency);
		}
	}

	/**
	 * checks if this frequency exists in any of the frequencies on the graph
	 */
	public boolean hasFrequency(float frequency) {
		for (Frequency f : frequencies) {
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
		return builds.sumf(b -> b.vibrationConfig().resistance);
	}

	/**
	 * method that constantly updates the graph and it's funcions
	 */
	public void update() {
		if (changed) {
			builds.removeAll(build -> !build.isValid() || build.vibrationGraph() != this);
			if (builds.isEmpty()) entity.remove();
			changed = false;
		}
		frequencies.each(frequency -> frequency.update(this));
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
			if (!enabled) graph.frequencies.remove(this);
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
			if (decay < 0) graph.frequencies.remove(this);
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
