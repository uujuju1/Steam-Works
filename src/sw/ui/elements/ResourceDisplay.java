package sw.ui.elements;

import arc.scene.ui.layout.*;
import mindustry.core.*;
import mindustry.type.*;
import mindustry.ui.*;

public class ResourceDisplay extends Table {
	public Planet currentPlanet;
	public ItemSeq items;

	public ResourceDisplay(Planet planet) {
		currentPlanet = planet;
		items = new ItemSeq();
		setBackground(Styles.black6);
		rebuild();
	}

	public void rebuild() {
		clear();
		items.clear();

		for (Sector sector : currentPlanet.sectors) {
			items.add(sector.items());
		}

		items.each((item, amount) -> {
			image(item.uiIcon).padRight(5f);
			add(UI.formatAmount(amount)).row();
		});
		if (items.toSeq().isEmpty()) {
			setBackground(null);
		} else {
			setBackground(Styles.black6);
		}
		margin(10f);
	}
}
