package sw.matryoshka.actions;

import arc.scene.ui.layout.*;
import sw.matryoshka.world.*;

/**
 * Silly class name because of {@link arc.scene.actions.RunnableAction}
 * <p> Has Individual Runnables for every function.
 */
public class RunnablesAction extends TutorialAction {
	public BaseAction start = (nesting, nestingContext) -> {}, end = (nesting, nestingContext) -> {}, update = (nesting, nestingContext) -> {};
	public UiAction init = (table, nesting) -> {};

	@Override
	public void init(Table table, Nesting nesting) {
		init.run(table, nesting);
	}

	@Override
	public void end(Nesting nesting, boolean nestingContext) {
		end.run(nesting, nestingContext);
	}

	@Override
	public void start(Nesting nesting, boolean nestingContext) {
		start.run(nesting, nestingContext);
	}

	@Override
	public void update(Nesting nesting, boolean nestingContext) {
		super.update(nesting, nestingContext);
		update.run(nesting, nestingContext);
	}

	public static interface BaseAction {
		void run(Nesting nesting, boolean nestingContext);
	}
	public static interface UiAction {
		void run(Table table, Nesting nesting);
	}
}
