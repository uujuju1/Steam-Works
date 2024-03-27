package sw.dream;

import arc.*;
import arc.util.*;
import mindustry.*;
import mindustry.game.*;

public class DreamCore implements ApplicationListener {
	public static DreamCore instance;
	public DreamEvent currentEvent;

	public DreamCore() {
		if (Vars.platform instanceof ApplicationCore core) {
			core.add(this);
		}
		instance = this;
		Events.run(EventType.Trigger.draw, this::draw);
	}

	void draw() {
		if (currentEvent != null) currentEvent.draw();
	}

	public void event(@Nullable DreamEvent event) {
		currentEvent = event;
		if (event != null) event.init();
	}

	@Override
	public void update() {
		if (currentEvent != null) currentEvent.update();
	}
}
