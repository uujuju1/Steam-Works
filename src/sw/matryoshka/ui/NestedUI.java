package sw.matryoshka.ui;

import arc.*;
import arc.graphics.*;
import arc.scene.*;
import arc.scene.ui.ImageButton.*;
import arc.scene.ui.TextButton.*;
import arc.scene.ui.layout.*;
import mindustry.gen.*;
import mindustry.ui.*;
import sw.gen.*;
import sw.matryoshka.*;
import sw.matryoshka.ui.TutorialSequence.*;
import sw.ui.*;

public class NestedUI extends WidgetGroup {
	public Runnable closeListener;

	public TutorialSequence currentTutorial;
	public SequenceLayer currentLayer;
	public int currentLayerIndex;

	private NestedLogic logic;

	public Group layerTable = new WidgetGroup();

	public void build(NestedLogic logic) {
		Core.scene.add(this);
		setFillParent(true);
		visible = false;
		closeListener = logic::disable;
		this.logic = logic;

		layerTable.setFillParent(true);
		addChild(layerTable);

		// close button
		fill(t -> {
			t.bottom().right();
			t.button("@back", new TextButtonStyle() {{
				font = Fonts.def;
				up = Tex.buttonSideLeft;
				down = Tex.buttonSideLeftDown;
				over = Tex.buttonSideLeftOver;
			}}, closeListener).size(200f, 54);
		});

		// layer buttons
		fill(t -> {
			t.bottom();
			t.button(Icon.left, new ImageButtonStyle() {{
				up = Tex.buttonEdge1;
				down = Tex.buttonEdgeDown1;
				over = Tex.buttonEdgeOver1;
				disabled = SWTex.buttonEdgeDisabled1;
			}}, () -> putTutorial(currentTutorial, currentLayerIndex - 1))
				.disabled(b -> currentTutorial == null || currentLayerIndex == 0)
				.size(100f, 54f);
			t.button(Icon.pause, new ImageButtonStyle() {{
				up = SWTex.paneCenter;
				down = SWTex.paneCenterDown;
				over = SWTex.paneCenterOver;
				disabled = SWStyles.whiteui.tint(Color.valueOf("454545"));
			}}, () -> logic.shouldUpdate = !logic.shouldUpdate)
				.update(button -> button.getStyle().imageUp = logic.shouldUpdate ? Icon.pause : Icon.play)
				.disabled(b -> currentTutorial == null)
				.size(100f, 54f);
			t.button(Icon.right, new ImageButtonStyle() {{
				up = Tex.buttonEdge3;
				down = Tex.buttonEdgeDown3;
				over = Tex.buttonEdgeOver3;
				disabled = SWTex.buttonEdgeDisabled3;
			}}, () -> putTutorial(currentTutorial, currentLayerIndex + 1))
				.disabled(b -> currentTutorial == null || currentLayerIndex == currentTutorial.maxLayers - 1)
				.size(100f, 54f);
		});

		// timer placement
		// TODO TIMER
		fill(t -> {
			t.top();
			t.table(Tex.wavepane, top -> {

			}).height(100f).growX();
		});
	}

	public void putTutorial(TutorialSequence tutorial, int layer) {
		currentTutorial = tutorial;
		currentLayer = tutorial.buildLayer(layer);
		currentLayerIndex = layer;

		logic.active.clear();
		logic.active.add(currentLayer.nesting);

		layerTable.clear();
		currentLayer.actions.each(a -> a.init(layerTable, currentLayer.nesting));
		layerTable.update(() -> currentLayer.actions.each(a -> a.update(currentLayer.nesting, false)));
	}
}
