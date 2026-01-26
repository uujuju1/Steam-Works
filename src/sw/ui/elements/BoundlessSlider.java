package sw.ui.elements;

import arc.func.*;
import arc.input.*;
import arc.math.*;
import arc.scene.event.*;
import arc.scene.ui.layout.*;

public class BoundlessSlider extends Table {
	public boolean dragging;
	public float drag;
	
	public Table button;
	public Floatc changed = lastDrag -> {};
	public Floatc onDrag = drag -> {};
	public float minDrag = 2.5f;
	
	public BoundlessSlider() {
		this(t -> {});
	}
	public BoundlessSlider(Cons<Table> button) {
		this(t -> {}, button);
	}
	public BoundlessSlider(Cons<Table> slider, Cons<Table> button) {
		this.button = new Table(button);
		slider.get(this);
		setup();
	}
	
	public BoundlessSlider setChanged(Floatc changed) {
		this.changed = changed;
		
		return this;
	}
	public BoundlessSlider setOnDrag(Floatc onDrag) {
		this.onDrag = onDrag;
		
		return this;
	}
	
	public void setup() {
		addChild(button);
		
		touchable = Touchable.childrenOnly;
		button.touchable = Touchable.enabled;

		update(() -> {
			float max = width/2f - button.getWidth()/2f;
			button.setPosition(
				width/2f + Mathf.clamp(drag, -max, max),
				height/2f,
				0
			);
			if (!dragging) {
				drag /= 2f;
				if (Math.abs(drag) < minDrag) drag = 0f;
			} else {
				onDrag.get(drag);
			}
		});
		
		button.addCaptureListener(new ElementGestureListener() {
			@Override
			public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
				dragging = true;
				drag += deltaX;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, KeyCode key) {
				dragging = false;
				changed.get(drag);
				float max = width/2f - button.getWidth()/2f;
				drag = Mathf.clamp(drag, -max, max);
			}
		});
		button.addListener(new HandCursorListener());
	}
}
