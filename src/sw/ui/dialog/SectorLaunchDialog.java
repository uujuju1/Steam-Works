package sw.ui.dialog;

import arc.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.scene.*;
import arc.scene.event.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.ui.dialogs.*;
import sw.*;
import sw.content.*;

public class SectorLaunchDialog extends BaseDialog {
	public SectorView view;

	public SectorLaunchDialog() {
		super("@sector.view");
		cont.clear();
		cont.stack(
			new Table(t -> t.add(view = new SectorView()))
		);

		addCaptureListener(new ElementGestureListener() {
			@Override
			public void pan(InputEvent event, float x, float y, float deltaX, float deltaY){
				view.x -= deltaX;
				view.y -= deltaY;
			}
		});
		closeOnBack();
		addToMenu();
	}

	public void addToMenu() {
		Events.run(EventType.Trigger.update, () -> {
			if (Vars.ui.planet.state.planet == SWPlanets.wendi && Vars.ui.planet.isShown()) {
				Vars.ui.planet.state.planet = Planets.serpulo;
				SWVars.sectorLaunchDialog.show();
			}
		});
	}

	public class SectorView extends Group {
		Shader background;

		public SectorView() {
			background = new Shader(Core.files.internal("shaders/screenspace.vert"), Vars.tree.get("shaders/sectorDialogBackground.frag")) {
				@Override
				public void apply() {
					setUniformf("u_resolution", Core.graphics.getWidth(), Core.graphics.getHeight());
					setUniformf("u_position", x, y);
					setUniformf("u_time", Time.time);
				}
			};
		}

		@Override
		public void draw() {
			Draw.blit(background);

			super.draw();
		}
	}
}
