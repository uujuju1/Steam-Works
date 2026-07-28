package sw.matryoshka;

import arc.*;
import arc.func.*;
import arc.struct.*;
import arc.util.*;

public class ExtraReflect {
	public static Seq<Cons<?>> getEvents(Class<?> type) {
		ObjectMap<Object, Seq<Cons<?>>> totalEvents = Reflect.get(Events.class, "events");

		return totalEvents.get(type, Seq.with());
	}

	/**
	 * Removes and returns the last runnable associated with a given event.
	 * Will return an empty runnable if the event doesn't have any runnables associated with it.
	 */
	public static Cons<?> popEvent(Class<?> type) {
		ObjectMap<Object, Seq<Cons<?>>> totalEvents = Reflect.get(Events.class, "events");
		Seq<Cons<?>> runs = totalEvents.get(type, Seq.with(new Cons<?>[]{e -> {}}));

		if (runs.isEmpty()) Log.info("no event listeners found for @", type);
		return runs.isEmpty() ? e -> {} : runs.pop();
	}
}
