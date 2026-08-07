package sw.matryoshka.ui.elements;

import arc.func.*;
import arc.math.*;
import arc.scene.*;
import arc.scene.event.*;
import arc.scene.style.*;
import arc.struct.*;
import mindustry.gen.*;

public class Timestamp extends Element {
	public ObjectMap<Float, Runnable> timedRuns = new ObjectMap<>();

	public TimestampStyle style = new TimestampStyle();

	public Floatp progress;

	public Timestamp(Floatp progress) {
		this.progress = progress;
		Element self = this;
		addListener(new HandCursorListener());
		addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y){
				float mappedPos = Mathf.map(x, self.x, self.x + self.getWidth(), 0f, 1f);

				float[] minTime = new float[]{0f};
				Runnable[] minRun = new Runnable[1];
				timedRuns.each((time, run) -> {
					if (minTime[0] <= time && time <= mappedPos) {
						minTime[0] = time;
						minRun[0] = run;
					}
				});

				if (minRun[0] != null) minRun[0].run();
			}
		});
	}

	@Override
	public void draw() {
		validate();

		style.baseLine.draw(x, y + (1f - style.markProfile) * height / 2f, width, height * style.markProfile);
		style.passedTime.draw(x, y + (1f - style.markProfile) * height / 2f, Mathf.clamp(width * progress.get(), style.passedTime.getLeftWidth(), width), height * style.markProfile);

		timedRuns.each((time, run) -> style.timestamp.draw(x + width * time, y, style.timestampStroke, height));
	}

	public class TimestampStyle extends Style {
		public Drawable baseLine = Tex.whiteui;
		public Drawable passedTime = Tex.whiteui;

		public Drawable timestamp = Tex.whiteui;
		public float timestampStroke = 5;

		public float markProfile = 0.5f;
	}
}
