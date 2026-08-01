package sw.matryoshka.actions;

import arc.math.*;
import arc.scene.*;
import arc.scene.actions.*;
import sw.matryoshka.world.*;

public class UIElementAction extends TutorialAction {
	public Element element;

	public float fadeSeconds = 1;
	public Interp fadeCurve = Interp.linear;

	public UIElementAction(Element element) {
		this.element = element;
	}

	@Override
	public void init(Group table, Nesting nesting) {
		table.addChild(element);
		element.color.a = 0;
	}

	@Override
	public void end(Nesting nesting, boolean nestingContext) {
		element.actions(
			Actions.fadeOut(fadeSeconds, fadeCurve),
			Actions.remove()
		);
	}

	@Override
	public void start(Nesting nesting, boolean nestingContext) {
		element.actions(Actions.fadeIn(fadeSeconds, fadeCurve));
	}
}
