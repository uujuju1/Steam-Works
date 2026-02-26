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
//	public boolean shown = false;
	
	public @Nullable SpinGraph currentGraph;
	public @Nullable Building currentHovered;

	public float currentRatio = 1f;
	
	private Table table;
	
	public SpinFragment() {
		Events.on(EventType.ResizeEvent.class, e -> setSize(Core.graphics.getWidth(), Core.graphics.getHeight()));
		Events.run(EventType.Trigger.draw, this::drawWorld);
	}
	
	public SpinFragment build(Group parent) {
		parent.addChild(this);
		visible(() -> !Vars.state.isMenu() && Core.input.keyDown(SWBinding.spinInfo));
		touchable = Touchable.disabled;
		
		update(this::update);

		table = new Table(Styles.black6);
		addChild(table);
		
		return this;
	}

	protected void buildGraph(SpinGraph graph) {
		currentGraph = graph;
		if (!(currentHovered instanceof HasSpin spin) || graph == null) {
			actions(Actions.fadeOut(0.25f, Interp.circleIn));
			return;
		} else {
			actions(Actions.fadeIn(0.25f, Interp.circleOut));
		}

		table.clear();

		table.defaults().pad(10);

		table.add(new RotationBar(
			() -> Core.bundle.format("bar.sw-rotation", Strings.fixed(graph.speed * 10 / currentRatio, 2)),
			() -> graph.rotation / currentRatio
		)).minSize(300, 20).growX().get();
		table.row();

		SplitBar forceBar = new SplitBar().setBar(
			() -> (graph.friction + graph.torque) == 0 ? 1 : graph.friction / (graph.friction + graph.torque),
			() -> Pal.breakInvalid,
			() -> "-" + Strings.fixed(graph.friction * 600 * currentRatio, 2) + " " + SWStat.force.localized(),
			true
		).setBar(
			() -> (graph.friction + graph.torque) == 0 ? 1 : graph.torque / (graph.friction + graph.torque),
			() -> Pal.heal,
			() -> Strings.fixed(graph.torque * 600 * currentRatio, 2) + " " + SWStat.force.localized(),
			false
		);
		table.add(forceBar).minSize(300, 20).growX().padTop(0);
		table.row();
		table.label(() -> "[lightgray]" + SWStat.spinOutput.localized() + ": []" + Strings.fixed(graph.targetSpeed * 10 / currentRatio, 2) + " " + SWStat.spinMinute.localized()).padTop(0).left().row();
		table.label(() -> "[lightgray]" + SWStat.weight.localized() + ": []" + Strings.fixed(graph.inertia == 1 ? 1f : graph.inertia * currentRatio, 2) + " " + SWStat.mass.localized()).padTop(0).left().row();
		// TODO show where the failure happens
		if (graph.invalid) table.add("@ui.sw-graph-invalid").width(300f).left().wrap().row();

		if (Core.settings.getBool("sw-developer-mode", false)) {
			table.label(() -> "[lightgray]Ratio: []X" + currentRatio).left().row();
		}

		table.invalidate();
		table.pack();
	}
	
	public void drawWorld() {
		if (currentGraph != null && visible) {
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
	
	public void update() {
		currentHovered = Vars.world.buildWorld(Core.input.mouseWorld());
		
		if (currentHovered instanceof HasSpin spin && spin.spinConfig() != null) {
			if (spin.spinGraph() != currentGraph) buildGraph(spin.spinGraph());

			currentRatio = spin.getRatio();

			Core.camera.project(Tmp.v1.set(currentHovered));
			table.setPosition(Tmp.v1.x, Tmp.v1.y, Align.center);
		} else {
			if (currentGraph != null) buildGraph(null);
		}
	}
}
