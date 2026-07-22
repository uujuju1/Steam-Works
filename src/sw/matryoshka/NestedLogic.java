package sw.matryoshka;

import arc.*;
import arc.graphics.g2d.*;
import sw.graphics.*;
import sw.matryoshka.world.*;

public class NestedLogic implements ApplicationListener {
	public boolean shoouldDraw;

	public void draw() {
		Draw.blit(SWShaders.hintBackgroundShader);
	}

	@Override
	public void update() {
		if (shoouldDraw) draw();
	}

	/**
	 * Standard vanilla like updating loop for a Nesting.
	 */
	public void updateNesting(Nesting nesting) {
		NestingContext context = nesting.getContext();

		context.begin();

		// TODO run world logic here

		context.end();
	}

	public void run(Nesting nesting, Runnable code) {
		NestingContext context = nesting.getContext();

		context.begin();

		code.run();

		context.end();
	}
}
