package sw.dream;

import arc.*;
import arc.struct.*;
import mindustry.*;
import mindustry.game.*;
import sw.dream.states.*;

public class Dream implements ApplicationListener {
	public static Dream self;

	public final Seq<DreamState> states = Seq.with(new AttractState());

	public int currentState = -1;

	public Dream() {
		if (!Vars.headless) {
			self = this;

			Events.on(EventType.ClientLoadEvent.class, e -> Core.app.addListener(this));

			Events.run(EventType.Trigger.draw, () -> {
				// TODO does not work for some goddamm reason
				self.draw();
			});

		}
	}

	public void draw() {
		if (currentState == -1) currentState = Core.settings.getInt("sw-fool-state", 0);
		states.get(currentState).draw();
	}

	@Override
	public void update() {
		if (currentState == -1) currentState = Core.settings.getInt("sw-fool-state", 0);
		states.get(currentState).update();
		states.get(currentState).draw();
	}

	public abstract static class DreamState {
		public void draw() {

		}

		public void update() {

		}
	}
}
