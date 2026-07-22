package sw.matryoshka.entities;

import mindustry.entities.*;
import mindustry.gen.*;

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
		all = new EntityGroup<>(Entityc.class, false, false, (e, pos) -> {
//			if (e instanceof IndexableEntity__all ix) {
//				e.setIndex__all(pos);
//			}
		});
		player = new EntityGroup<>(Player.class, false, true, (e, pos) -> ((Player) e).setIndex__player(pos));
		bullet = new EntityGroup<>(Bullet.class, true, false, (e, pos) -> ((Bullet) e).setIndex__bullet(pos));
		unit = new EntityGroup<>(Unit.class, true, true, (e, pos) -> ((Unit) e).setIndex__unit(pos));
		build = new EntityGroup<>(Building.class, false, false, (e, pos) -> ((Building) e).setIndex__build(pos));
		sync = new EntityGroup(Syncc.class, false, true, (e, pos) -> {
//			((Sync) e).setIndex__sync(pos);
		});
		draw = new EntityGroup(Drawc.class, false, false, (e, pos) -> {
//			if (e instanceof IndexableEntity__draw ix) {
//				ix.setIndex__draw(pos);
//			}
		});
		fire = new EntityGroup<>(Fire.class, false, false, (e, pos) -> ((Fire) e).setIndex__fire(pos));
		puddle = new EntityGroup<>(Puddle.class, false, false, (e, pos) -> ((Puddle) e).setIndex__puddle(pos));
		weather = new EntityGroup<>(WeatherState.class, false, false, (e, pos) -> ((WeatherState) e).setIndex__weather(pos));
		label = new EntityGroup<>(WorldLabel.class, false, true, (e, pos) -> ((WorldLabel) e).setIndex__label(pos));
		powerGraph = new EntityGroup<>(PowerGraphUpdaterc.class, false, false, (e, pos) -> ((PowerGraphUpdater) e).setIndex__powerGraph(pos));
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
}
