package sw.matryoshka.world;

import mindustry.*;
import mindustry.core.*;
import mindustry.entities.*;
import mindustry.gen.*;

public class NestingContext {
	private boolean isRunning;

	public Nesting origin;

	public World returnWorld;
	public static EntityGroup<Entityc> all;
	public static EntityGroup<Building> build;
	public static EntityGroup<Bullet> bullet;
	public static EntityGroup<Drawc> draw;
	public static EntityGroup<Fire> fire;
	public static EntityGroup<WorldLabel> label;
	public static EntityGroup<Player> player;
	public static EntityGroup<PowerGraphUpdaterc> powerGraph;
	public static EntityGroup<Puddle> puddle;
	public static EntityGroup<Syncc> sync;
	public static EntityGroup<Unit> unit;
	public static EntityGroup<WeatherState> weather;

	public void begin() {
		if (isRunning) throw new IllegalStateException("cannot begin a context that is running.");

		Vars.world = origin.world;
		Groups.all = origin.groups.all;
		Groups.build = origin.groups.build;
		Groups.bullet = origin.groups.bullet;
		Groups.draw = origin.groups.draw;
		Groups.fire = origin.groups.fire;
		Groups.label = origin.groups.label;
		Groups.player = origin.groups.player;
		Groups.powerGraph = origin.groups.powerGraph;
		Groups.puddle = origin.groups.puddle;
		Groups.sync = origin.groups.sync;
		Groups.unit = origin.groups.unit;
		Groups.weather = origin.groups.weather;

		isRunning = true;
	}

	public void end() {
		if (!isRunning) throw new IllegalStateException("cannot end a context that is not running.");

		Vars.world = returnWorld;
		Groups.all = all;
		Groups.build = build;
		Groups.bullet = bullet;
		Groups.draw = draw;
		Groups.fire = fire;
		Groups.label = label;
		Groups.player = player;
		Groups.powerGraph = powerGraph;
		Groups.puddle = puddle;
		Groups.sync = sync;
		Groups.unit = unit;
		Groups.weather = weather;

		isRunning = false;
	}
}
