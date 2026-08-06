package sw.matryoshka.actions;

import arc.scene.*;
import sw.matryoshka.world.*;

/**
 * A way to reliably modify actions within a Tutorial Sequence. Whether it is within the context of a Nesting itself or when rendering the ui.
 * <p> Some methods have a nestingContext parameter. Whenever that parameter is true, it means that the code is running within the context of a Nesting.
 * <p> Do not mess with the Nesting if nestingContext is false.
 */
public abstract class TutorialAction {
	/**
	 * Determines when {@link #start(Nesting, boolean)} and {@link #end(Nesting, boolean)} is called, aswell as for how long {@link #update(Nesting, boolean)} runs.
	 */
	public float startTime, endTime;

	public boolean started, ended;

	/**
	 * Called once when the SequenceLayer is created. nestingContext is always false.
	 */
	public void init(Group table, Nesting nesting) {

	}

	/**
	 * Called when the time passed withing a Nesting exceeds {@link #endTime}.
	 * <p> May be called twice, with different nestingContext values.
	 */
	public void end(Nesting nesting, boolean nestingContext) {

	}

	/**
	 * Called when the time passed withing a Nesting exceeds {@link #startTime}.
	 * <p> May be called twice, with different nestingContext values.
	 */
	public void start(Nesting nesting, boolean nestingContext) {

	}

	/**
	 * Called every frame.
	 */
	public void update(Nesting nesting, boolean nestingContext) {
		if (nesting.time >= startTime && !started) {
			start(nesting, nestingContext);
			started = true;
		}
		if (nesting.time >= endTime && !ended) {
			end(nesting, nestingContext);
			ended = true;
		}
	}
}
