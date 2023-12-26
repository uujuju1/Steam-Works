package sw.world.graph;

public class VibrationGraphDeprecated extends GraphDeprecated {
//	public final Seq<HasVibration> builds = new Seq<>(false, 16, HasVibration.class);
//	public final Seq<VibrationLink> links = new Seq<>(false, 16, VibrationLink.class);
//	public final Seq<Frequency> frequenci = new Seq<>(false, 16, Frequency.class);
//
//	/**
//	 * takes the build from the other graph and puts it here
//	 */
//	public void add(HasVibration b) {
//		if (!builds.contains(b)) {
//			builds.add(b);
//			b.vibration().links.each(this::addLink);
//			b.vibration().graph = this;
//			addGraph();
//		}
//	}
//	/**
//  * puts the removed build into another graph
//  */
//	public void remove(HasVibration b) {
//		builds.remove(b);
//		b.vibration().graph = new VibrationGraphDeprecated();
//		b.vGraph().add(b);
//		if (builds.isEmpty()) removeGraph();
//	}
//	/**
//	 * just removes the build from this graph
//	 */
//	public void delete(HasVibration b) {
//		builds.remove(b);
//		if (builds.isEmpty()) removeGraph();
//	}
//
//	public void mergeGraph(VibrationGraphDeprecated other) {
//		if (other.builds.size > builds.size) {
//			other.mergeGraph(this);
//			return;
//		}
//		other.removeGraph();
//		other.builds.each(this::add);
//		other.links.each(links::addUnique);
//	}
//
//	/**
//	 * links the 2 builds in this graph
//	 */
//	public void addLink(VibrationLink link) {
//		if(!link.valid()) return;
//		add(link.link1());
//		add(link.link2());
//		links.addUnique(link);
//	}
//	/**
//	 * separate the 2 builds into other graphs
//	 */
//	public void removeLink(VibrationLink link) {
//		if (link.link1() != null) {
//			remove(link.link1());
//		} else {
//			delete(link.link1());
//		}
//		if (link.link2() != null) {
//			remove(link.link2());
//		} else {
//			delete(link.link2());
//		}
//		links.remove(link);
//	}
//
//	public void addFrequency(Frequency frequency) {
//		if (!frequenci.contains(frequency)) {
//			frequenci.add(frequency);
//		} else {
//			frequenci.find(frequency1 -> frequency1.equals(frequency)).set(frequency);
//		}
//	}
//
//	public boolean hasFrequency(float frequency) {
//		for (Frequency f : frequenci) {
//			if (f.valid(frequency)) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	public float resistance() {
//		return builds.sumf(b -> b.vConfig().resistance);
//	}
//
	@Override
	public void update() {
//		frequenci.each(frequency -> frequency.update(this));
	}
//
//	public static class  VibrationLink {
//		public int link1, link2;
//
//		public VibrationLink(int link1, int link2) {
//			this.link1 = link1;
//			this.link2 = link2;
//		}
//
//		public @Nullable HasVibration link1() {
//			return (HasVibration) Vars.world.build(link1);
//		}
//		public @Nullable HasVibration link2() {
//			return (HasVibration) Vars.world.build(link2);
//		}
//
//		public boolean has(HasVibration link) {
//			return link == link1() || link == link2();
//		}
//		public @Nullable HasVibration other(HasVibration link) {
//			return has(link) ? (link == link2() ? link1() : link2()) : null;
//		}
//		public boolean valid() {
//			return link1() != null && link2() != null;
//		}
//
//		@Override
//		public boolean equals(Object obj) {
//			return obj instanceof VibrationLink vibrationLink &&
//				       (vibrationLink.link1 == link1 || vibrationLink.link1 == link2) &&
//				       (vibrationLink.link2 == link1 || vibrationLink.link2 == link2);
//		}
//		@Override
//		public String toString() {
//			return "Link:( (" + link1().tileX() + ", " + link1().tileY() + ") (" + link2().tileX() + ", " + link2().tileY() + ") )";
//		}
//	}
//
//	public static abstract class Frequency {
//		abstract void update(VibrationGraphDeprecated graph);
//		abstract boolean valid(float frequency);
//		abstract void set(Frequency frequency);
//
//		public Table display() {
//			return new Table();
//		}
//	}
//	public static class StaticFrequency extends Frequency {
//		public boolean enabled;
//		public float min, max;
//
//		public StaticFrequency(boolean enabled, float min, float max) {
//			this.enabled = enabled;
//			this.min = min;
//			this.max = max;
//		}
//		public StaticFrequency(float min, float max) {
//			this(true, min, max);
//		}
//
//		@Override public void update(VibrationGraphDeprecated graph) {
//			if (!enabled) graph.frequenci.remove(this);
//		}
//		@Override public boolean valid(float frequency) {
//			return min < frequency && frequency < max;
//		}
//		@Override public void set(Frequency f) {
//			if (f instanceof StaticFrequency frequency) enabled = frequency.enabled;
//		}
//
//		@Override
//		public Table display() {
//			return new Table(stat -> {
//				Stack values = new Stack();
//
//				values.add(new Table(o -> {
//					o.add("").size(200, 0);
//				}));
//				values.add(new Table(o -> {
//					o.left();
//					o.add(min + "");
//				}));
//				values.add(new Table(o -> {
//					o.right();
//					o.add(max + "");
//				}));
//				stat.table(Styles.black3, table -> {
//					table.add("Static Frequency").color(Pal.accent).row();
//					table.add(values).row();
//					table.rect((x, y, w, h) -> {
//						Draw.color(Color.gray);
//						Fill.rect(x + 100, y, 200, 5);
//						for (int i = 0; i < 11; i++) {
//							if (i == 0 || i == 10) {
//								Draw.color(Pal.accent);
//							} else {
//								Draw.color(Color.gray);
//							}
//							Fill.rect(x + 10 + 18 * i, y, 5, 20);
//						}
//					}).size(200, 10);
//				}).margin(5);
//			});
//		}
//
//		@Override
//		public boolean equals(Object obj) {
//			return obj instanceof StaticFrequency frequency &&
//							 min == frequency.min &&
//							 max == frequency.max;
//		}
//		@Override public String toString() {
//			return "(StaticFrequency, enabled: " + enabled + ", min: " + min + ", frequency: " + max + ")";
//		}
//	}
//	public static class DecayingFrequency extends Frequency {
//		public float decay;
//		public float min, max;
//
//		public DecayingFrequency(float min, float max, float decay) {
//			this.min = min;
//			this.max = max;
//			this.decay = decay;
//		}
//
//		@Override
//		public void update(VibrationGraphDeprecated graph) {
//			decay -= graph.resistance() * Time.delta;
//			if (decay < 0) graph.frequenci.remove(this);
//		}
//		@Override boolean valid(float frequency) {
//			return min + decay < frequency && frequency < max + decay;
//		}
//		@Override public void set(Frequency f) {
//			if (f instanceof DecayingFrequency frequency) decay = frequency.decay;
//		}
//
//		@Override
//		public Table display() {
//			return new Table(stat -> {
//				Stack values = new Stack();
//
//				values.add(new Table(o -> o.add("").size(200, 0)));
//				values.add(new Table(o -> o.add(decay + "+")));
//				values.add(new Table(o -> {
//					o.left();
//					o.add(min + "");
//				}));
//				values.add(new Table(o -> {
//					o.right();
//					o.add(max + "");
//				}));
//				stat.table(Styles.black3, table -> {
//					table.add("Decaying Frequency").color(Pal.accent).row();
//					table.add(values).row();
//					table.rect((x, y, w, h) -> {
//						Draw.color(Color.gray);
//						Fill.rect(x + 100, y, 200, 5);
//						for (int i = 0; i < 11; i++) {
//							if (i == 0 || i == 10) {
//								Draw.color(Pal.accent);
//							} else {
//								Draw.color(Color.gray);
//							}
//							Fill.rect(x + 10 + 18 * i, y, 5, 20);
//						}
//					}).size(200, 10);
//				}).margin(5);
//			});
//		}
//
//		@Override public boolean equals(Object obj) {
//			return obj instanceof DecayingFrequency frequency && min == frequency.min && max == frequency.max;
//		}
//		@Override public String toString() {
//			return "(DecayingFrequency" +
//				       ", min: " + (min + decay) + " from: " + min +
//				       ", max: " + (max + decay) + ", from: " + max +
//				       ", decay: " + decay + ")";
//		}
//	}
}
