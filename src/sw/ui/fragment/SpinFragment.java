package sw.ui.fragment;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.math.*;
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
import sw.*;
import sw.core.*;
import sw.ui.elements.*;
import sw.ui.elements.RotationBar.*;
import sw.world.graph.*;
import sw.world.interfaces.*;
import sw.world.meta.*;

public class SpinFragment extends Group{
	public boolean shown = false;
	
	public @Nullable SpinGraph currentGraph;
	
	private Table infoTable;
	
	public SpinFragment() {
		Events.on(EventType.ResizeEvent.class, e -> setSize(Core.graphics.getWidth(), Core.graphics.getHeight()));
		Events.run(EventType.Trigger.draw, this::drawWorld);
	}
	
	public SpinFragment build(Group parent) {
		parent.addChild(this);
		visible(() -> !Vars.state.isMenu());
		touchable(() -> shown ? Touchable.enabled : Touchable.disabled);
		
		buildInfo();
		
		update(() -> {
			if (Vars.state.isMenu() && shown || Core.input.keyTap(SWBinding.spinInfo)) toggle();
			if (!shown) return;
			
			Vars.ui.hudfrag.shown = false;
			Building buildAt = Vars.world.buildWorld(Core.input.mouseWorldX(), Core.input.mouseWorldY());
			
			if (buildAt instanceof HasSpin spin) {
				if (spin.spinGraph() != currentGraph) changeGraph(spin.spinGraph());
			} else {
				if (currentGraph != null) changeGraph(null);
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
			
			infoTable.add(new SplitBar().setBar(
				() -> {
					float force = newer.force();
					float resistance = Mathf.sign(-newer.speed) * newer.resistance();
					float net = 0;
					if (force < 0) net += force;
					if (resistance < 0) net += resistance;
					return net/(Math.abs(force) + Math.abs(resistance));
				},
				() -> newer.speed > 0 ? Color.scarlet : (newer.speed == 0 ? Pal.gray : Pal.heal),
				() -> {
					float force = newer.force();
					float resistance = Mathf.sign(-newer.speed) * newer.resistance();
					float net = 0;
					if (force < 0) net += force;
					if (resistance < 0) net += resistance;
					if (newer.speed == 0) {
						net = newer.resistance() * (newer.force() < 0 ? -1f : 1f) + newer.force();
					}
					return Strings.fixed(net * 600f, 2) + " " + SWStat.force.localized();
				},
				true
			).setBar(
				() -> {
					float force = newer.force();
					float resistance = Mathf.sign(-newer.speed) * newer.resistance();
					float net = 0;
					if (force > 0) net += force;
					if (resistance > 0) net += resistance;
					return net/(Math.abs(force) + Math.abs(resistance));
				},
				() -> newer.speed > 0 ? Pal.heal : (newer.speed == 0 ? Pal.gray : Color.scarlet),
				() -> {
					float force = newer.force();
					float resistance = Mathf.sign(-newer.speed) * newer.resistance();
					float net = 0;
					if (force > 0) net += force;
					if (resistance > 0) net += resistance;
					if (newer.speed == 0) {
						net = newer.resistance() * (newer.force() < 0 ? -1f : 1f) + newer.force();
					}
					return Strings.fixed(net * 600f, 2) + " " + SWStat.force.localized();
				},
				false
			)).size(250f, 20f).pad(10f);
			
			infoTable.row();
			
			infoTable.add(new Bar(
				() -> Strings.fixed(newer.inertia(), 2) + " " + SWStat.mass.localized(),
				() -> Color.black,
				() -> 0f
			)).size(250f, 20f).pad(10f);
			
			infoTable.row();
		}
	}
	
	public void drawWorld() {
		if (currentGraph != null && shown) {
			Draw.draw(Layer.flyingUnitLow - 1f, () -> {
				FrameBuffer buffer = SWVars.renderer.getBuffer("spinFragment");
				
				buffer.begin(Color.clear);
				currentGraph.builds.each(b -> {
					Building build = b.asBuilding();
					Fill.square(build.x, build.y, build.block.size * 4 + 1f);
				});
				
				Draw.flush();
				Draw.blend(new Blending(Gl.zero, Gl.zero));
				
				currentGraph.builds.each(b -> {
					Building build = b.asBuilding();
					Fill.square(build.x, build.y, build.block.size * 4);
				});
				
				Draw.flush();
				Draw.blend();
				
				buffer.end();
				
				Draw.color(Pal.accent);
				Draw.rect(Draw.wrap(buffer.getTexture()), Core.camera.position.x, Core.camera.position.y, Core.camera.width, -Core.camera.height);
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
				Actions.fadeIn(0f)
			);
		} else {
			infoTable.actions(
				Actions.fadeIn(0f),
				Actions.fadeOut(0f)
			);
		}
	}
}
