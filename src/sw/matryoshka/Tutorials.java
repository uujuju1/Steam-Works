package sw.matryoshka;

import arc.scene.ui.layout.*;
import mindustry.ui.*;
import sw.matryoshka.actions.*;
import sw.matryoshka.ui.*;
import sw.matryoshka.world.*;

public class Tutorials {
	public static TutorialSequence demo = new TutorialBuilder() {{
		addLayer((() -> new SequenceLayer() {{
			nesting = new Nesting(5, 5);

			actions.add(new UIElementAction(new Table(t -> {
				t.setFillParent(true);
				t.table(Styles.black6, e -> e.labelWrap("This is a text that will appear for a moment ooooo").width(100).pad(10f));
			})) {{
				startTime = 350f;
				endTime = 450f;
			}});

			timestamp.add(60f);
			timestamp.add(300f);
			maxTime = 600f;
		}}));
	}};
}
