package sw.ui.elements;

import arc.func.*;
import arc.scene.ui.*;

public class CenterSlider extends Slider {
	public CenterSlider(float max, float stepSize, Floatc released) {
		super(-max, max, stepSize, false);

		released(() -> released.get(getValue()));

		update(() -> {
			if (!isDragging()) setValue(getValue() * 0.49f);
		});
	}
}
