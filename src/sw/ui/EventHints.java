package sw.ui;

import arc.*;
import arc.func.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.ui.fragments.HintsFragment.*;
import sw.content.blocks.*;

public enum EventHints implements Hint {
	hydraulicDrill(
		() -> Vars.state.rules.defaultTeam.data().getBuildings(SWProduction.hydraulicDrill).size > 0,
		() -> Vars.control.input.block == SWProduction.hydraulicDrill
	),
	mechanicalBore(
		() -> Vars.state.rules.defaultTeam.data().getBuildings(SWProduction.mechanicalBore).size > 0,
		() -> Vars.control.input.block == SWProduction.mechanicalBore
	),
	dehydrator(
		() -> Vars.state.rules.defaultTeam.data().getBuildings(SWProduction.dehydrator).size > 0,
		() -> Vars.control.input.block == SWProduction.dehydrator
	);

	Boolp complete, shown = () -> true;
	EventHints[] requirements;

	int visibility = visibleAll;
	boolean cached, finished;

	static final String prefix = "sw-";

	public static void initHints() {
		Vars.ui.hints.hints.add(Seq.with(values()).removeAll(hint -> Core.settings.getBool(prefix + hint.name() + "-hint-done", false)));
	}
	public static void resetHints() {
		for(EventHints hint : values()) {
			Core.settings.put(prefix + hint.name() + "-hint-done", false);
			Vars.ui.hints.hints.addUnique(hint);
		}
	}

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
