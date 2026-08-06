package sw.matryoshka.ui.elements;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.scene.ui.layout.*;
import mindustry.graphics.*;

public class HighlightElement extends Table {
	public Color tint = Pal.accent;
	public float stroke = 1f;
	public float radius = 8;
	public float areaX, areaY;

	@Override
	public void draw() {
		validate();

		Lines.stroke(stroke, tint);
		Draw.alpha(parentAlpha * tint.a);
		Lines.square(areaX, areaY, radius);
		Draw.color(Color.white, parentAlpha);

		super.draw();
	}
}
