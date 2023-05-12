package sw.world.blocks.force;

import arc.math.*;
import arc.scene.ui.layout.*;
import mindustry.gen.*;

public class ForceSpinner extends ForceNode {
	public float maxSpin = 1;
	public ForceSpinner(String name) {
		super(name);
	}

	public class ForceSpinnerBuild extends ForceNodeBuild {
		public boolean spinning = false;

		@Override public void buildConfiguration(Table table) {
			table.button(Icon.settings, () -> spinning = !spinning);
		}

		@Override
		public void updateTile() {
			if (spinning) {
				force().speed = Mathf.approachDelta(force().speed, maxSpin, 0.25f);
			} else {
				force().speed = Mathf.approachDelta(force().speed, 0, 0.5f);
			}
		}
	}
}
