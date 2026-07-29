package sw.matryoshka.entities;

import mindustry.entities.*;
import mindustry.gen.*;

/**
 * @see Groups
 */
public class NestedGroups {
	public EntityGroup<Entityc> all;
	public EntityGroup<Building> build;
	public EntityGroup<Bullet> bullet;
	public EntityGroup<Drawc> draw;
	public EntityGroup<Fire> fire;
	public EntityGroup<WorldLabel> label;
	public EntityGroup<Player> player;
	public EntityGroup<PowerGraphUpdaterc> powerGraph;
	public EntityGroup<Puddle> puddle;
	public EntityGroup<Syncc> sync;
	public EntityGroup<Unit> unit;
	public EntityGroup<WeatherState> weather;

	public void init() {
		all = new NestedGroup<>(Entityc.class, false, false);
		player = new NestedGroup<>(Player.class, false, true);
		bullet = new NestedGroup<>(Bullet.class, true, false);
		unit = new NestedGroup<>(Unit.class, true, true);
		build = new NestedGroup<>(Building.class, false, false);
		sync = new NestedGroup<>(Syncc.class, false, true);
		draw = new NestedGroup<>(Drawc.class, false, false);
		fire = new NestedGroup<>(Fire.class, false, false);
		puddle = new NestedGroup<>(Puddle.class, false, false);
		weather = new NestedGroup<>(WeatherState.class, false, false);
		label = new NestedGroup<>(WorldLabel.class, false, true);
		powerGraph = new NestedGroup<>(PowerGraphUpdaterc.class, false, false);
	}

	public void resize(float x, float y, float w, float h) {
		bullet.resize(x, y, w, h);
		unit.resize(x, y, w, h);
	}

	public void update() {
		bullet.updatePhysics();
		unit.updatePhysics();
		all.update();
		build.update();
		bullet.collide();
	}

	public static class NestedGroup<T extends Entityc> extends EntityGroup<T> {
		public NestedGroup(Class<T> type, boolean spatial, boolean mapping) {
			super(type, spatial, mapping);
		}

		@Override
		public void removeIndex(T type, int position) {
			remove(type);
		}
	}
}
