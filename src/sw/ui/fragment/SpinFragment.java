package sw.ui.fragment;

import arc.*;
import arc.graphics.*;
import arc.scene.*;
import arc.scene.actions.*;
import arc.scene.event.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import sw.ui.elements.*;
import sw.ui.elements.RotationBar.*;
import sw.world.graph.*;
import sw.world.interfaces.*;

public class SpinFragment extends Group{
	public boolean shown = false;
	
	public @Nullable SpinGraph currentGraph;
	
	private Table infoTable;
	
	public SpinFragment() {
		Events.on(EventType.ResizeEvent.class, e -> {
			setSize(Core.graphics.getWidth(), Core.graphics.getHeight());
		});
	}
	
	public SpinFragment build(Group parent) {
		parent.addChild(this);
		visible(() -> !Vars.state.isMenu());
		touchable(() -> shown ? Touchable.enabled : Touchable.disabled);
		
		buildInfo();
		
		update(() -> {
			if (Vars.state.isMenu() && shown) toggle();
			if (!shown || infoTable.hasActions()) return;
			
			if (shown) {
				Vars.ui.hudfrag.shown = false;
				Building buildAt = Vars.world.buildWorld(Core.input.mouseWorldX(), Core.input.mouseWorldY());
				
				if (buildAt instanceof HasSpin spin) {
					if (spin.spinGraph() != currentGraph) changeGraph(spin.spinGraph());
				} else {
					if (currentGraph != null) changeGraph(null);
				}
			}
		});
		
		return this;
	}
	
	private void buildInfo() {
		fill(t -> {
			t.bottom().right();
			infoTable = t.table(Styles.black6, info -> {}).margin(10f).get();
			changeGraph(null);
		});
		infoTable.actions(Actions.fadeOut(0f));
	}
	
	private void changeGraph(@Nullable SpinGraph newer) {
		currentGraph = newer;
		
		infoTable.clear();
		
		if (newer == null) {
			infoTable.add("Please Select a graph");
		} else {
			infoTable.add(new RotationBar(
				() -> Core.bundle.format("bar.sw-rotation", Strings.fixed(newer.speed * 10f, 2)),
				() -> newer.rotation
			)).size(250f, 20f).pad(10f).get().setStyle(new RotationBarStyle() {{
				outlineColor = Pal.darkestGray;
				outlineRadius = 4f;
			}});
			
			infoTable.row();
			
			infoTable.table(forces -> {
				forces.add(new Bar(
					() -> Core.bundle.format("bar.sw-force", newer.force() * 600f),
					() -> Pal.heal,
					() -> (Math.abs(newer.force()) + newer.resistance()) != 0 ? newer.force() / (Math.abs(newer.force()) + newer.resistance()) : 0f
				)).size(250f, 20f);
				forces.add(new Bar(
					() -> Core.bundle.format("bar.sw-force", newer.resistance() * 600f),
					() -> Color.scarlet,
					() -> (Math.abs(newer.force()) + newer.resistance()) != 0 ? newer.resistance() / (Math.abs(newer.force()) + newer.resistance()) : 0f
				)).size(250f, 20f).row();
				forces.add(Core.bundle.get("stat.sw-spin-output-force"));
				forces.add(Core.bundle.get("stat.sw-spin-resistance"));
			});
		}
	}
	
	public void toggle() {
		if (infoTable.hasActions() || Vars.state.isMenu()) return;
		
		shown = !shown;
		Vars.ui.hudfrag.shown = !shown;
		
		if (shown) {
			infoTable.actions(
				Actions.fadeOut(0f),
				Actions.fadeIn(1f)
			);
		} else {
			infoTable.actions(
				Actions.fadeIn(0f),
				Actions.fadeOut(1f)
			);
		}
	}
}
