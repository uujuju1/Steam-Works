package sw.world.blocks.units;

import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.blocks.units.*;

public class MultiReconstructor extends Reconstructor {
	public MultiReconstructor(String name) {
		super(name);
	}

	public class MultiReconstructorBuild extends ReconstructorBuild {
		public int currentUpgrade = -1;

		@Override public boolean shouldShowConfigure(Player player) {
			return true;
		}

		@Override
		public void buildConfiguration(Table cont) {
			if (canSetCommand()) {
				super.buildConfiguration(cont);
			} else {
				Table table = new Table(Styles.black6);
				upgrades.each(upgrade -> {
					Button button = table.button(t -> {
					t.left();

					t.image(upgrade[0].uiIcon).size(40).pad(10).left().scaling(Scaling.fit);
					t.image(Icon.right).color(Pal.darkishGray).size(40).pad(10);
					t.image(upgrade[1].uiIcon).size(40).pad(10).right().scaling(Scaling.fit);

          }, Styles.flatTogglet, () -> {
					if (currentUpgrade != upgrades.indexOf(upgrade)) {
						currentUpgrade = upgrades.indexOf(upgrade);
					} else {
						currentUpgrade = -1;
					}
					}).padTop(5).padBottom(5).get();
					button.update(() -> button.setChecked(currentUpgrade == upgrades.indexOf(upgrade)));
					table.row();
				});

				ScrollPane pane = new ScrollPane(table, Styles.smallPane);
				pane.setScrollingDisabled(true, false);

				cont.add(pane).maxHeight(160);
			}
		}

		@Override
		public boolean acceptPayload(Building source, Payload payload){
			if(currentUpgrade == -1 || !(payload instanceof UnitPayload pay)) return false;
			return upgrades.get(currentUpgrade)[0] == pay.unit.type && super.acceptPayload(source, payload);
		}

		@Override
		public boolean hasUpgrade(UnitType type) {
			return currentUpgrade != -1 && super.hasUpgrade(type) && upgrades.get(currentUpgrade)[0] == type;
		}
	}
}
