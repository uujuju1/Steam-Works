package sw.ui;

import arc.*;
import arc.func.*;
import arc.util.*;
import mindustry.*;
import mindustry.ui.fragments.HintsFragment.*;
import sw.content.*;
import sw.content.blocks.*;

public enum EventHints implements Hint {
	tension(
		() -> Core.settings.getBool("sw-tension-hint-done", false),
		() -> Vars.state.rules.defaultTeam.data().getBuildings(SWDistribution.lowWire).size > 0
	),
	coatedTension(
		() -> Core.settings.getBool("sw-coatedTension-hint-done", false),
		() -> Vars.state.rules.defaultTeam.data().getBuildings(SWDistribution.coatedWire).size > 0,
		tension
	),
	rebuilder(
		() -> Vars.state.rules.defaultTeam.data().getBuildings(SWBlocks.rebuilder).size > 0 || Core.settings.getBool("sw-rebuilder-hint-done", false)
	);

	Boolp complete, shown = () -> true;
	EventHints[] requirements;

	int visibility = visibleAll;
	boolean cached, finished;

	static final String prefix = "sw-";

	EventHints(Boolp complete) {
		this.complete = complete;
	}
	EventHints(Boolp complete, Boolp shown) {
		this(complete);
		this.shown = shown;
	}
	EventHints(Boolp complete, Boolp shown, EventHints... requirements) {
		this(complete, shown);
		this.requirements = requirements;
	}

	@Override public boolean complete() {
		return complete.get();
	}

	@Override
	public void finish(){
		Core.settings.put(prefix + name() + "-hint-done", finished = true);
	}

	@Override
	public boolean finished(){
		if(!cached){
			cached = true;
			finished = Core.settings.getBool(prefix + name() + "-hint-done", false);
		}
		return finished;
	}

	@Override public boolean show(){
		return shown.get() && (requirements == null || (requirements.length == 0 || !Structs.contains(requirements, d -> !d.finished())));
	}

	@Override public int order() {
		return ordinal();
	}

	@Override public String text() {
		return Core.bundle.get("hint." + prefix + name(), "Missing bundle for hint: hint." + prefix + name());
	}

	@Override
	public boolean valid(){
		return (Vars.mobile && (visibility & visibleMobile) != 0) || (!Vars.mobile && (visibility & visibleDesktop) != 0);
	}
}
